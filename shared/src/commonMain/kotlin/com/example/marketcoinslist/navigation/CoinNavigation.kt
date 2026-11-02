package com.example.marketcoinslist.navigation

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.marketcoinslist.coin_details.presentation.ui.CoinDetailsScreen
import com.example.marketcoinslist.coin_list.presentation.ui.CoinListScreen
import com.example.marketcoinslist.core.notification.ErrorInfo
import com.example.marketcoinslist.core.notification.LoadingInfo
import com.example.marketcoinslist.ui_element.CircleLoader
import com.example.marketcoinslist.ui_element.ErrorView
import com.example.marketcoinslist.utils.EMPTY_VALUE
import com.example.marketcoinslist.watch_list.presentation.ui.WatchListScreen
import marketcoinslist.shared.generated.resources.Res
import marketcoinslist.shared.generated.resources.ic_close
import org.jetbrains.compose.resources.painterResource

@Composable
fun CoinNavigation() {
    val coinRememberNavigator = rememberCoinNavigator()

    val errorInfo = rememberSaveable(stateSaver = ErrorInfo.Saver) { mutableStateOf(null) }
    var loadingInfo by rememberSaveable(stateSaver = LoadingInfo.Saver) { mutableStateOf(null) }

    NavDisplay(
        backStack = coinRememberNavigator,
        modifier = Modifier.background(color = Color.White),
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<CoinList> {
                CoinListScreen()
            }
            entry<CoinDetails> {
                CoinDetailsScreen()
            }
            entry<WatchList> {
                WatchListScreen()
            }
        }
    )

    errorInfo.value?.let {
        ErrorView(
            message = errorInfo.value?.message.orEmpty(),
            isError = errorInfo.value?.isError ?: true,
            errorColor = Color.Red,
            successfulColor = Color.Green,
            iconPainter = painterResource(Res.drawable.ic_close),
            iconContentDescription = EMPTY_VALUE,
            messageStyle = MaterialTheme.typography.bodyMedium,
            onDismissRequest = {
                errorInfo.value = null
            }
        )
    }

    CircleLoader(loadingInfo = loadingInfo)
}