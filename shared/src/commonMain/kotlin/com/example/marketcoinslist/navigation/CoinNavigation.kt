package com.example.marketcoinslist.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.marketcoinslist.coin_details.presentation.ui.CoinDetailsScreen
import com.example.marketcoinslist.coins.presentation.ui.CoinsScreen
import com.example.marketcoinslist.core.notification.LoadingInfo
import com.example.marketcoinslist.core.notification.NotificationInfo
import com.example.marketcoinslist.theme.PrimaryNeon
import com.example.marketcoinslist.theme.StarActive
import com.example.marketcoinslist.ui_element.CircleLoader
import com.example.marketcoinslist.ui_element.GlassBox
import com.example.marketcoinslist.ui_element.NotificationView
import com.example.marketcoinslist.utils.EMPTY_VALUE
import com.example.marketcoinslist.watch_list.presentation.ui.WatchListScreen
import dev.chrisbanes.haze.HazeInput
import dev.chrisbanes.haze.blur.HazeBlurStyle
import dev.chrisbanes.haze.blur.HazeColorEffect
import dev.chrisbanes.haze.blur.hazeBlur
import dev.chrisbanes.haze.glass.ChromaticAberrationMode
import dev.chrisbanes.haze.glass.GlassOptics
import dev.chrisbanes.haze.glass.GlassStyle
import dev.chrisbanes.haze.glass.OpticalSizeValue
import dev.chrisbanes.haze.glass.SurfaceProfile
import dev.chrisbanes.haze.glass.hazeGlass
import dev.chrisbanes.haze.rememberHazeState
import marketcoinslist.shared.generated.resources.Res
import marketcoinslist.shared.generated.resources.ic_close
import marketcoinslist.shared.generated.resources.ic_coins
import marketcoinslist.shared.generated.resources.market_coins_title
import marketcoinslist.shared.generated.resources.watch_list_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CoinNavigation() {
    val coinRememberNavigator = rememberCoinNavigator()

    val notificationInfo =
        rememberSaveable(stateSaver = NotificationInfo.Saver) { mutableStateOf(null) }
    var loadingInfo by rememberSaveable(stateSaver = LoadingInfo.Saver) { mutableStateOf(null) }

    val currentDestination = coinRememberNavigator.lastOrNull()
    val showBottomBar = currentDestination is Coins || currentDestination is WatchList
    val hazeState = rememberHazeState()
    val surfaceVariant = colorScheme.surfaceVariant
    Scaffold(
        containerColor = colorScheme.primary, bottomBar = {
            if (showBottomBar) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 32.dp).hazeGlass(
                            input = HazeInput.Sources(hazeState),
                            style = GlassStyle.regular.then {
                                shape(RoundedCornerShape(20.dp))

                                // 1. Увеличиваем радиус размытия
                                optics(
                                    GlassOptics.regular.copy(
                                        blurRadius = OpticalSizeValue.Fixed(15.dp)
                                    )
                                )

                                // 2. Возвращаем темную тонировку, но почти прозрачную
                                tint(Color.White.copy(alpha = 0.1f))

                                // Включаем блики и отражения обратно (можно подобрать интенсивность)
                                specularIntensity(0.5f)
                                ambientResponse(0.5f)
                                // Добавляем четкую тонкую грань, чтобы панель читалась на фоне
                                edgeShadow(Color.Black.copy(alpha = 0.3f))
                                edgeSoftness(1.dp)

                                // Остальные настройки можно оставить как были
                                chromaticAberrationStrength(0f)
                                whitePoint(0f)
                                surfaceProfile(SurfaceProfile.Circle)
                                chromaticAberrationMode(ChromaticAberrationMode.Simple)
                                contrast(0.08f)
                                chromaMultiplier(1.05f)
                                contentNormalBlend(0.1f)
                                specularExponent(16f)
                                fresnelExponent(2.5f)
                            },
                        )
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().height(60.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .clickable {
                                        if (currentDestination !is Coins) {
                                            coinRememberNavigator.clear()
                                            coinRememberNavigator.add(Coins)
                                        }
                                    },
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_coins),
                                    contentDescription = stringResource(Res.string.market_coins_title),
                                    tint = if (currentDestination is Coins)
                                        PrimaryNeon
                                    else
                                        colorScheme.onSurface
                                )
                                Text(
                                    text = stringResource(Res.string.market_coins_title),
                                    style = typography.bodyMedium,
                                    color = if (currentDestination is Coins)
                                        PrimaryNeon
                                    else
                                        colorScheme.onSurface
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .clickable {
                                        if (currentDestination !is WatchList) {
                                            coinRememberNavigator.clear()
                                            coinRememberNavigator.add(WatchList)
                                        }
                                    },
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "★",
                                    style = typography.bodyMedium,
                                    color = if (currentDestination is WatchList)
                                        StarActive
                                    else
                                        colorScheme.onSurface
                                )
                                Text(
                                    text = stringResource(Res.string.watch_list_title),
                                    style = typography.bodyMedium,
                                    color = if (currentDestination is WatchList)
                                        StarActive
                                    else
                                        colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }

    ) { paddingValues ->
        NavDisplay(
            backStack = coinRememberNavigator,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<Coins> {
                    CoinsScreen(
                        onNavigateToDetails = { id ->
                            coinRememberNavigator.add(CoinDetails(id))
                        },
                        hazeState = hazeState,
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
                        hazeState = hazeState,
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
                        hazeState = hazeState,
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
            })
    }

    CircleLoader(loadingInfo = loadingInfo)
}