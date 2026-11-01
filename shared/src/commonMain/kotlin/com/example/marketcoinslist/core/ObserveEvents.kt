package com.example.marketcoinslist.core

import kotlinx.coroutines.flow.SharedFlow

interface ObserveEvents<Event> {
    fun observeEvent(): SharedFlow<Event>
}