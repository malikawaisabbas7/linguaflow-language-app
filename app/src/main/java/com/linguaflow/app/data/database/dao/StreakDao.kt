package com.linguaflow.app.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.linguaflow.app.data.database.entity.StreakEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StreakDao {
    @Query("SELECT * FROM streaks WHERE id = 1")
    fun observe(): Flow<StreakEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(streak: StreakEntity)
}
