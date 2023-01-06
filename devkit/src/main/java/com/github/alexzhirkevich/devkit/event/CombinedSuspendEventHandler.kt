package com.github.alexzhirkevich.devkit.event

import com.github.alexzhirkevich.devkit.Releasable
import com.github.alexzhirkevich.devkit.communication.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlin.reflect.KClass

open class CombinedSuspendEventHandler<E : Event, F : Effect, S : ViewState>(
    override val event : KClass<E>,
    stateCommunication: MutableStateCommunication<S>,
    effectCommunication: MutableCommunication<F>,
    private vararg val handlers: BaseSuspendEventHandler<out E, out F, out S>,
) : BaseSuspendEventHandler<E, F, S>(
    event, stateCommunication, effectCommunication
) {

    override val state: StateCommunication<S> =
        stateCommunication.asStateCommunication()
    override val effect: Communication<F> =
        effectCommunication.asCommunication()

    init {
        handlers.forEach {
            it.stateMutex = this.stateMutex
        }
    }

    override suspend fun update() = supervisorScope {
        handlers.forEach {
            launch { it.update() }
        }
    }

    override suspend fun launch() = supervisorScope {
        handlers.forEach {
            launch { it.launch() }
        }
    }

    override suspend fun handle(event: E) =
        requireNotNull(handlers.find { it.event == event::class }) {
            "Handler for ${event::class.qualifiedName} not found"
        }.handle(event)


    override fun release() {
        kotlin.runCatching {
            handlers.forEach(Releasable::release)
        }
    }
}