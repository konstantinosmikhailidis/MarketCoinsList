package com.example.marketcoinslist.core.viewmodel

import kotlinx.coroutines.flow.Flow

interface ObserveState<State> {
    fun observeState(): Flow<State>
}