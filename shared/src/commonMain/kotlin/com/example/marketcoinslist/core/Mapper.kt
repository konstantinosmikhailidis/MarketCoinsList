package com.example.marketcoinslist.core

interface Mapper<S, R> {

    fun map(s: S, vararg args: Any?): R

    interface List<S, R> : Mapper<S, R> {
        fun mapList(s : kotlin.collections.List<S>, vararg args: Any?) : kotlin.collections.List<R> {
            return s.map { map(it, *args) }
        }
    }

    interface Unit<S> : Mapper<S, kotlin.Unit>

    interface Default<R> {
        fun map() : R
    }
}