package com.pedro.test_1.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.test_1.data.repo.Repository
import com.pedro.test_1.domain.usecase.FetchListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class   DetailViewModel(private val repository: Repository, private val fetchListUseCase: FetchListUseCase) : ViewModel() {
    private val _state = MutableStateFlow<DetailState>(DetailState.Loading)
    val state: StateFlow<DetailState> = _state

    fun onIntent(intent: DetailIntent) {
        when (intent) {
            is DetailIntent.Load -> load(intent.itemId)
            is DetailIntent.Retry -> retry()
        }
    }

    private fun retry() {
        // no-op in scaffold - UI should call Load again
    }

    private fun load(id: String) {
        viewModelScope.launch {
            _state.value = DetailState.Loading
            try {
                val item = repository.getItemById(id)
                if (item != null) _state.value = DetailState.Data(item)
                else _state.value = DetailState.Error("Item not found")
            } catch (t: Throwable) {
                _state.value = DetailState.Error(t.message ?: "Unknown error")
            }
        }
    }
}
