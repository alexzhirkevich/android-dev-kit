package com.github.alexzhirkevich.devkit

class TaggedException(
    tag : String,
    cause: Throwable?
) : Exception(tag,cause)

interface Logger {

    enum class LogLevel{
        Warning,
        Error,
    }

    fun log(
        msg : String,
        tag : String = javaClass.simpleName,
        logLevel: LogLevel = LogLevel.Warning,
        cause: Throwable?=null,
    )
}