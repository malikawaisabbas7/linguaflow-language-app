package com.linguaflow.app.data.database.dao

import androidx.room.*
import com.linguaflow.app.data.database.entity.VocabularyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VocabularyDao {
    @Query("SELECT * FROM vocabulary WHERE languageCode = :languageCode ORDER BY id DESC")
    fun observeAll(languageCode: String): Flow<List<VocabularyEntity>>

    @Query("SELECT * FROM vocabulary WHERE languageCode = :languageCode AND masteryLevel = :mastery")
    fun observeByMastery(languageCode: String, mastery: String): Flow<List<VocabularyEntity>>

    @Query("SELECT * FROM vocabulary WHERE nextReviewDue <= :now AND languageCode = :languageCode ORDER BY nextReviewDue ASC LIMIT :limit")
    suspend fun getDueForReview(languageCode: String, now: Long, limit: Int = 20): List<VocabularyEntity>

    @Query("SELECT * FROM vocabulary WHERE word LIKE '%' || :query || '%' OR translation LIKE '%' || :query || '%'")
    fun search(query: String): Flow<List<VocabularyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(words: List<VocabularyEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(word: VocabularyEntity): Long

    @Update
    suspend fun update(word: VocabularyEntity)

    @Query("UPDATE vocabulary SET masteryLevel = :mastery, timesReviewed = timesReviewed + 1, lastReviewedAt = :now, nextReviewDue = :nextDue WHERE id = :id")
    suspend fun recordReview(id: Long, mastery: String, now: Long, nextDue: Long)

    @Query("SELECT COUNT(*) FROM vocabulary WHERE languageCode = :languageCode AND masteryLevel = 'Mastered'")
    suspend fun countMastered(languageCode: String): Int
}
