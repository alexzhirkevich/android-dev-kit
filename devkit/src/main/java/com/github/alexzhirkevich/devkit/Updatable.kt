package com.github.alexzhirkevich.devkit

import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

interface Updatable {
    suspend fun update()
}

//fun Collection<Updatable>.combine() : Updatable =
//    this.toTypedArray().combine()
//
//fun Array<out Updatable>.combine() = object : Updatable {
//    override suspend fun update() = supervisorScope {
//        forEach {
//            launch { it.update() }
//        }
//    }
//}