package com.example.marketcoinslist.core.viewmodel

import kotlinx.coroutines.flow.SharedFlow

interface ObserveEvents<Event> {
    fun observeEvent(): SharedFlow<Event>
}