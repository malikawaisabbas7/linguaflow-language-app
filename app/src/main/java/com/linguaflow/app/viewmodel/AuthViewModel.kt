package com.linguaflow.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.linguaflow.app.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

/**
 * Backs Login, Register, and Forgot Password screens.
 * Wraps AuthRepository so the UI never talks to Firebase/Room directly.
 */
class AuthViewModel(private val container: AppContainer) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    fun login(email: String, password: String) {
        _uiState.value = AuthUiState(isLoading = true)
        viewModelScope.launch {
            val result = container.authRepository.login(email, password)
            _uiState.value = if (result.isSuccess) {
                AuthUiState(isSuccess = true)
            } else {
                AuthUiState(errorMessage = result.exceptionOrNull()?.message ?: "Login failed")
            }
        }
    }

    fun register(
        name: String,
        email: String,
        password: String,
        nativeLanguage: String,
        targetLanguage: String,
        proficiencyLevel: String
    ) {
        _uiState.value = AuthUiState(isLoading = true)
        viewModelScope.launch {
            val result = container.authRepository.register(name, email, password, nativeLanguage, targetLanguage, proficiencyLevel)
            _uiState.value = if (result.isSuccess) {
                AuthUiState(isSuccess = true)
            } else {
                AuthUiState(errorMessage = result.exceptionOrNull()?.message ?: "Registration failed")
            }
        }
    }

    fun sendPasswordReset(email: String) {
        _uiState.value = AuthUiState(isLoading = true)
        viewModelScope.launch {
            val result = container.authRepository.sendPasswordReset(email)
            _uiState.value = if (result.isSuccess) {
                AuthUiState(isSuccess = true)
            } else {
                AuthUiState(errorMessage = result.exceptionOrNull()?.message ?: "Could not send reset email")
            }
        }
    }

    companion object {
        fun factory(container: AppContainer) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = AuthViewModel(container) as T
        }
    }
}
