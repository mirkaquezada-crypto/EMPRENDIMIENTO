package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_ideas")
data class IdeaEvaluation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val rawIdea: String,
    val category: String,
    val innovacion: String,
    val viabilidad: String,
    val competencia: String,
    val publicoObjetivo: String,
    val proximosPasosList: String, // Comma or newline separated steps
    val swotFortalezas: String,
    val swotOportunidades: String,
    val swotDebilidades: String,
    val swotAmenazas: String,
    val viabilityScore: Int,
    val scoreLabel: String,
    val targetNiche: String = "",
    val monetizationStrategy: String = "",
    val isFavorite: Boolean = false,
    val isRealAi: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun getSteps(): List<String> {
        return proximosPasosList.split("\n").filter { it.isNotBlank() }
    }
}
