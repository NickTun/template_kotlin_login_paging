package com.pedro.test_1.ui.screen.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun DetailScreen(
    state: DetailState,
    onIntent: (DetailIntent) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (state) {
            is DetailState.Loading -> {
                CircularProgressIndicator()
            }
            is DetailState.Data -> {
                Text("Detail: ${state.item.title} — implement UI here")
            }
            is DetailState.Error -> {
                Text("Error: ${state.message}")
            }
        }
    }
}
