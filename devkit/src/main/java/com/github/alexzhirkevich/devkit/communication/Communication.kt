package com.github.alexzhirkevich.devkit.communication

import kotlinx.coroutines.flow.Flow

interface Communication<T>{

    suspend fun collect(collector : suspend (T) -> Unit)
}

fun <T> Flow<T>.toCommunication() = object : Communication<T> {
    override suspend fun collect(collector: suspend (T) -> Unit) {
        this@toCommunication.collect {
            collector(it)
        }
    }
}