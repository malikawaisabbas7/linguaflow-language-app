package com.linguaflow.app.data.database.dao

import androidx.room.*
import com.linguaflow.app.data.database.entity.ProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {
    @Query("SELECT * FROM progress ORDER BY date DESC LIMIT :days")
    fun observeRecent(days: Int = 30): Flow<List<ProgressEntity>>

    @Query("SELECT * FROM progress WHERE date = :date LIMIT 1")
    suspend fun getForDate(date: String): ProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(progress: ProgressEntity)

    // CHANGED 'progress_logs' to 'progress' here:
    @Query("SELECT SUM(xpEarned) FROM progress")
    suspend fun getTotalXp(): Int?
}
