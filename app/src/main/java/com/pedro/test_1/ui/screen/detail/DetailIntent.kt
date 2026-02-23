package com.pedro.test_1.ui.screen.detail

sealed interface DetailIntent {
    data class Load(val itemId: String) : DetailIntent
    object Retry : DetailIntent
}
