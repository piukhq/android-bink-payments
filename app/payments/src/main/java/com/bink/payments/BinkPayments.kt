package com.bink.payments

import kotlin.properties.Delegates

class BinkPayments {

    private lateinit var userToken: String
    private lateinit var spreedlyEnvironmentKey: String
    private var isDebug by Delegates.notNull<Boolean>()
    private lateinit var binkLogger: BinkLogger

    fun init(userToken: String, spreedlyEnvironmentKey: String, isDebug: Boolean) {
        this.userToken = userToken
        this.spreedlyEnvironmentKey = spreedlyEnvironmentKey
        this.isDebug = isDebug
        this.binkLogger = BinkLogger(if (isDebug) BinkLogger.LogType.DEBUG else BinkLogger.LogType.VERBOSE)

        this.binkLogger.log(BinkLogger.LogType.VERBOSE, "Bink Payments SDK Initialised")
        this.binkLogger.log(BinkLogger.LogType.DEBUG, "User token set to $userToken")
        this.binkLogger.log(BinkLogger.LogType.DEBUG, "Spreedly Environment Token set to $spreedlyEnvironmentKey")
    }

}