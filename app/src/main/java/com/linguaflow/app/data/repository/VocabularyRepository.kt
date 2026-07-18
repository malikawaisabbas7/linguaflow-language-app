package com.linguaflow.app.data.repository

import com.linguaflow.app.data.database.dao.VocabularyDao
import com.linguaflow.app.data.database.entity.VocabularyEntity
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

class VocabularyRepository(private val vocabularyDao: VocabularyDao) {

    // This is the function the VocabularyBankScreen calls
    suspend fun insert(vocabulary: VocabularyEntity) {
        vocabularyDao.insert(vocabulary)
    }

    // Keep this for backward compatibility or the UI's specific call
    suspend fun insertVocabulary(vocabulary: VocabularyEntity) {
        vocabularyDao.insert(vocabulary)
    }

    fun observeAll(languageCode: String): Flow<List<VocabularyEntity>> =
        vocabularyDao.observeAll(languageCode)

    fun search(query: String): Flow<List<VocabularyEntity>> =
        vocabularyDao.search(query)

    suspend fun getDueFlashcards(languageCode: String, limit: Int = 20): List<VocabularyEntity> =
        vocabularyDao.getDueForReview(languageCode, System.currentTimeMillis(), limit)

    suspend fun addCustomWord(word: VocabularyEntity) =
        vocabularyDao.insert(word)

    suspend fun addCustomWordsBulk(words: List<VocabularyEntity>) =
        vocabularyDao.insertAll(words)

    /**
     * Simple spaced-repetition style scheduling:
     * known -> push next review further out, unknown -> review again soon.
     */
    suspend fun recordSwipe(id: Long, known: Boolean) {
        val mastery: String
        val nextDueOffsetDays: Long
        if (known) {
            mastery = "Mastered"
            nextDueOffsetDays = 7
        } else {
            mastery = "Learning"
            nextDueOffsetDays = 1
        }
        val nextDue = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(nextDueOffsetDays)
        vocabularyDao.recordReview(id, mastery, System.currentTimeMillis(), nextDue)
    }

    suspend fun masteredCount(languageCode: String): Int =
        vocabularyDao.countMastered(languageCode)
}