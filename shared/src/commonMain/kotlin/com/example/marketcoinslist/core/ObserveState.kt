package com.example.marketcoinslist.core

import kotlinx.coroutines.flow.Flow

interface ObserveState<State> {
    fun observeState(): Flow<State>
}