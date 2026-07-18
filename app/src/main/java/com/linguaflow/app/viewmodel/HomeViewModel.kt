package com.linguaflow.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.linguaflow.app.data.database.entity.LessonEntity
import com.linguaflow.app.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val userName: String = "",
    val currentStreak: Int = 0,
    val totalXp: Int = 0,
    val wordsLearned: Int = 0,
    val dailyGoalMinutes: Int = 10,
    val minutesStudiedToday: Int = 0,
    val lessons: List<LessonEntity> = emptyList(),
    val motivationalQuote: String = "Every word you learn opens a new door.",
    val currentLanguage: String = "Spanish"
)

class HomeViewModel(private val container: AppContainer) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        // 1. Observe User Preferences (Daily Goal & User Name)
        viewModelScope.launch {
            container.preferencesManager.dailyGoalFlow.collectLatest { goal ->
                _uiState.update { it.copy(dailyGoalMinutes = goal) }
            }
        }

        // 2. Observe Language and Sync Lessons + Stats
        viewModelScope.launch {
            container.preferencesManager.targetLanguageFlow.collectLatest { language ->
                val langCode = languageCodeFor(language)

                // Fetch lessons for the specific language
                // Note: observeLessons should ideally take the language code
                container.lessonRepository.observeLessons(langCode).collectLatest { lessonList ->
                    _uiState.update { it.copy(
                        lessons = lessonList,
                        currentLanguage = language
                    ) }
                }
            }
        }

        // 3. Observe Study Progress (Minutes & XP)
        viewModelScope.launch {
            container.progressRepository.observeRecentProgress(1).collectLatest { progressList ->
                val today = progressList.firstOrNull()
                _uiState.update { it.copy(
                    minutesStudiedToday = today?.minutesStudied ?: 0,
                    wordsLearned = today?.wordsLearned ?: it.wordsLearned
                ) }
            }
        }

        // 4. Observe Global Stats (Streak & Total XP)
        viewModelScope.launch {
            container.progressRepository.observeStreak().collectLatest { streak ->
                val totalXp = container.progressRepository.getTotalXp() // Suspend call
                _uiState.update { it.copy(
                    currentStreak = streak?.currentStreak ?: 0,
                    totalXp = totalXp
                ) }
            }
        }
    }

    private fun languageCodeFor(name: String): String = when (name) {
        "Spanish" -> "es"
        "French" -> "fr"
        "German" -> "de"
        "Japanese" -> "ja"
        "Chinese" -> "zh"
        "Arabic" -> "ar"
        "Italian" -> "it"
        "Korean" -> "ko"
        else -> "es"
    }

    companion object {
        fun factory(container: AppContainer) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = HomeViewModel(container) as T
        }
    }
}