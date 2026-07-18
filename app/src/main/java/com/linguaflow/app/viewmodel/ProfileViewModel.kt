package com.linguaflow.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.linguaflow.app.data.database.entity.UserEntity
import com.linguaflow.app.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileViewModel(private val container: AppContainer) : ViewModel() {

    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user

    private val _darkMode = MutableStateFlow(false)
    val darkMode: StateFlow<Boolean> = _darkMode

    init {
        val uid = container.authRepository.currentUser?.uid
        if (uid != null) {
            viewModelScope.launch {
                container.authRepository.observeLocalUser(uid).collectLatest { _user.value = it }
            }
        }
        viewModelScope.launch {
            container.preferencesManager.darkModeFlow.collectLatest { _darkMode.value = it }
        }
    }

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch { container.preferencesManager.setDarkMode(enabled) }
    }

    fun logout() {
        container.authRepository.logout()
    }

    companion object {
        fun factory(container: AppContainer) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = ProfileViewModel(container) as T
        }
    }
}
