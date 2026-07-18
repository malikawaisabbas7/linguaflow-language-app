package com.linguaflow.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.linguaflow.app.data.database.entity.LessonEntity
import com.linguaflow.app.data.database.entity.VocabularyEntity
import com.linguaflow.app.di.AppContainer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class LessonsUiState(
    val allLessons: List<LessonEntity> = emptyList(),
    val selectedCategory: String = "All"
)

class LessonsViewModel(private val container: AppContainer) : ViewModel() {

    private val _uiState = MutableStateFlow(LessonsUiState())
    val uiState: StateFlow<LessonsUiState> = _uiState

    init {
        viewModelScope.launch {
            container.lessonRepository.observeLessons("es").collectLatest { lessons ->
                _uiState.value = _uiState.value.copy(allLessons = lessons)
            }
        }
    }

    fun selectCategory(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    companion object {
        val categories = listOf("All", "Vocabulary", "Grammar", "Phrases", "Listening", "Speaking", "Culture")

        fun factory(container: AppContainer) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = LessonsViewModel(container) as T
        }
    }
}

data class LessonDetailUiState(
    val lesson: LessonEntity? = null,
    val vocabulary: List<VocabularyEntity> = emptyList(),
    val isLoading: Boolean = true
)

class LessonDetailViewModel(private val container: AppContainer, private val lessonId: Long) : ViewModel() {

    private val _uiState = MutableStateFlow(LessonDetailUiState())
    val uiState: StateFlow<LessonDetailUiState> = _uiState

    init {
        viewModelScope.launch {
            val lesson = container.lessonRepository.getLesson(lessonId)
            _uiState.value = _uiState.value.copy(lesson = lesson, isLoading = false)
        }
        viewModelScope.launch {
            container.vocabularyRepository.observeAll("es").collectLatest { words ->
                _uiState.value = _uiState.value.copy(vocabulary = words.filter { it.lessonId == lessonId })
            }
        }
    }

    fun markLessonLearned() {
        viewModelScope.launch {
            container.lessonRepository.completeLesson(lessonId)
            container.progressRepository.logStudySession(
                minutes = _uiState.value.lesson?.estimatedMinutes ?: 5,
                wordsLearned = _uiState.value.vocabulary.size,
                xpEarned = com.linguaflow.app.utils.Constants.XP_PER_LESSON,
                lessonCompleted = true
            )
        }
    }

    companion object {
        fun factory(container: AppContainer, lessonId: Long) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                LessonDetailViewModel(container, lessonId) as T
        }
    }
}
