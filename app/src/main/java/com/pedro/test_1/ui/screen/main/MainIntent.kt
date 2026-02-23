package com.pedro.test_1.ui.screen.main

sealed interface MainIntent {
    object Refresh : MainIntent
    object Logout : MainIntent
}
