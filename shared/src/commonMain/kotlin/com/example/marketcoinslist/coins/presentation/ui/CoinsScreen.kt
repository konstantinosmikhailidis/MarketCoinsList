package com.example.marketcoinslist.coins.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.marketcoinslist.coins.domain.model.Coin
import com.example.marketcoinslist.coins.presentation.CoinsAction
import com.example.marketcoinslist.coins.presentation.CoinsEvent
import com.example.marketcoinslist.coins.presentation.CoinsViewModel
import com.example.marketcoinslist.core.notification.LoadingInfo
import com.example.marketcoinslist.core.notification.NotificationInfo
import com.example.marketcoinslist.utils.EMPTY_VALUE
import io.ktor.websocket.Frame
import marketcoinslist.shared.generated.resources.Res
import marketcoinslist.shared.generated.resources.market_coins_title
import marketcoinslist.shared.generated.resources.no_chart_data_available
import marketcoinslist.shared.generated.resources.retry
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CoinsScreen(
    viewModel: CoinsViewModel = koinViewModel(),
    onNavigateToDetails: (String) -> Unit = {},
    onShowNotification: (NotificationInfo) -> Unit,
    onShowLoadingAnimation: (LoadingInfo?) -> Unit,
) {
    val state by viewModel.observeUiState().collectAsState()
    var isLoading by rememberSaveable { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        viewModel.observeEvent().collect { event ->
            when (event) {
                is CoinsEvent.ShowLoadingAnimation -> {
                    onShowLoadingAnimation(event.loadingInfo)
                }

                is CoinsEvent.ShowNotification -> onShowNotification(event.notificationInfo)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text( text = stringResource(Res.string.market_coins_title)) })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            if (!isLoading && state.coins.isEmpty()) {
                ErrorView(
                    error = stringResource(Res.string.no_chart_data_available),
                    onRetry = { viewModel.handle(CoinsAction.RefreshCoins) },
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.coins) { coin ->
                        CoinItem(
                            coin = coin,
                            onClick = { onNavigateToDetails(coin.id) },
                            onFavoriteClick = { viewModel.handle(CoinsAction.ToggleFavorite(coin.id)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CoinItem(
    coin: Coin,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = coin.imageUrl,
            contentDescription = coin.name,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = coin.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = coin.symbol.uppercase(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "$${coin.currentPrice}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            val change = coin.priceChangePercentage24h ?: 0.0
            val color = if (change >= 0) Color(0xFF4CAF50) else Color(0xFFF44336)
            val sign = if (change >= 0) "+" else EMPTY_VALUE
            Text(
                text = "$sign${change}%",
                style = MaterialTheme.typography.bodyMedium,
                color = color
            )
        }
        
        IconButton(onClick = onFavoriteClick) {
            Text(
                text = if (coin.isFavorite)
                    "★"
                else
                    "☆",
                color = if (coin.isFavorite)
                    Color(0xFFFFC107)
                else
                    MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@Composable
fun ErrorView(error: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Text(text = error, color = Color.Red, modifier = Modifier.padding(16.dp))
        Button(onClick = onRetry) {
            Text(stringResource(Res.string.retry))
        }
    }
}