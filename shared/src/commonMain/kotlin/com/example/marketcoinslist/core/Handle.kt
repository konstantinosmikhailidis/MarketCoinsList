package com.example.marketcoinslist.core

interface Handle<T> {
    fun handle(data: T)
}