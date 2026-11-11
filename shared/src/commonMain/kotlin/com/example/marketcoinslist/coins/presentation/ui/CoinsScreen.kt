package com.example.marketcoinslist.coins.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.marketcoinslist.coins.domain.model.Coin
import com.example.marketcoinslist.coins.presentation.CoinsAction
import com.example.marketcoinslist.coins.presentation.CoinsEvent
import com.example.marketcoinslist.coins.presentation.CoinsViewModel
import com.example.marketcoinslist.coins.presentation.FilterType
import com.example.marketcoinslist.core.notification.LoadingInfo
import com.example.marketcoinslist.core.notification.NotificationInfo
import com.example.marketcoinslist.theme.LossRed
import com.example.marketcoinslist.theme.ProfitGreen
import com.example.marketcoinslist.theme.StarActive
import com.example.marketcoinslist.theme.StarInactive
import com.example.marketcoinslist.ui_element.VerticalSpacer
import com.example.marketcoinslist.utils.EMPTY_VALUE
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import marketcoinslist.shared.generated.resources.Res
import marketcoinslist.shared.generated.resources.all
import marketcoinslist.shared.generated.resources.gainers
import marketcoinslist.shared.generated.resources.losers
import marketcoinslist.shared.generated.resources.market_coins_title
import marketcoinslist.shared.generated.resources.no_chart_data_available
import marketcoinslist.shared.generated.resources.retry
import marketcoinslist.shared.generated.resources.top_fifty
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CoinsScreen(
    viewModel: CoinsViewModel = koinViewModel(),
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
                is CoinsEvent.ShowLoadingAnimation -> {
                    onShowLoadingAnimation(event.loadingInfo)
                    isLoading = event.loadingInfo != null
                }

                is CoinsEvent.ShowNotification -> onShowNotification(event.notificationInfo)
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
        containerColor = colorScheme.primary,
        topBar = {
            Column {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorScheme.primary
                    ),
                    title = {
                        Text(
                            text = stringResource(Res.string.market_coins_title),
                            color = colorScheme.onPrimary
                        )
                    }
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .hazeSource(hazeState),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        val isSelectedAll = state.filterType == FilterType.ALL
                        FilterChip(
                            selected = isSelectedAll,
                            onClick = { viewModel.handle(CoinsAction.SetFilter(FilterType.ALL)) },
                            shape = RoundedCornerShape(percent = 50),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelectedAll,
                                borderColor = colorScheme.outline,
                                selectedBorderColor = colorScheme.onSurfaceVariant,
                                selectedBorderWidth = 2.dp,
                                borderWidth = 1.dp
                            ),
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = colorScheme.surface,
                                selectedContainerColor = colorScheme.surfaceVariant,
                                labelColor = colorScheme.onSurface,
                                selectedLabelColor = colorScheme.onSurfaceVariant
                            ),
                            label = {
                                Text(
                                    text = stringResource(Res.string.all),
                                    style = typography.labelMedium
                                )
                            }
                        )
                    }
                    item {
                        val isSelectedTop50 = state.filterType == FilterType.TOP_50
                        FilterChip(
                            selected = isSelectedTop50,
                            onClick = { viewModel.handle(CoinsAction.SetFilter(FilterType.TOP_50)) },
                            shape = RoundedCornerShape(percent = 50),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelectedTop50,
                                borderColor = colorScheme.outline,
                                selectedBorderColor = colorScheme.onSurfaceVariant,
                                selectedBorderWidth = 2.dp,
                                borderWidth = 1.dp
                            ),
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = colorScheme.surface,
                                selectedContainerColor = colorScheme.surfaceVariant,
                                labelColor = colorScheme.onSurface,
                                selectedLabelColor = colorScheme.onSurfaceVariant
                            ),
                            label = {
                                Text(
                                    text = stringResource(Res.string.top_fifty),
                                    style = typography.labelMedium
                                )
                            }
                        )
                    }
                    item {
                        val isSelectedGainers = state.filterType == FilterType.GAINERS
                        FilterChip(
                            selected = isSelectedGainers,
                            onClick = { viewModel.handle(CoinsAction.SetFilter(FilterType.GAINERS)) },
                            shape = RoundedCornerShape(percent = 50),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelectedGainers,
                                borderColor = colorScheme.outline,
                                selectedBorderColor = colorScheme.onSurfaceVariant,
                                selectedBorderWidth = 2.dp,
                                borderWidth = 1.dp
                            ),
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = colorScheme.surface,
                                selectedContainerColor = colorScheme.surfaceVariant,
                                labelColor = colorScheme.onSurface,
                                selectedLabelColor = colorScheme.onSurfaceVariant
                            ),
                            label = {
                                Text(
                                    text = stringResource(Res.string.gainers),
                                    style = typography.labelMedium
                                )
                            }
                        )
                    }
                    item {
                        val isSelectedLosers = state.filterType == FilterType.LOSERS
                        FilterChip(
                            selected = isSelectedLosers,
                            onClick = { viewModel.handle(CoinsAction.SetFilter(FilterType.LOSERS)) },
                            shape = RoundedCornerShape(percent = 50),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelectedLosers,
                                borderColor = colorScheme.outline,
                                selectedBorderColor = colorScheme.onSurfaceVariant,
                                selectedBorderWidth = 2.dp,
                                borderWidth = 1.dp
                            ),
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = colorScheme.surface,
                                selectedContainerColor = colorScheme.surfaceVariant,
                                labelColor = colorScheme.onSurface,
                                selectedLabelColor = colorScheme.onSurfaceVariant
                            ),
                            label = {
                                Text(
                                    text = stringResource(Res.string.losers),
                                    style = typography.labelMedium
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(hazeState) // Размываем весь экран
        ) {
            if (!isLoading && state.coins.isEmpty()) {
                ErrorView(
                    error = stringResource(Res.string.no_chart_data_available),
                    onRetry = { viewModel.handle(CoinsAction.RefreshCoins) },
                    modifier = Modifier.align(Alignment.Center)
                )
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
                            onFavoriteClick = { viewModel.handle(CoinsAction.ToggleFavorite(coin.id)) }
                        )
                        VerticalSpacer(8.dp)
                    }
                    item {
                        VerticalSpacer(96.dp)
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
    modifier: Modifier = Modifier,
) {

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surface.copy(alpha = 0.8f)
        ),
        border = BorderStroke(
            width = 2.dp,
            color = colorScheme.outline
        )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 12.dp, vertical = 12.dp),
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
                    style = typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onPrimary
                )
                Text(
                    text = coin.symbol.uppercase(),
                    style = typography.bodyMedium,
                    color = colorScheme.onSurface
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$${coin.currentPrice}",
                    style = typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                val change = coin.priceChangePercentage24h ?: 0.0
                val color = if (change >= 0) ProfitGreen else LossRed
                val sign = if (change >= 0) "+" else EMPTY_VALUE
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = color.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "$sign${change}%",
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                        style = typography.bodyMedium,
                        color = color
                    )
                }
            }

            IconButton(onClick = onFavoriteClick) {
                Text(
                    text = if (coin.isFavorite) "★" else "☆",
                    color = if (coin.isFavorite) StarActive else StarInactive,
                    style = typography.headlineSmall
                )
            }
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