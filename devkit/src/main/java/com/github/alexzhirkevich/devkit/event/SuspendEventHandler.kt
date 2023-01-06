package com.github.alexzhirkevich.devkit.event;

import com.github.alexzhirkevich.devkit.communication.Launcher
import com.github.alexzhirkevich.devkit.Releasable
import com.github.alexzhirkevich.devkit.Updatable
import com.github.alexzhirkevich.devkit.communication.Communication
import com.github.alexzhirkevich.devkit.communication.StateCommunication
import kotlin.reflect.KClass

interface SuspendEventHandler<E : Event, F : Effect, S : ViewState>
    : Launcher, Releasable, Updatable {

    val event: KClass<@UnsafeVariance E>

    val state : StateCommunication<out S>

    val effect : Communication<F>

    @Throws(Exception::class)
    suspend fun handle(event: @UnsafeVariance E)
}
