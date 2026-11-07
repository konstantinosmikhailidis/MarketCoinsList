package com.example.marketcoinslist.coin_details.presentation

import com.example.marketcoinslist.coin_details.domain.model.MarketChart
import com.example.marketcoinslist.coins.domain.model.Coin
import com.example.marketcoinslist.core.notification.NotificationInfo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CoinDetailsUpdaterTest {

    private val updater = CoinDetailsUpdater()

    @Test
    fun `LoadDetails action emits FetchMarketChart effect and ShowLoadingAnimation event`() {
        val initialState = CoinDetailsState(selectedDays = "7")
        val action = CoinDetailsAction.LoadDetails("bitcoin")

        val result = updater.update(action, initialState)

        assertEquals(initialState, result.state)
        assertTrue(result.effects.contains(CoinDetailsSideEffect.FetchMarketChart("bitcoin", "7")))

        val loadingEvent = result.events.filterIsInstance<CoinDetailsEvent.ShowLoadingAnimation>().firstOrNull()
        assertNotNull(loadingEvent)
        assertNotNull(loadingEvent.loadingInfo)
    }

    @Test
    fun `SelectTimeframe action updates selectedDays and triggers fetch`() {
        val initialState = CoinDetailsState(selectedDays = "1")
        val action = CoinDetailsAction.SelectTimeframe("ethereum", "30")

        val result = updater.update(action, initialState)

        assertEquals("30", result.state.selectedDays)
        assertTrue(result.effects.contains(CoinDetailsSideEffect.FetchMarketChart("ethereum", "30")))

        val loadingEvent = result.events.filterIsInstance<CoinDetailsEvent.ShowLoadingAnimation>().firstOrNull()
        assertNotNull(loadingEvent)
        assertNotNull(loadingEvent.loadingInfo)
    }

    @Test
    fun `CoinUpdated action updates coin in state`() {
        val initialState = CoinDetailsState()
        val mockCoin = Coin("bitcoin", "BTC", "Bitcoin", "", 50000.0, null, 1)
        val action = CoinDetailsAction.CoinUpdated(mockCoin)

        val result = updater.update(action, initialState)

        assertEquals(mockCoin, result.state.coin)
        assertTrue(result.effects.isEmpty())
        assertTrue(result.events.isEmpty())
    }

    @Test
    fun `MarketChartUpdated action updates chart in state and hides loading`() {
        val initialState = CoinDetailsState()
        val mockChart = MarketChart(prices = emptyList())
        val action = CoinDetailsAction.MarketChartUpdated(mockChart)

        val result = updater.update(action, initialState)

        assertEquals(mockChart, result.state.marketChart)

        val loadingEvent = result.events.filterIsInstance<CoinDetailsEvent.ShowLoadingAnimation>().firstOrNull()
        assertNotNull(loadingEvent)
        assertNull(loadingEvent.loadingInfo)
    }

    @Test
    fun `LoadNotification action shows notification and hides loading`() {
        val initialState = CoinDetailsState()
        val notification = NotificationInfo("Failed")
        val action = CoinDetailsAction.LoadNotification(notification)

        val result = updater.update(action, initialState)

        val events = result.events.toList()
        assertEquals(2, events.size)

        val notificationEvent = events.filterIsInstance<CoinDetailsEvent.ShowNotification>().firstOrNull()
        assertNotNull(notificationEvent)
        assertEquals("Failed", notificationEvent.notificationInfo.message)

        val loadingEvent = events.filterIsInstance<CoinDetailsEvent.ShowLoadingAnimation>().firstOrNull()
        assertNotNull(loadingEvent)
        assertNull(loadingEvent.loadingInfo)
    }

    @Test
    fun `ToggleFavorite action emits effect`() {
        val initialState = CoinDetailsState()
        val action = CoinDetailsAction.ToggleFavorite("bitcoin")

        val result = updater.update(action, initialState)

        assertTrue(result.effects.contains(CoinDetailsSideEffect.ToggleFavorite("bitcoin")))
        assertTrue(result.events.isEmpty())
    }
}