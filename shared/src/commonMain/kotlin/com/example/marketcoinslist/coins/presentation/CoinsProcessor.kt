package com.example.marketcoinslist.coins.presentation

import com.example.marketcoinslist.core.viewmodel.Processor
import com.example.marketcoinslist.coins.domain.repository.CoinRepository
import com.example.marketcoinslist.core.notification.NotificationInfo
import marketcoinslist.shared.generated.resources.Res
import marketcoinslist.shared.generated.resources.failed_to_load_coins
import org.jetbrains.compose.resources.getString

class CoinsProcessor(
    private val repository: CoinRepository,
) : Processor<CoinsAction, CoinsSideEffect> {

    override suspend fun process(effect: CoinsSideEffect): CoinsAction {
        return when (effect) {
            is CoinsSideEffect.LoadCoins -> safeAction(
                defaultErrorMessage = getString(Res.string.failed_to_load_coins)
            ) {
                repository.refreshCoins()
                CoinsAction.CoinsRefreshed
            }

            is CoinsSideEffect.ToggleFavorite -> safeAction {
                repository.toggleFavorite(effect.id)
                CoinsAction.CoinsRefreshed
            }
        }
    }

    override fun onError(message: String): CoinsAction {
        return CoinsAction.RefreshFailed(
            NotificationInfo(
                message = message
            )
        )
    }
}