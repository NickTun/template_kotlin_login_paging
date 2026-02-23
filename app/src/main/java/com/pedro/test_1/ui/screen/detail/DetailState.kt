package com.pedro.test_1.ui.screen.detail

import com.pedro.test_1.domain.entities.ListItem

sealed interface DetailState {
    object Loading : DetailState
    data class Data(val item: ListItem) : DetailState
    data class Error(val message: String) : DetailState
}
