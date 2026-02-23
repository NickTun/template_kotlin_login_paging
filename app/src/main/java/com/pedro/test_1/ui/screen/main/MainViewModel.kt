package com.pedro.test_1.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pedro.test_1.core.Constants
import com.pedro.test_1.data.ListPagerSource
import com.pedro.test_1.data.repo.FakeRepository
import com.pedro.test_1.data.repo.Repository
import com.pedro.test_1.domain.entities.ListItem
import com.pedro.test_1.domain.usecase.FetchListUseCase
import com.pedro.test_1.domain.usecase.LogoutUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val fetchListUseCase: FetchListUseCase, private val logoutUseCase: LogoutUseCase, val repository: FakeRepository) : ViewModel() {
    private val _state = MutableStateFlow<MainState>(MainState.Loading)
    val state: StateFlow<MainState> = _state

    private val _actions = MutableSharedFlow<MainUiAction>()
    val actions: SharedFlow<MainUiAction> = _actions

    init {
        loadPage()
    }

    fun onIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.Refresh -> loadPage()
            is MainIntent.Logout -> logout()
        }
    }

    private fun loadPage() {
        viewModelScope.launch {
            val current = _state.value
            _state.value = MainState.Loading
            try {
                val flow = Pager(
                    PagingConfig(pageSize = Constants.PAGE_SIZE)
                ) {
                    ListPagerSource(repository, Constants.PAGE_SIZE)
                }.flow.cachedIn(viewModelScope)
                _state.value = MainState.Data(flow)
            } catch (t: Throwable) {
                _state.value = MainState.Error(t.message ?: "Unknown error")
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            logoutUseCase.invoke()
            _actions.emit(MainUiAction.ToAuth)
        }
    }
}
