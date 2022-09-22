package com.github.alexzhirkevich.devkit.event

interface EventHandler<T : Event>{

    fun handle(event: T)
}