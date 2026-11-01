package com.example.marketcoinslist.core

import kotlinx.coroutines.flow.StateFlow

interface ObserveUiState<UiState> {
    fun observeUiState(): StateFlow<UiState>
}