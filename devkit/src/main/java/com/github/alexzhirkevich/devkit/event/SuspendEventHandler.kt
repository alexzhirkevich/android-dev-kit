package com.github.alexzhirkevich.devkit.event

import com.github.alexzhirkevich.devkit.communication.Launcher
import com.github.alexzhirkevich.devkit.communication.Releasable
import kotlin.reflect.KClass

@JvmSuppressWildcards
interface SuspendEventHandler<T : Event> : Launcher, Releasable {

    val event: KClass<@UnsafeVariance T>

    suspend fun handle(event: @UnsafeVariance T)
    
    companion object {
        inline fun <reified T : Event> from(
            vararg handlers: SuspendEventHandler<out T>
        ): SuspendEventHandler<T> = from(T::class, *handlers)

        fun <T : Event> from(
            clazz: KClass<T>,
            vararg handlers: SuspendEventHandler<out T>
        ): SuspendEventHandler<T> = object : SuspendEventHandler<T> {
            override val event: KClass<T> get() = clazz

            override suspend fun handle(event: T) {
                val handler = requireNotNull(handlers.find {
                    it.event == event::class
                }) {
                    "Handler for ${event::class.qualifiedName} not found"
                }

                handler.handle(event)
            }

            override suspend fun launch() =
                Launcher.combine(*handlers)
                    .launch()


            override fun release() {
                handlers.forEach {
                    it.release()
                }
            }
        }
    }
}