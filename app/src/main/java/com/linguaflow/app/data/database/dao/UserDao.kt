package com.linguaflow.app.data.database.dao

import androidx.room.*
import com.linguaflow.app.data.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE uid = :uid LIMIT 1")
    fun observeUser(uid: String): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE uid = :uid LIMIT 1")
    suspend fun getUser(uid: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(user: UserEntity)

    @Query("UPDATE users SET totalXp = totalXp + :xp WHERE uid = :uid")
    suspend fun addXp(uid: String, xp: Int)

    @Query("UPDATE users SET currentStreak = :streak, longestStreak = MAX(longestStreak, :streak) WHERE uid = :uid")
    suspend fun updateStreak(uid: String, streak: Int)

    @Query("DELETE FROM users WHERE uid = :uid")
    suspend fun delete(uid: String)
}
