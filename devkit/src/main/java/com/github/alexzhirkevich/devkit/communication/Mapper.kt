package com.github.alexzhirkevich.devkit.communication

fun interface Mapper<T> {
    fun map(data : T)
}