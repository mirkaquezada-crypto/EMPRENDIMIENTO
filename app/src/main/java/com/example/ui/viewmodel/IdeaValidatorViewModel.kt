package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.GeminiAnalysisService
import com.example.data.db.AppDatabase
import com.example.data.model.IdeaEvaluation
import com.example.data.repository.IdeaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class PresetIdea(
    val title: String,
    val category: String,
    val description: String,
    val iconEmoji: String
)

enum class AppScreen {
    EVALUATOR,
    RESULT,
    SAVED_HISTORY,
    STARTUP_GUIDE
}

class IdeaValidatorViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: IdeaRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = IdeaRepository(db.ideaDao())
    }

    val availableCategories = listOf(
        "🌱 Productos Orgánicos",
        "💻 Tecnología & IA",
        "☕ Gastronomía & Alimentos",
        "🎓 Educación & Aprendizaje",
        "⚡ Servicios Profesionales",
        "🛍️ Comercio & E-Commerce",
        "🏥 Salud & Bienestar",
        "♻️ Sostenibilidad & Reciclaje"
    )

    val samplePresets = listOf(
        PresetIdea(
            title = "Canasta Orgánica Local",
            category = "🌱 Productos Orgánicos",
            description = "Servicio de suscripción semanal de frutas, verduras y huevos orgánicos cosechados por productores locales con empaques biodegradables y entrega en bicicleta.",
            iconEmoji = "🍏"
        ),
        PresetIdea(
            title = "Asistente Nutricional IA",
            category = "💻 Tecnología & IA",
            description = "App móvil con visión artificial que escanea platos de comida y etiquetas de supermercado para alertar sobre ingredientes nocivos para diabéticos e intolerantes al gluten.",
            iconEmoji = "📱"
        ),
        PresetIdea(
            title = "Cafetería Coworking Botánica",
            category = "☕ Gastronomía & Alimentos",
            description = "Cafetería de especialidad inmersa en un vivero botánico con internet de alta velocidad, cabinas insonorizadas para videollamadas y repostería artesanal sin azúcar.",
            iconEmoji = "☕"
        ),
        PresetIdea(
            title = "Juguetes Montessori en Renta",
            category = "🎓 Educación & Aprendizaje",
            description = "Membresía circular para alquilar sets de juguetes educativos de madera y material Montessori que se intercambian cada mes a medida que el niño crece.",
            iconEmoji = "🧩"
        )
    )

    private val _currentScreen = MutableStateFlow(AppScreen.EVALUATOR)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _ideaInput = MutableStateFlow("")
    val ideaInput: StateFlow<String> = _ideaInput.asStateFlow()

    private val _selectedCategory = MutableStateFlow(availableCategories[0])
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _validationError = MutableStateFlow<String?>(null)
    val validationError: StateFlow<String?> = _validationError.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    private val _currentEvaluation = MutableStateFlow<IdeaEvaluation?>(null)
    val currentEvaluation: StateFlow<IdeaEvaluation?> = _currentEvaluation.asStateFlow()

    private val _isCurrentSaved = MutableStateFlow(false)
    val isCurrentSaved: StateFlow<Boolean> = _isCurrentSaved.asStateFlow()

    // Filters for saved ideas
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filterCategory = MutableStateFlow("Todas")
    val filterCategory: StateFlow<String> = _filterCategory.asStateFlow()

    private val _filterFavoritesOnly = MutableStateFlow(false)
    val filterFavoritesOnly: StateFlow<Boolean> = _filterFavoritesOnly.asStateFlow()

    val savedIdeas: StateFlow<List<IdeaEvaluation>> = combine(
        repository.allIdeas,
        _searchQuery,
        _filterCategory,
        _filterFavoritesOnly
    ) { ideas, query, cat, favOnly ->
        ideas.filter { item ->
            val matchesQuery = query.isBlank() ||
                    item.title.contains(query, ignoreCase = true) ||
                    item.rawIdea.contains(query, ignoreCase = true) ||
                    item.category.contains(query, ignoreCase = true)

            val matchesCategory = cat == "Todas" || item.category == cat
            val matchesFav = !favOnly || item.isFavorite
            matchesQuery && matchesCategory && matchesFav
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val isGeminiAvailable = GeminiAnalysisService.isApiKeyConfigured()

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun onIdeaInputChanged(text: String) {
        _ideaInput.value = text
        if (text.length >= 20 && _validationError.value != null) {
            _validationError.value = null
        }
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
    }

    fun applyPreset(preset: PresetIdea) {
        _ideaInput.value = preset.description
        _selectedCategory.value = preset.category
        _validationError.value = null
    }

    fun validateAndAnalyze() {
        val input = _ideaInput.value.trim()
        if (input.length < 20) {
            _validationError.value = "Por favor, escribe una descripción más detallada de tu idea (mínimo 20 caracteres)."
            return
        }

        _validationError.value = null
        _isAnalyzing.value = true

        viewModelScope.launch {
            try {
                val result = GeminiAnalysisService.evaluateIdea(
                    rawIdea = input,
                    category = _selectedCategory.value,
                    preferAi = true
                )
                _currentEvaluation.value = result
                _isCurrentSaved.value = false
                _currentScreen.value = AppScreen.RESULT
            } catch (e: Exception) {
                _validationError.value = "Error al analizar la idea: ${e.localizedMessage}"
            } finally {
                _isAnalyzing.value = false
            }
        }
    }

    fun saveCurrentEvaluation() {
        val current = _currentEvaluation.value ?: return
        viewModelScope.launch {
            val newId = repository.insert(current)
            _currentEvaluation.value = current.copy(id = newId)
            _isCurrentSaved.value = true
        }
    }

    fun openSavedEvaluation(item: IdeaEvaluation) {
        _currentEvaluation.value = item
        _isCurrentSaved.value = true
        _currentScreen.value = AppScreen.RESULT
    }

    fun toggleFavorite(item: IdeaEvaluation) {
        viewModelScope.launch {
            repository.toggleFavorite(item)
            if (_currentEvaluation.value?.id == item.id) {
                _currentEvaluation.value = _currentEvaluation.value?.copy(isFavorite = !item.isFavorite)
            }
        }
    }

    fun deleteIdea(item: IdeaEvaluation) {
        viewModelScope.launch {
            repository.delete(item)
            if (_currentEvaluation.value?.id == item.id) {
                _isCurrentSaved.value = false
            }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setFilterCategory(category: String) {
        _filterCategory.value = category
    }

    fun toggleFilterFavorites() {
        _filterFavoritesOnly.value = !_filterFavoritesOnly.value
    }

    fun clearForm() {
        _ideaInput.value = ""
        _validationError.value = null
    }
}
