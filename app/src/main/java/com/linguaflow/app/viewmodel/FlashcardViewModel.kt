package com.linguaflow.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.linguaflow.app.data.database.entity.VocabularyEntity
import com.linguaflow.app.di.AppContainer
import com.linguaflow.app.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class FlashcardUiState(
    val deck: List<VocabularyEntity> = emptyList(),
    val currentIndex: Int = 0,
    val sessionComplete: Boolean = false,
    val isLoading: Boolean = true,
    val currentLanguage: String = "Spanish" // Track current language
)

class FlashcardViewModel(private val container: AppContainer) : ViewModel() {

    private val _uiState = MutableStateFlow(FlashcardUiState())
    val uiState: StateFlow<FlashcardUiState> = _uiState

    init {
        observeLanguageAndLoadDeck()
    }

    /**
     * Instead of a one-time load, we "Observe" the language preference.
     * If the user switches from Spanish to French in Settings,
     * this block triggers automatically.
     */
    private fun observeLanguageAndLoadDeck() {
        viewModelScope.launch {
            container.preferencesManager.targetLanguageFlow.collectLatest { language ->
                _uiState.value = _uiState.value.copy(isLoading = true, currentLanguage = language)

                // Convert "Spanish" to "es", "French" to "fr" etc. if your DB uses codes
                val langCode = mapLanguageToCode(language)

                val due = container.vocabularyRepository.getDueFlashcards(langCode)
                _uiState.value = _uiState.value.copy(
                    deck = due,
                    isLoading = false,
                    currentIndex = 0,
                    sessionComplete = false
                )
            }
        }
    }

    /**
     * FEATURE: Add Custom Card
     * This function will be called when the user clicks the (+) button
     * and enters a new word.
     */
    fun addCustomCard(word: String, translation: String) {
        viewModelScope.launch {
            val currentLang = _uiState.value.currentLanguage
            val newVocab = VocabularyEntity(
                id = 0L,
                lessonId = 0L, // Matches val lessonId: Long?
                languageCode = mapLanguageToCode(currentLang),
                word = word,
                translation = translation,
                pronunciation = "", // Matches val pronunciation: String
                audioUrl = null,    // Matches val audioUrl: String?
                exampleSentence = "",
                exampleSentenceTranslation = "",
                category = "Custom",
                masteryLevel = "Learning",
                timesReviewed = 0,
                lastReviewedAt = System.currentTimeMillis(), // FIXED NAME
                nextReviewDue = System.currentTimeMillis(),  // FIXED NAME
                isCustom = true                              // It's a custom word!
            )

            // Make sure your repository has a function named 'insert'
            // If it's named 'insertVocabulary', change this line to .insertVocabulary(newVocab)
            container.vocabularyRepository.insert(newVocab)

            // Reload the deck
            val langCode = mapLanguageToCode(currentLang)
            val due = container.vocabularyRepository.getDueFlashcards(langCode)
            _uiState.value = _uiState.value.copy(deck = due)
        }
    }

    fun swipe(known: Boolean) {
        val current = _uiState.value.deck.getOrNull(_uiState.value.currentIndex) ?: return
        viewModelScope.launch {
            container.vocabularyRepository.recordSwipe(current.id, known)
            val nextIndex = _uiState.value.currentIndex + 1
            if (nextIndex >= _uiState.value.deck.size) {
                container.progressRepository.logStudySession(
                    minutes = 3,
                    wordsLearned = 0,
                    xpEarned = Constants.XP_PER_FLASHCARD_SESSION,
                    lessonCompleted = false
                )
                _uiState.value = _uiState.value.copy(sessionComplete = true)
            } else {
                _uiState.value = _uiState.value.copy(currentIndex = nextIndex)
            }
        }
    }

    // Helper to match your Database naming convention
    private fun mapLanguageToCode(name: String): String {
        return when (name) {
            "Spanish" -> "es"
            "French" -> "fr"
            "German" -> "de"
            "Italian" -> "it"
            "Japanese" -> "ja"
            else -> "es"
        }
    }

    companion object {
        fun factory(container: AppContainer) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = FlashcardViewModel(container) as T
        }
    }
}