package edu.mirea.onebeattrue.mylittlepet.ui.customview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import edu.mirea.onebeattrue.mylittlepet.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <I> CustomPullToRefreshLazyColumn(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    items: List<I>,
    key: (item: I) -> Any,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    isError: Boolean = false,
    errorText: String = stringResource(R.string.something_went_wrong),
    isEmpty: Boolean = false,
    emptyContent: @Composable (LazyItemScope.() -> Unit) = {},
    itemContent: @Composable (LazyItemScope.(item: I) -> Unit)
) {
    val pullToRefreshState = rememberPullToRefreshState()
    Box(
        modifier = modifier
            .fillMaxWidth()
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                vertical = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            state = lazyListState
        ) {
            if (isError) {
                item {
                    ErrorCardWithRetryButton(
                        message = errorText
                    ) {
                        onRefresh()
                    }
                }
            }

            if (isEmpty) {
                item {
                    emptyContent()
                }
            }

            items(
                items = items,
                key = key
            ) {
                itemContent(it)
            }
        }

        if (pullToRefreshState.isRefreshing) {
            LaunchedEffect(true) {
                onRefresh()
            }
        }

        LaunchedEffect(isRefreshing) {
            if (isRefreshing) {
                pullToRefreshState.startRefresh()
            } else {
                pullToRefreshState.endRefresh()
            }
        }

        PullToRefreshContainer(
            state = pullToRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}