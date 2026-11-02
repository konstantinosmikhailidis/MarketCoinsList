package com.example.marketcoinslist.core.viewmodel

interface Handle<T> {
    fun handle(data: T)
}