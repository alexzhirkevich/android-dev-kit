package com.github.alexzhirkevich.devkit.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.alexzhirkevich.devkit.ErrorHandler
import com.github.alexzhirkevich.devkit.dispatchers.Dispatchers
import com.github.alexzhirkevich.devkit.toCoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope

open class SuspendHandlerViewModel<T : Event>(
    private val dispatchers: Dispatchers,
    private val suspendEventHandler: SuspendEventHandler<T>,
    private val errorHandler: ErrorHandler
) : ViewModel(), EventHandler<T> {

    init {
        launch {
            suspendEventHandler.launch()
        }
    }

    final override fun handle(event: T) = launch {
        suspendEventHandler.handle(event)
    }

    override fun onCleared() {
        super.onCleared()
        suspendEventHandler.release()
    }


    private fun launch(block : suspend CoroutineScope.() -> Unit){
        dispatchers.launchIO(
            viewModelScope,
            exceptionHandler = errorHandler.toCoroutineExceptionHandler(),
            block = block
        )
    }
}