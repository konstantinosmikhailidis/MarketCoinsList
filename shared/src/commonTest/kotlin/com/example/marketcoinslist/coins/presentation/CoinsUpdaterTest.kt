package com.example.marketcoinslist.coins.presentation

import com.example.marketcoinslist.coins.domain.model.Coin
import com.example.marketcoinslist.core.notification.NotificationInfo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertNull
import kotlin.test.assertNotNull

class CoinsUpdaterTest {

    private val updater = CoinsUpdater()

    @Test
    fun `RefreshCoins action should not change state but emit LoadCoins effect and ShowLoadingAnimation event`() {
        // Arrange
        val initialState = CoinsState()
        val action = CoinsAction.RefreshCoins

        // Act
        val result = updater.update(action, initialState)

        // Assert
        assertEquals(
            initialState,
            result.state,
            "State should remain unchanged"
        )
        assertTrue(
            result.effects.contains(CoinsSideEffect.LoadCoins),
            "Should emit LoadCoins effect"
        )

        val event = result.events.firstOrNull() as? CoinsEvent.ShowLoadingAnimation
        assertNotNull(event, "Should emit ShowLoadingAnimation event")
        assertNotNull(
            event.loadingInfo,
            "LoadingInfo should not be null when starting load"
        )
    }

    @Test
    fun `CoinsUpdated action should update coins list in state`() {
        // Arrange
        val initialState = CoinsState()
        val mockCoins = listOf(
            Coin(
                id = "bitcoin",
                symbol = "btc",
                name = "Bitcoin",
                imageUrl = "",
                currentPrice = 50000.0,
                priceChangePercentage24h = 5.0,
                marketCapRank = 1,
                isFavorite = false
            )
        )
        val action = CoinsAction.CoinsUpdated(mockCoins)

        // Act
        val result = updater.update(action, initialState)

        // Assert
        assertEquals(
            mockCoins,
            result.state.coins,
            "State should contain updated coins"
        )
        assertTrue(
            result.effects.isEmpty(),
            "Should not emit any effects"
        )
        assertTrue(
            result.events.isEmpty(),
            "Should not emit any events"
        )
    }

    @Test
    fun `RefreshFailed action should emit ShowNotification and hide loading animation`() {
        // Arrange
        val initialState = CoinsState()
        val notificationInfo = NotificationInfo("Network error", isError = true)
        val action = CoinsAction.RefreshFailed(notificationInfo)

        // Act
        val result = updater.update(action, initialState)

        // Assert
        assertEquals(
            initialState,
            result.state,
            "State should remain unchanged"
        )

        val events = result.events.toList()
        assertEquals(2, events.size, "Should emit 2 events")

        val notificationEvent = events.filterIsInstance<CoinsEvent.ShowNotification>().firstOrNull()
        assertNotNull(notificationEvent, "Should emit ShowNotification event")
        assertEquals("Network error", notificationEvent.notificationInfo.message)

        val loadingEvent = events.filterIsInstance<CoinsEvent.ShowLoadingAnimation>().firstOrNull()
        assertNotNull(loadingEvent, "Should emit ShowLoadingAnimation event")
        assertNull(
            loadingEvent.loadingInfo,
            "LoadingInfo should be null to hide animation"
        )
    }

    @Test
    fun `ToggleFavorite action should emit ToggleFavorite side effect`() {
        // Arrange
        val initialState = CoinsState()
        val action = CoinsAction.ToggleFavorite("ethereum")

        // Act
        val result = updater.update(action, initialState)

        // Assert
        assertEquals(
            initialState,
            result.state,
            "State should remain unchanged"
        )
        assertTrue(
            result.effects.contains(CoinsSideEffect.ToggleFavorite("ethereum")),
            "Should emit ToggleFavorite effect"
        )
        assertTrue(
            result.events.isEmpty(),
            "Should not emit any events"
        )
    }
}