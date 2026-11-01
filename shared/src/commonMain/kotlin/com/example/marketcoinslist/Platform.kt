package com.example.marketcoinslist

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform