package com.github.alexzhirkevich.devkit.communication

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach

class SharedFlowCommunication<T>(
    extraBufferCapacity : Int = 3,
    onBufferOverflow : BufferOverflow = BufferOverflow.DROP_OLDEST
) : MutableCommunication<T> {

    private val flow = MutableSharedFlow<T>(
        extraBufferCapacity = extraBufferCapacity,
        onBufferOverflow = onBufferOverflow
    )

    override fun map(data: T)  {
        flow.tryEmit(data)
    }

    override suspend fun collect(collector: suspend (T) -> Unit) =
        flow.onEach(collector).collect()
}
