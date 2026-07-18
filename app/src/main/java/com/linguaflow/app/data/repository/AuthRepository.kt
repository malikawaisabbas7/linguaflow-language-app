package com.linguaflow.app.data.repository

import com.google.firebase.auth.FirebaseUser
import com.linguaflow.app.data.database.dao.UserDao
import com.linguaflow.app.data.database.entity.UserEntity
import com.linguaflow.app.data.firebase.FirebaseAuthManager
import com.linguaflow.app.data.firebase.FirestoreSyncManager
import kotlinx.coroutines.flow.Flow

class AuthRepository(
    private val authManager: FirebaseAuthManager,
    private val syncManager: FirestoreSyncManager,
    private val userDao: UserDao
) {
    val currentUser: FirebaseUser? get() = authManager.currentUser

    fun observeLocalUser(uid: String): Flow<UserEntity?> = userDao.observeUser(uid)

    suspend fun register(
        name: String,
        email: String,
        password: String,
        nativeLanguage: String,
        targetLanguage: String,
        proficiencyLevel: String
    ): Result<UserEntity> = runCatching {
        val firebaseUser = authManager.register(email, password).getOrThrow()
        val user = UserEntity(
            uid = firebaseUser.uid,
            name = name,
            email = email,
            nativeLanguage = nativeLanguage,
            targetLanguage = targetLanguage,
            proficiencyLevel = proficiencyLevel
        )
        userDao.upsert(user)
        syncManager.pushUserProfile(user)
        user
    }

    suspend fun login(email: String, password: String): Result<UserEntity?> = runCatching {
        val firebaseUser = authManager.login(email, password).getOrThrow()
        val remoteUser = syncManager.pullUserProfile(firebaseUser.uid)
        remoteUser?.let { userDao.upsert(it) }
        userDao.getUser(firebaseUser.uid)
    }

    suspend fun sendPasswordReset(email: String): Result<Unit> = authManager.sendPasswordReset(email)

    fun logout() = authManager.signOut()
}
