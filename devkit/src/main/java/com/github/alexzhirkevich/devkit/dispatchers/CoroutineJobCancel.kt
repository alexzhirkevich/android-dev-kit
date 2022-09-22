package com.github.alexzhirkevich.devkit.dispatchers

fun interface CoroutineJobCancel {
    fun cancel(key: Any?)
}
