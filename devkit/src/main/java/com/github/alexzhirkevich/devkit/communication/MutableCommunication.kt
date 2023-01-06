package com.github.alexzhirkevich.devkit.communication

import kotlinx.coroutines.channels.BufferOverflow

fun <T> MutableCommunication(
    extraBufferCapacity : Int = 3,
    onBufferOverflow: BufferOverflow = BufferOverflow.DROP_OLDEST
)  : MutableCommunication<T> = SharedFlowCommunication(
    extraBufferCapacity , onBufferOverflow
)

interface MutableCommunication<T> :  Mapper<T>, Communication<T>

fun <T> MutableCommunication<T>.asCommunication() : Communication<T> =
    object : Communication<T> by this{}


internal object EmptyCommunication : MutableCommunication<Nothing> {
    override suspend fun collect(collector: suspend (Nothing) -> Unit) = Unit

    override fun map(data: Nothing) = Unit
}
