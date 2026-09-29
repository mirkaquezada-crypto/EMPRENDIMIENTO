package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ResultScreen
import com.example.ui.screens.SavedHistoryScreen
import com.example.ui.screens.StartupGuideScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.IdeaValidatorViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: IdeaValidatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: IdeaValidatorViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val ideaInput by viewModel.ideaInput.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val validationError by viewModel.validationError.collectAsStateWithLifecycle()
    val isAnalyzing by viewModel.isAnalyzing.collectAsStateWithLifecycle()
    val currentEvaluation by viewModel.currentEvaluation.collectAsStateWithLifecycle()
    val isCurrentSaved by viewModel.isCurrentSaved.collectAsStateWithLifecycle()

    val savedIdeas by viewModel.savedIdeas.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val filterCategory by viewModel.filterCategory.collectAsStateWithLifecycle()
    val filterFavoritesOnly by viewModel.filterFavoritesOnly.collectAsStateWithLifecycle()

    // Back handling for sub-screens
    if (currentScreen != AppScreen.EVALUATOR) {
        BackHandler {
            viewModel.navigateTo(AppScreen.EVALUATOR)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (currentScreen != AppScreen.RESULT) {
                NavigationBar(
                    modifier = Modifier.testTag("bottom_nav_bar")
                ) {
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.EVALUATOR,
                        onClick = { viewModel.navigateTo(AppScreen.EVALUATOR) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Evaluar"
                            )
                        },
                        label = { Text("Evaluar") },
                        modifier = Modifier.testTag("nav_evaluator")
                    )

                    NavigationBarItem(
                        selected = currentScreen == AppScreen.SAVED_HISTORY,
                        onClick = { viewModel.navigateTo(AppScreen.SAVED_HISTORY) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = "Guardadas"
                            )
                        },
                        label = { Text("Guardadas") },
                        modifier = Modifier.testTag("nav_saved")
                    )

                    NavigationBarItem(
                        selected = currentScreen == AppScreen.STARTUP_GUIDE,
                        onClick = { viewModel.navigateTo(AppScreen.STARTUP_GUIDE) },
                        icon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                contentDescription = "Guía"
                            )
                        },
                        label = { Text("Guía") },
                        modifier = Modifier.testTag("nav_guide")
                    )
                }
            }
        }
    ) { innerPadding ->
        when (currentScreen) {
            AppScreen.EVALUATOR -> {
                HomeScreen(
                    viewModel = viewModel,
                    ideaInput = ideaInput,
                    selectedCategory = selectedCategory,
                    validationError = validationError,
                    isAnalyzing = isAnalyzing,
                    modifier = Modifier.padding(innerPadding)
                )
            }

            AppScreen.RESULT -> {
                val eval = currentEvaluation
                if (eval != null) {
                    ResultScreen(
                        evaluation = eval,
                        isSaved = isCurrentSaved,
                        onBack = { viewModel.navigateTo(AppScreen.EVALUATOR) },
                        onSave = { viewModel.saveCurrentEvaluation() },
                        onToggleFavorite = { viewModel.toggleFavorite(eval) },
                        onNewEvaluation = {
                            viewModel.clearForm()
                            viewModel.navigateTo(AppScreen.EVALUATOR)
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                } else {
                    viewModel.navigateTo(AppScreen.EVALUATOR)
                }
            }

            AppScreen.SAVED_HISTORY -> {
                SavedHistoryScreen(
                    savedIdeas = savedIdeas,
                    searchQuery = searchQuery,
                    filterCategory = filterCategory,
                    filterFavoritesOnly = filterFavoritesOnly,
                    categories = viewModel.availableCategories,
                    onSearchChanged = { viewModel.setSearchQuery(it) },
                    onCategoryChanged = { viewModel.setFilterCategory(it) },
                    onToggleFavoritesOnly = { viewModel.toggleFilterFavorites() },
                    onOpenIdea = { viewModel.openSavedEvaluation(it) },
                    onToggleFavorite = { viewModel.toggleFavorite(it) },
                    onDeleteIdea = { viewModel.deleteIdea(it) },
                    onGoToEvaluator = { viewModel.navigateTo(AppScreen.EVALUATOR) },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            AppScreen.STARTUP_GUIDE -> {
                StartupGuideScreen(
                    onGoToEvaluator = { viewModel.navigateTo(AppScreen.EVALUATOR) },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
