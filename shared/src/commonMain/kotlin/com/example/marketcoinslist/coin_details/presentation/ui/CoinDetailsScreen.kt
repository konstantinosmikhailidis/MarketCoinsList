package com.example.marketcoinslist.coin_details.presentation.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.marketcoinslist.coin_details.domain.model.PricePoint
import com.example.marketcoinslist.coin_details.presentation.CoinDetailsAction
import com.example.marketcoinslist.coin_details.presentation.CoinDetailsEvent
import com.example.marketcoinslist.coin_details.presentation.CoinDetailsViewModel
import com.example.marketcoinslist.coins.domain.model.Coin
import com.example.marketcoinslist.core.notification.LoadingInfo
import com.example.marketcoinslist.core.notification.NotificationInfo
import com.example.marketcoinslist.utils.EMPTY_VALUE
import marketcoinslist.shared.generated.resources.Res
import marketcoinslist.shared.generated.resources.char_back
import marketcoinslist.shared.generated.resources.char_non_selected
import marketcoinslist.shared.generated.resources.char_plus
import marketcoinslist.shared.generated.resources.char_selected
import marketcoinslist.shared.generated.resources.coin_details_title
import marketcoinslist.shared.generated.resources.no_chart_data_available
import marketcoinslist.shared.generated.resources.retry
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CoinDetailsScreen(
    coinId: String,
    viewModel: CoinDetailsViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
    onShowNotification: (NotificationInfo) -> Unit,
    onShowLoadingAnimation: (LoadingInfo?) -> Unit,
) {
    val state by viewModel.observeUiState().collectAsState()
    LaunchedEffect(coinId) {
        viewModel.init(coinId)
    }

    LaunchedEffect(Unit) {
        viewModel.observeEvent().collect { event ->
            when (event) {
                is CoinDetailsEvent.ShowLoadingAnimation -> {
                    onShowLoadingAnimation(event.loadingInfo)
                }

                is CoinDetailsEvent.ShowNotification -> onShowNotification(event.notificationInfo)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.coin?.name ?: stringResource(Res.string.coin_details_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Text(
                            text = stringResource(Res.string.char_back),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    state.coin?.let { coin ->
                        IconButton(onClick = {
                            viewModel.handle(
                                CoinDetailsAction.ToggleFavorite(
                                    coin.id
                                )
                            )
                        }) {
                            Text(
                                text = if (coin.isFavorite)
                                    stringResource(Res.string.char_selected)
                                else
                                    stringResource(Res.string.char_non_selected),
                                color = if (coin.isFavorite)
                                    Color(0xFFFFC107)
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            state.coin?.let { coin ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp)
                ) {
                    CoinHeader(coin = coin)

                    Spacer(modifier = Modifier.height(24.dp))

                    ChartSection(
                        prices = state.marketChart?.prices ?: emptyList(),
                        selectedDays = state.selectedDays,
                        onTimeframeSelected = { days ->
                            viewModel.handle(CoinDetailsAction.SelectTimeframe(coinId, days))
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    CoinStatsSection(coin = coin)
                }
            }
        }
    }
}

@Composable
private fun CoinHeader(coin: Coin) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        AsyncImage(
            model = coin.imageUrl,
            contentDescription = coin.name,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = coin.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                coin.marketCapRank?.let { rank ->
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerHigh
                    ) {
                        Text(
                            text = "#$rank",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
            Text(
                text = coin.symbol.uppercase(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "$${coin.currentPrice}",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        val change = coin.priceChangePercentage24h ?: 0.0
        val isPositive = change >= 0
        val color = if (isPositive) Color(0xFF4CAF50) else Color(0xFFF44336)
        val backgroundColor = if (isPositive) Color(0xE8E8F5E9) else Color(0xFFFFEBEE)
        val sign = if (isPositive) stringResource(Res.string.char_plus) else EMPTY_VALUE

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = backgroundColor
        ) {
            Text(
                text = "$sign${change}%",
                style = MaterialTheme.typography.titleMedium,
                color = color,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
private fun ChartSection(
    prices: List<PricePoint>,
    selectedDays: String,
    onTimeframeSelected: (String) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Price Chart",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val timeframes = listOf("1" to "24H", "7" to "7D", "30" to "30D", "365" to "1Y")
                timeframes.forEach { (days, label) ->
                    FilterChip(
                        selected = selectedDays == days,
                        onClick = { onTimeframeSelected(days) },
                        label = { Text(label) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (prices.isNotEmpty()) {
                val firstPrice = prices.first().price
                val lastPrice = prices.last().price
                val isPositiveTrend = lastPrice >= firstPrice
                val chartColor = if (isPositiveTrend) Color(0xFF4CAF50) else Color(0xFFF44336)

                PriceLineChart(
                    prices = prices,
                    color = chartColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(Res.string.no_chart_data_available),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { onTimeframeSelected(selectedDays) }) {
                        Text(stringResource(Res.string.retry))
                    }
                }
            }
        }
    }
}

@Composable
private fun PriceLineChart(
    prices: List<PricePoint>,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val minPrice = remember(prices) { prices.minOf { it.price } }
    val maxPrice = remember(prices) { prices.maxOf { it.price } }
    val priceRange = if (maxPrice == minPrice) 1.0 else maxPrice - minPrice

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val verticalPadding = 16.dp.toPx()

        val points = prices.mapIndexed { index, point ->
            val x = (index.toFloat() / (prices.size - 1).coerceAtLeast(1)) * width
            val y =
                height - (((point.price - minPrice) / priceRange).toFloat() * (height - verticalPadding * 2) + verticalPadding)
            Offset(x, y)
        }

        if (points.size > 1) {
            val strokePath = Path().apply {
                moveTo(points.first().x, points.first().y)
                for (i in 1 until points.size) {
                    val p1 = points[i - 1]
                    val p2 = points[i]
                    val controlPoint1 = Offset(p1.x + (p2.x - p1.x) / 2f, p1.y)
                    val controlPoint2 = Offset(p1.x + (p2.x - p1.x) / 2f, p2.y)
                    cubicTo(
                        controlPoint1.x,
                        controlPoint1.y,
                        controlPoint2.x,
                        controlPoint2.y,
                        p2.x,
                        p2.y
                    )
                }
            }

            val fillPath = Path().apply {
                addPath(strokePath)
                lineTo(width, height)
                lineTo(0f, height)
                close()
            }

            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(color.copy(alpha = 0.3f), Color.Transparent),
                    startY = 0f,
                    endY = height
                )
            )

            drawPath(
                path = strokePath,
                color = color,
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
            )
        }
    }
}

@Composable
private fun CoinStatsSection(coin: Coin) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Market Stats",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            StatRow(label = "Market Cap Rank", value = coin.marketCapRank?.let { "#$it" } ?: "-")
            StatRow(label = "Current Price", value = "$${coin.currentPrice}")
            StatRow(label = "24h Price Change", value = "${coin.priceChangePercentage24h ?: 0.0}%")
            StatRow(
                label = "Watchlist Status",
                value = if (coin.isFavorite) "In Watchlist" else "Not in Watchlist"
            )
        }
    }
}

@Composable
private fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}