package com.bink.payments

import android.util.Log

/**
 * Set a logType upon initialization
 */
class BinkLogger(private val applicationLogType: LogType) {

    init {
        if (applicationLogType == LogType.DEBUG) {
            log(LogType.DEBUG, "Bink Payments SDK is currently running in test mode")
        }
    }

    companion object {
        private const val LOGTAG = "BinkPaymentsLog"
    }

    enum class LogType {
        DEBUG,
        VERBOSE,
        ERROR
    }

    /**
     * If the application is set to debug, we want to display the log under all conditions.
     * Otherwise, we only want to display verbose and error logs whilst the application isn't in debug.
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