package com.pedro.test_1.ui.screen.splash

sealed interface SplashState {
    object Loading : SplashState
    object NoSession : SplashState
    object Authorized : SplashState
}
