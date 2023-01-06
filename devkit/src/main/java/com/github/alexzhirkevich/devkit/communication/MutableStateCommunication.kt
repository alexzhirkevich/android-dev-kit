package com.github.alexzhirkevich.devkit.communication

fun <T> MutableStateCommunication(initial : T) : MutableStateCommunication<T> =
    StateFlowCommunication(initial)

interface MutableStateCommunication<T> : StateCommunication<T>, StateMapper<T>,
    MutableCommunication<T>

fun <T> MutableStateCommunication<T>.asStateCommunication() =
    object : StateCommunication<T> by this {}