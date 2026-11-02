package com.example.marketcoinslist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import marketcoinslist.shared.generated.resources.Res
import marketcoinslist.shared.generated.resources.coin_details_screen_name
import marketcoinslist.shared.generated.resources.coin_list_screen_name
import marketcoinslist.shared.generated.resources.watch_list_screen_name
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Serializable
sealed interface Screen : NavKey {
    val titleRes: StringResource? get() = null


    val title: String
        @Composable
        get() = titleRes?.let { stringResource(it) }.orEmpty()
}

@Serializable
data object CoinList : Screen {
    override val titleRes get() = Res.string.coin_list_screen_name
}

@Serializable
data object CoinDetails : Screen {
    override val titleRes get() = Res.string.coin_details_screen_name
}

@Serializable
data object WatchList : Screen {
    override val titleRes get() = Res.string.watch_list_screen_name
}