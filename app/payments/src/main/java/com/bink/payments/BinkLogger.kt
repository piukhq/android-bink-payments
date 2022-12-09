package com.bink.payments

import android.util.Log

/**
 * Helper class for creating logs.
 *
 * @param applicationLogType: An enum class to set what level of logging the current app is
 */
class BinkLogger(private val applicationLogType: LogType) {

    enum class LogType {
        DEBUG,
        VERBOSE,
        ERROR
    }

    init {
        if (applicationLogType == LogType.DEBUG) {
            log(LogType.DEBUG, "Bink Payments SDK is currently running in test mode")
        }
    }

    companion object {
        private const val LOGTAG = "BinkPaymentsLog"
    }

    /**
     * Log a message to the console.
     *
     * @param currentLogType: An enum class to set what level of logging the current message is.
     * @param message: The output of the log.
     */
    fun log(currentLogType: LogType, message: String) {
        if (applicationLogType == LogType.DEBUG) {
            Log.d(LOGTAG, message)
        } else if (currentLogType == LogType.VERBOSE) {
            Log.v(LOGTAG, message)
        } else if (currentLogType == LogType.ERROR) {
            Log.e(LOGTAG, message)
        }
    }

}
