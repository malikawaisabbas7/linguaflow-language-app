package com.linguaflow.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.linguaflow.app.data.database.entity.QuizResultEntity
import com.linguaflow.app.data.database.entity.VocabularyEntity
import com.linguaflow.app.di.AppContainer
import com.linguaflow.app.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class QuizQuestion(
    val word: VocabularyEntity,
    val options: List<String>,
    val correctAnswer: String
)

data class QuizUiState(
    val questions: List<QuizQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val correctCount: Int = 0,
    val selectedAnswer: String? = null,
    val showFeedback: Boolean = false,
    val isComplete: Boolean = false,
    val lastResultId: Long = 0L,
    val isLoading: Boolean = true
)

class QuizViewModel(private val container: AppContainer, private val lessonId: Long) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState

    init {
        viewModelScope.launch {
            val words = container.vocabularyRepository.observeAll("es")
            // Take a one-shot snapshot for the quiz session
            words.collect { allWords ->
                if (_uiState.value.questions.isEmpty() && allWords.isNotEmpty()) {
                    val pool = allWords.shuffled().take(8)
                    val questions = pool.map { word ->
                        val distractors = allWords.filter { it.id != word.id }.shuffled().take(3).map { it.translation }
                        QuizQuestion(
                            word = word,
                            options = (distractors + word.translation).shuffled(),
                            correctAnswer = word.translation
                        )
                    }
                    _uiState.value = _uiState.value.copy(questions = questions, isLoading = false)
                }
            }
        }
    }

    fun selectAnswer(answer: String) {
        if (_uiState.value.showFeedback) return
        val question = _uiState.value.questions.getOrNull(_uiState.value.currentIndex) ?: return
        val isCorrect = answer == question.correctAnswer
        _uiState.value = _uiState.value.copy(
            selectedAnswer = answer,
            showFeedback = true,
            correctCount = _uiState.value.correctCount + if (isCorrect) 1 else 0
        )
    }

    fun nextQuestion() {
        val nextIndex = _uiState.value.currentIndex + 1
        if (nextIndex >= _uiState.value.questions.size) {
            finishQuiz()
        } else {
            _uiState.value = _uiState.value.copy(
                currentIndex = nextIndex,
                selectedAnswer = null,
                showFeedback = false
            )
        }
    }

    private fun finishQuiz() {
        viewModelScope.launch {
            val total = _uiState.value.questions.size
            val correct = _uiState.value.correctCount
            val accuracy = if (total > 0) (correct * 100 / total) else 0
            val xp = correct * Constants.XP_PER_QUIZ_CORRECT_ANSWER
            val resultId = container.quizRepository.saveResult(
                QuizResultEntity(
                    lessonId = lessonId,
                    languageCode = "es",
                    totalQuestions = total,
                    correctAnswers = correct,
                    xpEarned = xp,
                    accuracyPercent = accuracy
                )
            )
            container.progressRepository.logStudySession(
                minutes = 5, wordsLearned = 0, xpEarned = xp, lessonCompleted = false
            )
            _uiState.value = _uiState.value.copy(isComplete = true, lastResultId = resultId)
        }
    }

    companion object {
        fun factory(container: AppContainer, lessonId: Long) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = QuizViewModel(container, lessonId) as T
        }
    }
}
