package com.github.alexzhirkevich.devkit.event

import com.github.alexzhirkevich.devkit.communication.MutableStateCommunication
import com.github.alexzhirkevich.devkit.communication.asStateCommunication
import com.github.alexzhirkevich.devkit.communication.Communication
import com.github.alexzhirkevich.devkit.communication.MutableCommunication
import com.github.alexzhirkevich.devkit.communication.asCommunication
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.cancellation.CancellationException
import kotlin.reflect.KClass

abstract class BaseSuspendEventHandler<E : Event, F : Effect, S : ViewState>(
    clazz : KClass<E>,
    private val stateCommunication: MutableStateCommunication<S>,
    private val effectCommunication: MutableCommunication<F>,
) : SuspendEventHandler<E, F, S> {

    internal var stateMutex = Mutex()

    abstract override suspend fun handle(event: @UnsafeVariance E)

    /**
     * Thread-safe state mapping
     *
     * @param state transform old state to new state
     * */
    protected suspend fun mapState(state: suspend S.() -> S) =
        stateMutex.withLock {
            stateCommunication.map(state(stateCommunication.current))
        }

    protected fun mapEffect(effect: F) = effectCommunication.map(effect)

    override val state = stateCommunication.asStateCommunication()

    override val effect: Communication<F> = effectCommunication.asCommunication()

    override val event: KClass<E> = clazz

    private var launched = false

    override suspend fun launch() {
        if (launched) {
            update()
            throw CancellationException()
        }
        launched = true
    }

    override suspend fun update() {}

    override fun release() = Unit

    protected fun finalize() {
        release()
    }
}