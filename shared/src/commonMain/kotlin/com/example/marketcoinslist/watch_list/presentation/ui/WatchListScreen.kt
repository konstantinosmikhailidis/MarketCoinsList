package com.example.marketcoinslist.watch_list.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.marketcoinslist.coins.presentation.ui.CoinItem
import com.example.marketcoinslist.core.notification.LoadingInfo
import com.example.marketcoinslist.core.notification.NotificationInfo
import com.example.marketcoinslist.watch_list.presentation.WatchListAction
import com.example.marketcoinslist.watch_list.presentation.WatchListEvent
import com.example.marketcoinslist.watch_list.presentation.WatchListViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WatchListScreen(
    viewModel: WatchListViewModel = koinViewModel(),
    onNavigateToDetails: (String) -> Unit = {},
    onShowNotification: (NotificationInfo) -> Unit,
    onShowLoadingAnimation: (LoadingInfo?) -> Unit,
) {
    val state by viewModel.observeUiState().collectAsState()

    LaunchedEffect(Unit) {
        viewModel.observeEvent().collect { event ->
            when (event) {
                is WatchListEvent.ShowLoadingAnimation -> {
                    onShowLoadingAnimation(event.loadingInfo)
                }

                is WatchListEvent.ShowNotification -> onShowNotification(event.notificationInfo)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Watch List") })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.coins.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "★",
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Your Watch List is empty",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        text = "Tap the star icon on any coin to add it here",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.coins) { coin ->
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