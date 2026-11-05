package com.example.marketcoinslist.coins.data.mapper

import com.example.marketcoinslist.database.CoinEntity
import com.example.marketcoinslist.coins.data.remote.dto.CoinDto
import com.example.marketcoinslist.coins.domain.model.Coin

fun CoinDto.toEntity(): CoinEntity {
    return CoinEntity(
        id = id,
        symbol = symbol.uppercase(),
        name = name,
        image = image,
        currentPrice = currentPrice,
        priceChange24h = priceChangePercentage24h,
        marketCapRank = marketCapRank?.toLong()
    )
}

fun CoinEntity.toDomain(isFavorite: Boolean = false): Coin {
    return Coin(
        id = id,
        symbol = symbol,
        name = name,
        imageUrl = image,
        currentPrice = currentPrice,
        priceChangePercentage24h = priceChange24h,
        marketCapRank = marketCapRank?.toInt(),
        isFavorite = isFavorite
    )
}