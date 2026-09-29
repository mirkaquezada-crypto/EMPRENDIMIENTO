package com.example.data.api

import android.util.Log
import com.example.BuildConfig
import com.example.data.engine.SmartAnalysisEngine
import com.example.data.model.IdeaEvaluation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiAnalysisService {
    private const val TAG = "GeminiAnalysisService"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent"

    private val client by lazy {
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    fun isApiKeyConfigured(): Boolean {
        val key = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }
        return key.isNotBlank() && key != "MY_GEMINI_API_KEY"
    }

    suspend fun evaluateIdea(
        rawIdea: String,
        category: String,
        preferAi: Boolean = true
    ): IdeaEvaluation = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }

        if (!preferAi || apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            Log.d(TAG, "Using offline smart engine (API key not configured or preferAi is false)")
            return@withContext SmartAnalysisEngine.analyzeIdea(rawIdea, category)
        }

        try {
            val systemPrompt = """
                Eres un consultor experto senior en validación de ideas de negocio, startups y emprendimiento Lean.
                Analiza la idea del usuario de manera profunda, realista y constructiva.
                Responde EXCLUSIVAMENTE con un objeto JSON válido con los siguientes campos estrictos:
                {
                  "title": "Nombre comercial sugerido (máximo 5 palabras)",
                  "innovacion": "Análisis detallado de la innovación y propuesta de valor única",
                  "viabilidad": "Análisis de viabilidad técnica, operativa y financiera",
                  "competencia": "Análisis de la competencia existente y cómo diferenciarse",
                  "publicoObjetivo": "Segmento de clientes ideal, perfil demográfico y dolores que resuelve",
                  "proximosPasos": [
                     "Paso 1 concreto y accionable",
                     "Paso 2 concreto y accionable",
                     "Paso 3 concreto y accionable",
                     "Paso 4 concreto y accionable",
                     "Paso 5 concreto y accionable"
                  ],
                  "swotFortalezas": "Puntos fuertes de la idea",
                  "swotOportunidades": "Oportunidades de mercado para crecer",
                  "swotDebilidades": "Desafíos o puntos débiles internos",
                  "swotAmenazas": "Riesgos externos o del mercado",
                  "viabilityScore": 85,
                  "scoreLabel": "Excelente Viabilidad",
                  "monetizationStrategy": "Estrategia clave de monetización y flujo de ingresos"
                }
            """.trimIndent()

            val userContent = "Categoría: $category\nIdea de negocio a evaluar:\n\"$rawIdea\""

            val jsonBody = JSONObject().apply {
                val contents = JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", "$systemPrompt\n\n$userContent")
                            })
                        })
                    })
                }
                put("contents", contents)
                put("generationConfig", JSONObject().apply {
                    put("responseMimeType", "application/json")
                    put("temperature", 0.6)
                })
            }

            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (!response.isSuccessful || responseBody.isNullOrBlank()) {
                Log.w(TAG, "Gemini API call failed with code ${response.code}: $responseBody. Falling back to Smart Engine.")
                return@withContext SmartAnalysisEngine.analyzeIdea(rawIdea, category)
            }

            val rootJson = JSONObject(responseBody)
            val candidates = rootJson.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val contentObj = firstCandidate?.optJSONObject("content")
            val parts = contentObj?.optJSONArray("parts")
            val textPart = parts?.optJSONObject(0)?.optString("text")

            if (textPart.isNullOrBlank()) {
                return@withContext SmartAnalysisEngine.analyzeIdea(rawIdea, category)
            }

            // Parse returned JSON from Gemini
            val parsedResult = JSONObject(textPart)
            val title = parsedResult.optString("title", "Proyecto Validado")
            val innovacion = parsedResult.optString("innovacion")
            val viabilidad = parsedResult.optString("viabilidad")
            val competencia = parsedResult.optString("competencia")
            val publicoObjetivo = parsedResult.optString("publicoObjetivo")

            val stepsArray = parsedResult.optJSONArray("proximosPasos")
            val stepsList = mutableListOf<String>()
            if (stepsArray != null) {
                for (i in 0 until stepsArray.length()) {
                    stepsList.add(stepsArray.optString(i))
                }
            }
            val proximosPasos = if (stepsList.isNotEmpty()) {
                stepsList.joinToString("\n")
            } else {
                "Realizar entrevistas con clientes\nDesarrollar prototipo inicial\nMedir disposición a pagar"
            }

            val swotFortalezas = parsedResult.optString("swotFortalezas")
            val swotOportunidades = parsedResult.optString("swotOportunidades")
            val swotDebilidades = parsedResult.optString("swotDebilidades")
            val swotAmenazas = parsedResult.optString("swotAmenazas")
            val viabilityScore = parsedResult.optInt("viabilityScore", 82).coerceIn(40, 99)
            val scoreLabel = parsedResult.optString("scoreLabel", "Alta Viabilidad")
            val monetization = parsedResult.optString("monetizationStrategy", "Venta directa y suscripción")

            IdeaEvaluation(
                title = title,
                rawIdea = rawIdea,
                category = category,
                innovacion = innovacion,
                viabilidad = viabilidad,
                competencia = competencia,
                publicoObjetivo = publicoObjetivo,
                proximosPasosList = proximosPasos,
                swotFortalezas = swotFortalezas,
                swotOportunidades = swotOportunidades,
                swotDebilidades = swotDebilidades,
                swotAmenazas = swotAmenazas,
                viabilityScore = viabilityScore,
                scoreLabel = scoreLabel,
                targetNiche = publicoObjetivo.take(60),
                monetizationStrategy = monetization,
                isRealAi = true
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error generating evaluation with Gemini: ${e.message}", e)
            SmartAnalysisEngine.analyzeIdea(rawIdea, category)
        }
    }
}
