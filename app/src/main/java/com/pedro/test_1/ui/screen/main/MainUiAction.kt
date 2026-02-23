package com.pedro.test_1.ui.screen.main

sealed interface MainUiAction {
    data class ToDetail(val itemId: String) : MainUiAction
    object ToAuth: MainUiAction
}
