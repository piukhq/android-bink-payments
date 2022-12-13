package com.bink.payments

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.bink.payments.di.networkModule
import com.bink.payments.di.spreedlyModule
import com.bink.payments.di.viewModelModule
import org.koin.core.context.startKoin
import kotlin.properties.Delegates

object BinkPayments {

    private lateinit var userToken: String
    private lateinit var spreedlyEnvironmentKey: String
    private var isDebug by Delegates.notNull<Boolean>()

    private lateinit var binkLogger: BinkLogger

    fun getBinkLogger(): BinkLogger {
        if (!this::binkLogger.isInitialized) throw NullPointerException("The Bink Payments SDK needs to be initialized first")
        return binkLogger
    }

    /**
     * Initialize the Bink Payments library.
     *
     * @param userToken: The token required to use the Bink API.
     * @param spreedlyEnvironmentKey: The key required to use the Spreedly API.
     * @param isDebug: If true, enable debug logging.
     */
    fun init(userToken: String, spreedlyEnvironmentKey: String, isDebug: Boolean) {
        if (userToken.isBlank()) throw NullPointerException("User token must not be null or blank")
        if (spreedlyEnvironmentKey.isBlank()) throw NullPointerException("Spreedly Environment Key must not be null or blank")
        if (this::userToken.isInitialized && this::spreedlyEnvironmentKey.isInitialized) {
            this.binkLogger.log(BinkLogger.LogType.ERROR, "Bink Payments SDK has already been initialized with User Token:${this.userToken} and Spreedly Environment Key:${this.spreedlyEnvironmentKey}")
        } else {
            this.userToken = userToken
            this.spreedlyEnvironmentKey = spreedlyEnvironmentKey
            this.isDebug = isDebug
            this.binkLogger = BinkLogger(if (isDebug) BinkLogger.LogType.DEBUG else BinkLogger.LogType.VERBOSE)

            this.binkLogger.log(BinkLogger.LogType.VERBOSE, "Bink Payments SDK Initialised")
            this.binkLogger.log(BinkLogger.LogType.DEBUG, "User token set to $userToken")
            this.binkLogger.log(BinkLogger.LogType.DEBUG, "Spreedly Environment Token set to $spreedlyEnvironmentKey")

            startKoin {
                modules(networkModule, spreedlyModule, viewModelModule)
            }
        }
    }

    /**
     * Start the Bink Payments activity.
     *
     * @param context: The context launching the Bink Payments activity.
     */
    fun startCardEntry(context: Context) {
        if (!this::userToken.isInitialized || !this::spreedlyEnvironmentKey.isInitialized) {
            throw RuntimeException("The Bink Payments SDK needs to be initialized first")
        }

        context.startActivity(Intent(context, BinkPaymentsActivity::class.java))
    }

}
