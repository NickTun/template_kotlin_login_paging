package com.pedro.test_1.ui.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.test_1.core.Constants
import com.pedro.test_1.domain.entities.AuthResult
import com.pedro.test_1.domain.usecase.AuthUseCase
import com.pedro.test_1.domain.usecase.SaveSessionUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authUseCase: AuthUseCase, private val saveSessionUseCase: SaveSessionUseCase) : ViewModel() {
    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state: StateFlow<AuthState> = _state

    private val _actions = MutableSharedFlow<AuthUiAction>()
    val actions: SharedFlow<AuthUiAction> = _actions

    private var currentInput: String = ""

    fun onIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.UpdateInput -> {
                currentInput = if (intent.text.length > Constants.MAX_INPUT_LENGTH)
                    intent.text.substring(0, Constants.MAX_INPUT_LENGTH)
                else intent.text
                // keep UI-driven state minimal; UI should show the trimmed input
            }
            is AuthIntent.Submit -> submit(intent.username, intent.password)
        }
    }

    private fun submit(username: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            val result = authUseCase(username.trim(), password)
            when (result) {
                is AuthResult.Success -> {
                    _state.value = AuthState.Data(result.user)
                    // persist session via domain usecase
                    saveSessionUseCase(result.user)
                    _actions.emit(AuthUiAction.ToMain)
                }
                is AuthResult.Failure -> {
                    _state.value = AuthState.Error(result.message)
                }
            }
        }
    }
}
