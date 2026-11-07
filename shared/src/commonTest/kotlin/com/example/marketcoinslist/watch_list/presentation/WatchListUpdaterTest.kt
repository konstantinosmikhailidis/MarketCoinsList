package com.example.marketcoinslist.watch_list.presentation

import com.example.marketcoinslist.coins.domain.model.Coin
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class WatchListUpdaterTest {

    private val updater = WatchListUpdater()

    @Test
    fun `WatchListUpdated action updates coins in state`() {
        val initialState = WatchListState()
        val mockCoins = listOf(Coin(
            "bitcoin",
            "BTC",
            "Bitcoin",
            "",
            50000.0,
            null,
            1
        ))
        val action = WatchListAction.WatchListUpdated(mockCoins)

        val result = updater.update(action, initialState)

        assertEquals(mockCoins, result.state.coins)
        assertTrue(result.effects.isEmpty())
        assertTrue(result.events.isEmpty())
    }

    @Test
    fun `RemoveFavorite action emits RemoveFavorite side effect`() {
        val initialState = WatchListState()
        val action = WatchListAction.RemoveFavorite("bitcoin")

        val result = updater.update(action, initialState)

        assertEquals(initialState, result.state)
        assertTrue(result.effects.contains(
            WatchListSideEffect.RemoveFavorite("bitcoin"))
        )
        assertTrue(result.events.isEmpty())
    }

    @Test
    fun `Nothing action does nothing`() {
        val initialState = WatchListState()
        val action = WatchListAction.Nothing

        val result = updater.update(action, initialState)

        assertEquals(initialState, result.state)
        assertTrue(result.effects.isEmpty())
        assertTrue(result.events.isEmpty())
    }
}