package com.github.alexzhirkevich.devkit.communication

interface MutableStateCommunication<T> : StateCommunication<T>, StateMapper<T>,
    MutableCommunication<T>