package com.example.marketcoinslist.feature_coins.presentation.ui

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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.marketcoinslist.feature_coins.domain.model.Coin
import com.example.marketcoinslist.feature_coins.presentation.CoinsAction
import com.example.marketcoinslist.feature_coins.presentation.CoinsViewModel
import org.jetbrains.compose.resources.painterResource
import marketcoinslist.shared.generated.resources.Res
import marketcoinslist.shared.generated.resources.compose_multiplatform
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinsScreen(
    viewModel: CoinsViewModel = koinInject()
) {
    val state by viewModel.observeUiState().collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Market Coins") })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            if (state.coins.isEmpty() && state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.error != null && state.coins.isEmpty()) {
                ErrorView(
                    error = state.error!!,
                    onRetry = { viewModel.handle(CoinsAction.RefreshCoins) },
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.coins) { coin ->
                        CoinItem(
                            coin = coin,
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
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { /* Navigate to Details */ }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Image placeholder, normally you'd use coil3 here for actual KMP image loading
        Icon(
            painter = painterResource(Res.drawable.compose_multiplatform),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = coin.name, fontWeight = FontWeight.Bold)
            Text(text = coin.symbol, color = Color.Gray)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(text = "$${coin.currentPrice}")
            val change = coin.priceChangePercentage24h ?: 0.0
            val color = if (change >= 0) Color.Green else Color.Red
            Text(text = "${if (change >= 0) "+" else ""}${change}%", color = color)
        }
        IconButton(onClick = onFavoriteClick) {
            Text(if (coin.isFavorite) "★" else "☆", color = if (coin.isFavorite) Color.Yellow else Color.Gray)
        }
    }
}

@Composable
fun ErrorView(error: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Text(text = error, color = Color.Red, modifier = Modifier.padding(16.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}