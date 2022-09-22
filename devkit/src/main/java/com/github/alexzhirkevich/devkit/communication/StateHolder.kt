package com.github.alexzhirkevich.devkit.communication

import androidx.lifecycle.SavedStateHandle

interface StateHolder<T> {

    val current : T

    /**
     * Restore [current] state from [savedStateHandle] by [key] and save all future states.
     * */
    suspend fun saveIn(savedStateHandle: SavedStateHandle, key : String)
}