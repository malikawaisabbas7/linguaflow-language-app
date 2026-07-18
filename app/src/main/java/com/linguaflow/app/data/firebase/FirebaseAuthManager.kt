package com.linguaflow.app.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

/**
 * Thin wrapper around FirebaseAuth exposing suspend functions
 * so ViewModels/Repositories can call it from coroutines.
 */
class FirebaseAuthManager(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    val currentUser: FirebaseUser?
        get() = auth.currentUser

    suspend fun register(email: String, password: String): Result<FirebaseUser> = runCatching {
        auth.createUserWithEmailAndPassword(email, password).await().user
            ?: throw IllegalStateException("Registration failed: no user returned")
    }

    suspend fun login(email: String, password: String): Result<FirebaseUser> = runCatching {
        auth.signInWithEmailAndPassword(email, password).await().user
            ?: throw IllegalStateException("Login failed: no user returned")
    }

    suspend fun sendPasswordReset(email: String): Result<Unit> = runCatching {
        auth.sendPasswordResetEmail(email).await()
    }

    suspend fun signInWithGoogleCredential(idToken: String): Result<FirebaseUser> = runCatching {
        val credential = com.google.firebase.auth.GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).await().user
            ?: throw IllegalStateException("Google sign-in failed: no user returned")
    }

    fun signOut() = auth.signOut()
}
