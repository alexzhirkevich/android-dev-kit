package com.github.alexzhirkevich.devkit.event

fun interface EventHandler<T : Event>{

    fun handle(event: T)
}