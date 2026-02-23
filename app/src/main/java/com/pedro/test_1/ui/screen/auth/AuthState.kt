package com.pedro.test_1.ui.screen.auth

import com.pedro.test_1.domain.entities.User

sealed interface AuthState {
    object Idle : AuthState
    object Loading : AuthState
    data class Error(val message: String) : AuthState
    data class Data(val user: User) : AuthState
}
