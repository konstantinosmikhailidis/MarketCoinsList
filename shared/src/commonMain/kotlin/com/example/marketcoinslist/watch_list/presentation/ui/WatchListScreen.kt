package com.example.marketcoinslist.watch_list.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.example.marketcoinslist.coins.presentation.FilterType
import com.example.marketcoinslist.coins.presentation.ui.CoinItem
import com.example.marketcoinslist.core.notification.LoadingInfo
import com.example.marketcoinslist.core.notification.NotificationInfo
import com.example.marketcoinslist.utils.isNotNull
import com.example.marketcoinslist.watch_list.presentation.WatchListAction
import com.example.marketcoinslist.watch_list.presentation.WatchListEvent
import com.example.marketcoinslist.watch_list.presentation.WatchListViewModel
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import marketcoinslist.shared.generated.resources.Res
import marketcoinslist.shared.generated.resources.watch_list_empty_state_message
import marketcoinslist.shared.generated.resources.watch_list_empty_state_title
import marketcoinslist.shared.generated.resources.watch_list_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WatchListScreen(
    viewModel: WatchListViewModel = koinViewModel(),
    hazeState: HazeState,
    onNavigateToDetails: (String) -> Unit = {},
    onShowNotification: (NotificationInfo) -> Unit,
    onShowLoadingAnimation: (LoadingInfo?) -> Unit,
) {
    val state by viewModel.observeUiState().collectAsState()
    var isLoading by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.observeEvent().collect { event ->
            when (event) {
                is WatchListEvent.ShowLoadingAnimation -> {
                    isLoading = event.loadingInfo.isNotNull()
                    onShowLoadingAnimation(event.loadingInfo)
                }
                is WatchListEvent.ShowNotification -> onShowNotification(event.notificationInfo)
            }
        }
    }

    val filteredCoins = when (state.filterType) {
        FilterType.ALL -> state.coins
        FilterType.TOP_50 -> state.coins.take(50)
        FilterType.GAINERS -> state.coins.filter { (it.priceChangePercentage24h ?: 0.0) >= 0 }
        FilterType.LOSERS -> state.coins.filter { (it.priceChangePercentage24h ?: 0.0) < 0 }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(title = { Text(text = stringResource(Res.string.watch_list_title)) })

                if (state.coins.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .hazeSource(hazeState),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            FilterChip(
                                selected = state.filterType == FilterType.ALL,
                                onClick = { viewModel.handle(WatchListAction.SetFilter(FilterType.ALL)) },
                                label = { Text("All") }
                            )
                        }
                        item {
                            FilterChip(
                                selected = state.filterType == FilterType.TOP_50,
                                onClick = { viewModel.handle(WatchListAction.SetFilter(FilterType.TOP_50)) },
                                label = { Text("Top 50") }
                            )
                        }
                        item {
                            FilterChip(
                                selected = state.filterType == FilterType.GAINERS,
                                onClick = { viewModel.handle(WatchListAction.SetFilter(FilterType.GAINERS)) },
                                label = { Text("Gainers") }
                            )
                        }
                        item {
                            FilterChip(
                                selected = state.filterType == FilterType.LOSERS,
                                onClick = { viewModel.handle(WatchListAction.SetFilter(FilterType.LOSERS)) },
                                label = { Text("Losers") }
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(hazeState)
        ) {
            if (state.coins.isEmpty() && !isLoading) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(paddingValues),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "★",
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = stringResource(Res.string.watch_list_empty_state_title),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        text = stringResource(Res.string.watch_list_empty_state_message),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().background(
                        Brush.verticalGradient(
                            colors = listOf(
                                colorScheme.primary,
                                colorScheme.surfaceVariant
                            )
                        )
                    ),
                    contentPadding = PaddingValues(
                        top = paddingValues.calculateTopPadding(),
                        bottom = paddingValues.calculateBottomPadding() + 16.dp
                    )
                ) {
                    items(filteredCoins) { coin ->
                        CoinItem(
                            coin = coin,
                            onClick = { onNavigateToDetails(coin.id) },
                            onFavoriteClick = { viewModel.handle(WatchListAction.RemoveFavorite(coin.id)) }
                        )
                    }
                }
            }
        }
    }
}