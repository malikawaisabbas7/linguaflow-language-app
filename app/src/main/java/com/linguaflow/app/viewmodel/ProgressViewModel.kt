package com.linguaflow.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.linguaflow.app.data.database.entity.AchievementEntity
import com.linguaflow.app.data.database.entity.ProgressEntity
import com.linguaflow.app.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class ProgressUiState(
    val recentProgress: List<ProgressEntity> = emptyList(),
    val achievements: List<AchievementEntity> = emptyList(),
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val totalWordsLearned: Int = 0,
    val totalLessonsCompleted: Int = 0,
    val averageQuizAccuracy: Int = 0,
    val totalQuizzesTaken: Int = 0
)

class ProgressViewModel(private val container: AppContainer) : ViewModel() {

    private val _uiState = MutableStateFlow(ProgressUiState())
    val uiState: StateFlow<ProgressUiState> = _uiState

    init {
        viewModelScope.launch {
            container.progressRepository.observeRecentProgress(30).collectLatest { progress ->
                _uiState.value = _uiState.value.copy(
                    recentProgress = progress,
                    totalWordsLearned = progress.sumOf { it.wordsLearned },
                    totalLessonsCompleted = progress.sumOf { it.lessonsCompleted }
                )
            }
        }
        viewModelScope.launch {
            container.progressRepository.observeStreak().collectLatest { streak ->
                _uiState.value = _uiState.value.copy(
                    currentStreak = streak?.currentStreak ?: 0,
                    longestStreak = streak?.longestStreak ?: 0
                )
            }
        }
        viewModelScope.launch {
            container.progressRepository.observeAchievements().collectLatest { achievements ->
                _uiState.value = _uiState.value.copy(achievements = achievements)
            }
        }
        viewModelScope.launch {
            val avg = container.quizRepository.averageAccuracy()
            val total = container.quizRepository.totalTaken()
            _uiState.value = _uiState.value.copy(averageQuizAccuracy = avg.toInt(), totalQuizzesTaken = total)
        }
    }

    companion object {
        fun factory(container: AppContainer) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = ProgressViewModel(container) as T
        }
    }
}
