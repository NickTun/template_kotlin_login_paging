package com.pedro.test_1.ui.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.test_1.domain.usecase.CheckSessionUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel(private val checkSessionUseCase: CheckSessionUseCase) : ViewModel() {
    private val _state = MutableStateFlow<SplashState>(SplashState.Loading)
    val state: StateFlow<SplashState> = _state

    private val _actions = MutableSharedFlow<SplashUiAction>()
    val actions: SharedFlow<SplashUiAction> = _actions

    fun onIntent(intent: SplashIntent) {
        when (intent) {
            is SplashIntent.CheckSession -> checkSession()
        }
    }

    private fun checkSession() {
        viewModelScope.launch {
            val logged = checkSessionUseCase()
            if (logged) {
                _state.value = SplashState.Authorized
                _actions.emit(SplashUiAction.ToMain)
            } else {
                _state.value = SplashState.NoSession
                _actions.emit(SplashUiAction.ToAuth)
            }
        }
    }
}
