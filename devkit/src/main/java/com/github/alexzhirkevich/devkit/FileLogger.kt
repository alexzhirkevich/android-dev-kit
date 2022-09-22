package com.github.alexzhirkevich.devkit

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.os.Build
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

abstract class DefaultLogger : Logger {

    override fun log(msg: String, tag : String, logLevel: Logger.LogLevel, cause: Throwable?) {
        when (logLevel) {
            Logger.LogLevel.Warning -> Log.w(tag, msg, cause)
            Logger.LogLevel.Error -> Log.e(tag, msg, cause)
        }
    }
}

class FileLogger(
    private val logDir: File, fileName: String,
) : DefaultLogger() {

    private val logFile = File(logDir, fileName)

    private val timestampDateFormat: DateFormat = SimpleDateFormat(
        "[yyyy-mm-dd hh:mm:ss]", Locale.getDefault()
    )

    init {
        synchronized(FileLogger::class) {
            tryInit()
        }
    }

    override fun log(msg: String, tag: String, logLevel: Logger.LogLevel, cause : Throwable?) {

        if (BuildConfig.DEBUG) {
            super.log(msg, tag, logLevel, cause)
        }

        synchronized(FileLogger::class) {
            tryInit()
            kotlin.runCatching {
                logFile.appendText(
                    buildString {
                        append(timestampDateFormat.format(Date()))
                        append(" \\\\ ${logLevel.name.take(1)}\n")
                        append("$tag: $msg\n")
                        if (cause != null && logLevel == Logger.LogLevel.Error)
                            append(cause.stackTraceToString())
                        append("\n")
                    }
                )
            }
        }
    }

    private fun tryInit() {
        kotlin.runCatching {
            logDir.mkdirs()
            if (!logFile.exists()) {
                logFile.createNewFile()
                logFile.appendText(buildString {
                    append("Device: ${Build.DEVICE}\n")
                    append("Brand: ${Build.BRAND}\n")
                    append("Model: ${Build.MODEL}\n")
                    append("Manufacturer: ${Build.MANUFACTURER}\n")
                    append("Display: ${Build.DISPLAY}\n")
                    append("API: ${Build.VERSION.SDK_INT}\n")
                    append("\n")
                })
            }
        }
    }
}