package com.example.ui.screens

import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SettingsSuggest
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.IdeaEvaluation
import com.example.ui.components.ActionStepsCard
import com.example.ui.components.PillarCard
import com.example.ui.components.ScoreGaugeCard
import com.example.ui.components.SwotMatrixCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    evaluation: IdeaEvaluation,
    isSaved: Boolean,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onToggleFavorite: () -> Unit,
    onNewEvaluation: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Diagnóstico de la Idea",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("result_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier.testTag("result_favorite_button")
                    ) {
                        Icon(
                            imageVector = if (evaluation.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = if (evaluation.isFavorite) Color(0xFFE11D48) else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(
                        onClick = { shareReport(context, evaluation) },
                        modifier = Modifier.testTag("result_share_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Compartir reporte"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .testTag("result_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Card with Project Title and Category
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = evaluation.category,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        if (evaluation.isRealAi) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFE0F2FE)
                            ) {
                                Text(
                                    text = "✨ Gemini 3.5 Flash",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color(0xFF0369A1),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = evaluation.title,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "\"${evaluation.rawIdea}\"",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontStyle = FontStyle.Italic,
                                lineHeight = 18.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }

            // 1. Viability Gauge
            ScoreGaugeCard(
                score = evaluation.viabilityScore,
                label = evaluation.scoreLabel
            )

            // Monetization Strategy Highlight
            if (evaluation.monetizationStrategy.isNotBlank()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFEF3C7)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = null,
                            tint = Color(0xFFB45309),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Modelo de Ingresos Sugerido",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFF92400E)
                            )
                            Text(
                                text = evaluation.monetizationStrategy,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF78350F)
                            )
                        }
                    }
                }
            }

            // The 5 Evaluation Pillars
            // Pillar 1: Innovación
            PillarCard(
                title = "Innovación & Propuesta de Valor",
                content = evaluation.innovacion,
                icon = Icons.Default.Lightbulb,
                accentColor = Color(0xFF00897B),
                tag = "innovacion"
            )

            // Pillar 2: Viabilidad
            PillarCard(
                title = "Viabilidad Técnica & Operativa",
                content = evaluation.viabilidad,
                icon = Icons.Default.SettingsSuggest,
                accentColor = Color(0xFF2563EB),
                tag = "viabilidad"
            )

            // Pillar 3: Competencia
            PillarCard(
                title = "Competencia & Diferenciación",
                content = evaluation.competencia,
                icon = Icons.Default.Shield,
                accentColor = Color(0xFF7C3AED),
                tag = "competencia"
            )

            // Pillar 4: Público Objetivo
            PillarCard(
                title = "Público Objetivo & Nicho",
                content = evaluation.publicoObjetivo,
                icon = Icons.Default.Groups,
                accentColor = Color(0xFFD97706),
                tag = "publico"
            )

            // Pillar 5: Próximos Pasos (Interactive Checklist)
            ActionStepsCard(
                steps = evaluation.getSteps()
            )

            // SWOT Matrix
            SwotMatrixCard(
                fortalezas = evaluation.swotFortalezas,
                oportunidades = evaluation.swotOportunidades,
                debilidades = evaluation.swotDebilidades,
                amenazas = evaluation.swotAmenazas
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onSave,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("save_evaluation_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSaved) Color(0xFF10B981) else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Default.Check else Icons.Default.BookmarkBorder,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isSaved) "Guardada" else "Guardar Idea",
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = onNewEvaluation,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("new_evaluation_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Nueva Idea", fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

private fun shareReport(context: Context, evaluation: IdeaEvaluation) {
    val report = buildString {
        appendLine("🚀 REPORTE DE VALIDACIÓN DE IDEA DE NEGOCIO")
        appendLine("Título: ${evaluation.title}")
        appendLine("Categoría: ${evaluation.category}")
        appendLine("Índice de Viabilidad: ${evaluation.viabilityScore}/100 (${evaluation.scoreLabel})")
        appendLine("\n💡 IDEA:")
        appendLine("\"${evaluation.rawIdea}\"")
        appendLine("\n✨ INNOVACIÓN & PROPUESTA DE VALOR:")
        appendLine(evaluation.innovacion)
        appendLine("\n⚙️ VIABILIDAD TÉCNICA & OPERATIVA:")
        appendLine(evaluation.viabilidad)
        appendLine("\n🛡️ COMPETENCIA & DIFERENCIACIÓN:")
        appendLine(evaluation.competencia)
        appendLine("\n🎯 PÚBLICO OBJETIVO:")
        appendLine(evaluation.publicoObjetivo)
        appendLine("\n📋 PRÓXIMOS PASOS ACCIONABLES:")
        evaluation.getSteps().forEachIndexed { index, step ->
            appendLine("${index + 1}. $step")
        }
        appendLine("\n📊 ANÁLISIS FODA:")
        appendLine("• Fortalezas: ${evaluation.swotFortalezas}")
        appendLine("• Oportunidades: ${evaluation.swotOportunidades}")
        appendLine("• Debilidades: ${evaluation.swotDebilidades}")
        appendLine("• Amenazas: ${evaluation.swotAmenazas}")
        appendLine("\nGenerado con Validador de Ideas con IA.")
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Reporte de Validación: ${evaluation.title}")
        putExtra(Intent.EXTRA_TEXT, report)
    }
    context.startActivity(Intent.createChooser(intent, "Compartir Reporte de Idea"))
}
