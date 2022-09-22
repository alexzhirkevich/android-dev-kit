package com.github.alexzhirkevich.devkit.dispatchers

import kotlinx.coroutines.Job

fun interface CoroutineJobSaver {
    fun save(job: Job, key : Any?)
}