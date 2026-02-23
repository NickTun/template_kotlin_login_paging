package com.pedro.test_1.ui.screen.main

import androidx.paging.PagingData
import com.pedro.test_1.domain.entities.ListItem
import com.pedro.test_1.ui.nav.Main
import kotlinx.coroutines.flow.Flow

sealed interface MainState {
    object Loading : MainState
    data class Data(val pager: Flow<PagingData<ListItem>>): MainState

    data class Error(val message: String) : MainState
}
