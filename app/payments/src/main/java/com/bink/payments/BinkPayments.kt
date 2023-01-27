package com.bink.payments

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import com.bink.payments.di.networkModule
import com.bink.payments.di.spreedlyModule
import com.bink.payments.di.viewModelModule
import com.bink.payments.model.wallet.*
import com.bink.payments.screens.BinkPaymentsActivity
import com.bink.payments.screens.BinkPaymentsOptions
import com.bink.payments.viewmodel.BinkPaymentViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
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
                koin.setProperty("userToken", userToken)
                modules(networkModule, spreedlyModule, viewModelModule)
            }
        }
    }

    /**
     * Start the Bink Payments activity.
     *
     * @param context: The context launching the Bink Payments activity.
     * @param binkPaymentsOptions: Custom UI options.
     */
    fun startCardEntry(context: Context, binkPaymentsOptions: BinkPaymentsOptions? = null) {
        if (!this::userToken.isInitialized || !this::spreedlyEnvironmentKey.isInitialized) {
            throw RuntimeException("The Bink Payments SDK needs to be initialized first")
        }

        val intent = Intent(context, BinkPaymentsActivity::class.java)
        binkPaymentsOptions?.let {
            intent.putExtra(BinkPaymentsActivity.binkPaymentsOptionsName, it)
        }

        intent.putExtra(BinkPaymentsActivity.spreedlyEnvKey, spreedlyEnvironmentKey)

        context.startActivity(intent)
    }

    /**
     * Retrieve the user wallet.
     *
     * @param context: The context used for injecting the view model
     * @param callback: Callback function that returns the user wallet retrieved with the current user token.
     */
    fun getWallet(context: Context, callback: (UserWallet) -> Unit) {
        if (!this::userToken.isInitialized || !this::spreedlyEnvironmentKey.isInitialized) {
            throw RuntimeException("The Bink Payments SDK needs to be initialized first")
        }

        val viewModel: BinkPaymentViewModel by lazy {
            (context as ComponentActivity).getViewModel()
        }

        viewModel.getWallet(callback)
    }

    /**
     * Get the PLL Status for a given loyalty card.
     *
     * @param context: The context used for injecting the view model
     * @param callback: Callback function that returns a an object including all linked and unlinked payment accounts.
     */
    fun getPLLStatus(context: Context, callback: (LoyaltyCardPllState?, Exception?) -> Unit) {
        if (!this::userToken.isInitialized || !this::spreedlyEnvironmentKey.isInitialized) {
            throw RuntimeException("The Bink Payments SDK needs to be initialized first")
        }

        val viewModel: BinkPaymentViewModel by lazy {
            (context as ComponentActivity).getViewModel()
        }

        viewModel.checkPllState(callback)
    }

}
