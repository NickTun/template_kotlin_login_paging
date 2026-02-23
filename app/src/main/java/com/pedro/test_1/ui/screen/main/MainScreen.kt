package com.pedro.test_1.ui.screen.main

import android.graphics.drawable.shapes.Shape
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.pedro.test_1.R
import com.pedro.test_1.domain.entities.ListItem
import kotlinx.coroutines.flow.Flow

@Composable
fun MainScreen(
    state: MainState,
    onIntent: (MainIntent) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (state) {
            is MainState.Loading -> {
                CircularProgressIndicator()
            }
            is MainState.Data -> {
                MessageList(pager = state.pager, onIntent = onIntent)
            }
            is MainState.Error -> {
                Text("Error: ${state.message}")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageList(
    pager: Flow<PagingData<ListItem>>,
    onIntent: (MainIntent) -> Unit
) {
    val lazyPagingItems = pager.collectAsLazyPagingItems()
    var isRefreshing by remember { mutableStateOf(false) }
    val lazyListState = rememberLazyListState()
    var isTopExtended by remember { mutableStateOf(true) }
    val nestedScrollConnection = object : NestedScrollConnection {
        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource
        ): Offset {
            if(available.y == 0f && !lazyListState.canScrollBackward) {
                if (!isTopExtended) isTopExtended = true
            } else if(isTopExtended) isTopExtended = false
            return Offset.Zero
        }
    }
    val animatedCardHeight by animateDpAsState(
        if (isTopExtended) 200.dp else 68.dp
    )

    LaunchedEffect(pager) {
        isRefreshing = false
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            onIntent(MainIntent.Refresh)
        },
        Modifier.nestedScroll(nestedScrollConnection),

    ) {
        LazyColumn (
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = lazyListState
        ) {
            stickyHeader {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .dropShadow(
                            shape = RoundedCornerShape(12.dp),
                            shadow = Shadow(
                                radius = 12.dp,

                                )
                        )
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        AnimatedVisibility(isTopExtended) {
                            Image(
                                painter = painterResource(R.drawable.ic_launcher_background),
                                contentDescription = "Icon",
                                modifier = Modifier
                                    .aspectRatio(1f/1f)
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(12.dp))
                                    .padding(8.dp)
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.padding(16.dp).fillMaxWidth()
                        ) {
                            Row (
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AnimatedVisibility(!isTopExtended) {
                                    Image(
                                        painter = painterResource(R.drawable.ic_launcher_background),
                                        contentDescription = "Icon",
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape)
                                    )
                                }
                                Text("admin_allah")
                            }

                            Row (horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                ElevatedButton(
                                    onClick = { onIntent(MainIntent.Logout) }
                                ) { Text("Logout") }
//                            ElevatedButton(
//                                onClick = { onIntent(MainIntent.Refresh) }
//                            ) { Text("Refresh") }
                            }

                        }
                    }

                }
            }
            items(
                lazyPagingItems.itemCount,
                key = lazyPagingItems.itemKey { it.id }
            ) { index ->
                val message = lazyPagingItems[index]
                if (message != null) {
                    MessageRow(message = message)
                } else {
                    MessagePlaceholder()
                }
            }
        }
    }
}

@Composable
fun MessagePlaceholder() {
    Box(
        Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun MessageRow(
    message: ListItem
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(message.title)
            Text(message.subtitle)
            Text(message.details)
        }
    }
}