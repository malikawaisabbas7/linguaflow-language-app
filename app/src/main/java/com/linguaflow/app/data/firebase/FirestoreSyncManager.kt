package com.linguaflow.app.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.linguaflow.app.data.database.entity.UserEntity
import com.linguaflow.app.utils.Constants
import kotlinx.coroutines.tasks.await

/**
 * Syncs local Room state (progress, profile, streaks) up to Firestore,
 * and pulls remote state down on login for cross-device continuity.
 */
class FirestoreSyncManager(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun pushUserProfile(user: UserEntity) {
        firestore.collection(Constants.Firestore.USERS_COLLECTION)
            .document(user.uid)
            .set(user)
            .await()
    }

    suspend fun pullUserProfile(uid: String): UserEntity? {
        val snapshot = firestore.collection(Constants.Firestore.USERS_COLLECTION)
            .document(uid)
            .get()
            .await()
        return snapshot.toObject(UserEntity::class.java)
    }

    suspend fun pushDailyProgress(uid: String, date: String, minutesStudied: Int, xpEarned: Int) {
        firestore.collection(Constants.Firestore.PROGRESS_COLLECTION)
            .document(uid)
            .collection("daily")
            .document(date)
            .set(mapOf("minutesStudied" to minutesStudied, "xpEarned" to xpEarned))
            .await()
    }

    suspend fun updateLeaderboardEntry(uid: String, name: String, totalXp: Int) {
        firestore.collection(Constants.Firestore.LEADERBOARD_COLLECTION)
            .document(uid)
            .set(mapOf("name" to name, "totalXp" to totalXp))
            .await()
    }
}
