package com.example.marketcoinslist.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.marketcoinslist.coin_details.presentation.ui.CoinDetailsScreen
import com.example.marketcoinslist.coins.presentation.ui.CoinListScreen
import com.example.marketcoinslist.core.notification.LoadingInfo
import com.example.marketcoinslist.core.notification.NotificationInfo
import com.example.marketcoinslist.ui_element.CircleLoader
import com.example.marketcoinslist.ui_element.NotificationView
import com.example.marketcoinslist.utils.EMPTY_VALUE
import com.example.marketcoinslist.watch_list.presentation.ui.WatchListScreen
import marketcoinslist.shared.generated.resources.Res
import marketcoinslist.shared.generated.resources.ic_close
import org.jetbrains.compose.resources.painterResource

@Composable
fun CoinNavigation() {
    val coinRememberNavigator = rememberCoinNavigator()

    val notificationInfo = rememberSaveable(stateSaver = NotificationInfo.Saver) { mutableStateOf(null) }
    var loadingInfo by rememberSaveable(stateSaver = LoadingInfo.Saver) { mutableStateOf(null) }

    val currentDestination = coinRememberNavigator.lastOrNull()
    val showBottomBar = currentDestination is Coins || currentDestination is WatchList

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentDestination is Coins,
                        onClick = {
                            if (currentDestination !is Coins) {
                                coinRememberNavigator.clear()
                                coinRememberNavigator.add(Coins)
                            }
                        },
                        icon = { Text("🪙") },
                        label = { Text("Coins") }
                    )
                    NavigationBarItem(
                        selected = currentDestination is WatchList,
                        onClick = {
                            if (currentDestination !is WatchList) {
                                coinRememberNavigator.clear()
                                coinRememberNavigator.add(WatchList)
                            }
                        },
                        icon = { Text("★") },
                        label = { Text("Watch List") }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavDisplay(
            backStack = coinRememberNavigator,
            modifier = Modifier
                .padding(bottom = 88.dp)
                .background(color = Color.White),
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<Coins> {
                    CoinListScreen(
                        onNavigateToDetails = { id ->
                            coinRememberNavigator.add(CoinDetails(id))
                        },
                        onShowNotification = {
                            notificationInfo.value = it
                        },
                        onShowLoadingAnimation = {
                            loadingInfo = it
                        }
                    )
                }
                entry<CoinDetails> { details ->
                    CoinDetailsScreen(
                        coinId = details.id,
                        onBackClick = {
                            coinRememberNavigator.removeLastOrNull()
                        },
                        onShowNotification = {
                            notificationInfo.value = it
                        },
                        onShowLoadingAnimation = {
                            loadingInfo = it
                        }
                    )
                }
                entry<WatchList> {
                    WatchListScreen(
                        onNavigateToDetails = { id ->
                            coinRememberNavigator.add(CoinDetails(id))
                        },
                        onShowNotification = {
                            notificationInfo.value = it
                        },
                        onShowLoadingAnimation = {
                            loadingInfo = it
                        }
                    )
                }
            }
        )
    }

    notificationInfo.value?.let { info ->
        val isError = info.isError ?: true
        NotificationView(
            message = info.message,
            isError = isError,
            errorColor = Color(0xFFFDEDED),
            successfulColor = Color(0xFFEDF7ED),
            messageColor = if (isError) Color(0xFF5F2120) else Color(0xFF1E4620),
            clipCard = RoundedCornerShape(12.dp),
            iconPainter = painterResource(Res.drawable.ic_close),
            iconContentDescription = EMPTY_VALUE,
            messageStyle = MaterialTheme.typography.bodyMedium,
            onDismissRequest = {
                notificationInfo.value = null
            }
        )
    }

    CircleLoader(loadingInfo = loadingInfo)
}