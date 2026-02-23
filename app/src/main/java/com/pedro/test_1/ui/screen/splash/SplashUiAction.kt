package com.pedro.test_1.ui.screen.splash

sealed interface SplashUiAction {
    object ToAuth : SplashUiAction
    object ToMain : SplashUiAction
}
