package com.github.alexzhirkevich.devkit

import kotlinx.coroutines.CoroutineExceptionHandler

interface ErrorHandler {

    fun handle(error : Throwable)

    class Ignore : ErrorHandler {
        override fun handle(error: Throwable) = Unit
    }

    class ReThrow : ErrorHandler {
        override fun handle(error: Throwable) = throw error
    }

    class Log(private val logger: Logger) : ErrorHandler {
        override fun handle(error: Throwable) {
            val (tag,cause) = if (error is TaggedException)
                error.message.orEmpty() to error.cause
            else "" to error

            logger.log(
                msg = "Unhandled error occurred",
                tag = tag,
                cause = cause,
                logLevel = Logger.LogLevel.Error
            )
        }
    }
}

fun ErrorHandler.toCoroutineExceptionHandler() : CoroutineExceptionHandler =
    CoroutineExceptionHandler { _, throwable ->
        handle(throwable)
    }