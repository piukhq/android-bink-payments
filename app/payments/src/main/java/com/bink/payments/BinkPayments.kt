package com.bink.payments

import com.bink.payments.di.networkModule
import com.bink.payments.di.spreedlyModule
import com.bink.payments.di.viewModelModule
import com.bink.payments.viewmodel.PaymentCardViewModel
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.inject
import kotlin.properties.Delegates

object BinkPayments {

    private lateinit var userToken: String
    private lateinit var spreedlyEnvironmentKey: String
    private var isDebug by Delegates.notNull<Boolean>()
    private lateinit var binkLogger: BinkLogger

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

}