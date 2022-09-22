package com.github.alexzhirkevich.devkit.event

import kotlin.reflect.KClass

abstract class BaseSuspendEventHandler<T : Event>(
    private val clazz : KClass<T>
) : SuspendEventHandler<T> {

    override suspend fun launch() = Unit

    override fun release() = Unit

    override val event: KClass<T>
        get() = clazz
}
