package com.example.marketcoinslist.core.viewmodel

import kotlinx.coroutines.flow.StateFlow

interface ObserveUiState<UiState> {
    fun observeUiState(): StateFlow<UiState>
}