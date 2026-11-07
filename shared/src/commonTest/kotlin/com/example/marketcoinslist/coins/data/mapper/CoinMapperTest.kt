package com.example.marketcoinslist.coins.data.mapper

import com.example.marketcoinslist.coins.data.remote.dto.CoinDto
import com.example.marketcoinslist.database.CoinEntity
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.test.assertNull

class CoinMapperTest {

    @Test
    fun `CoinDto toEntity maps all fields correctly and uppercases symbol`() {
        val dto = CoinDto(
            id = "bitcoin",
            symbol = "btc", // lowercase symbol to test capitalization
            name = "Bitcoin",
            image = "https://example.com/btc.png",
            currentPrice = 50000.0,
            marketCap = 1_000_000_000.0,
            marketCapRank = 1,
            totalVolume = 500_000.0,
            high24h = 51000.0,
            low24h = 49000.0,
            priceChange24h = 1000.0,
            priceChangePercentage24h = 2.0
        )

        val entity = dto.toEntity()

        assertEquals("bitcoin", entity.id)
        assertEquals("BTC", entity.symbol, "Symbol should be uppercased")
        assertEquals("Bitcoin", entity.name)
        assertEquals("https://example.com/btc.png", entity.image)
        assertEquals(50000.0, entity.currentPrice)
        assertEquals(2.0, entity.priceChange24h)
        assertEquals(1L, entity.marketCapRank)
    }

    @Test
    fun `CoinEntity toDomain maps all fields correctly and handles nulls`() {
        val entity = CoinEntity(
            id = "ethereum",
            symbol = "ETH",
            name = "Ethereum",
            image = "https://example.com/eth.png",
            currentPrice = 3000.0,
            priceChange24h = -1.5,
            marketCapRank = null // Testing null rank
        )

        val domain = entity.toDomain(isFavorite = true)

        assertEquals("ethereum", domain.id)
        assertEquals("ETH", domain.symbol)
        assertEquals("Ethereum", domain.name)
        assertEquals("https://example.com/eth.png", domain.imageUrl)
        assertEquals(3000.0, domain.currentPrice)
        assertEquals(-1.5, domain.priceChangePercentage24h)
        assertNull(domain.marketCapRank)
        assertTrue(domain.isFavorite, "isFavorite should be passed from argument")
    }

    @Test
    fun `CoinEntity toDomain maps correctly with isFavorite false`() {
        val entity = CoinEntity(
            id = "solana",
            symbol = "SOL",
            name = "Solana",
            image = "",
            currentPrice = 100.0,
            priceChange24h = null,
            marketCapRank = 5
        )

        val domain = entity.toDomain(isFavorite = false)

        assertFalse(domain.isFavorite, "isFavorite should be false")
        assertNull(domain.priceChangePercentage24h)
    }
}