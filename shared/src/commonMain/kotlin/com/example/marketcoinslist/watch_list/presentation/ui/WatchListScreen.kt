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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.marketcoinslist.coins.presentation.ui.CoinItem
import com.example.marketcoinslist.core.notification.LoadingInfo
import com.example.marketcoinslist.core.notification.NotificationInfo
import com.example.marketcoinslist.utils.isNotNull
import com.example.marketcoinslist.watch_list.presentation.WatchListAction
import com.example.marketcoinslist.watch_list.presentation.WatchListEvent
import com.example.marketcoinslist.watch_list.presentation.WatchListViewModel
import marketcoinslist.shared.generated.resources.Res
import marketcoinslist.shared.generated.resources.watch_list_empty_state_message
import marketcoinslist.shared.generated.resources.watch_list_title
import marketcoinslist.shared.generated.resources.watch_list_empty_state_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WatchListScreen(
    viewModel: WatchListViewModel = koinViewModel(),
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

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(
                text = stringResource(Res.string.watch_list_title)
            ) })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.coins.isEmpty() && !isLoading) {
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