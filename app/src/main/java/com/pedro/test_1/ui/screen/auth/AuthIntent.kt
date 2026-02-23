package com.pedro.test_1.ui.screen.auth

sealed interface AuthIntent {
    data class UpdateInput(val text: String) : AuthIntent
    data class Submit(val username: String, val password: String) : AuthIntent
}
