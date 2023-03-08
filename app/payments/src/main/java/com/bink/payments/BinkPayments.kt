package com.bink.payments

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import com.bink.payments.di.networkModule
import com.bink.payments.di.spreedlyModule
import com.bink.payments.di.viewModelModule
import com.bink.payments.model.wallet.Configuration
import com.bink.payments.model.wallet.LoyaltyCardPllState
import com.bink.payments.model.wallet.UserWallet
import com.bink.payments.screens.BinkPaymentsActivity
import com.bink.payments.screens.BinkPaymentsOptions
import com.bink.payments.viewmodel.BinkPaymentViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.context.startKoin
import kotlin.properties.Delegates

object BinkPayments {

    private lateinit var configuration: Configuration
    private lateinit var refreshToken: String
    private lateinit var spreedlyEnvironmentKey: String
    private var isDebug by Delegates.notNull<Boolean>()

    private lateinit var binkLogger: BinkLogger

    fun getBinkLogger(): BinkLogger {
        if (!this::binkLogger.isInitialized) throw NullPointerException("The Bink Payments SDK needs to be initialized first")
        return binkLogger
    }

    fun getRefreshToken(): String {
        if (!this::refreshToken.isInitialized) throw NullPointerException("The Bink Payments SDK needs to be initialized first")
        return refreshToken
    }

    /**
     * Initialize the Bink Payments library.
     *
     * @param refreshToken: The token required to use the Bink API.
     * @param spreedlyEnvironmentKey: The key required to use the Spreedly API.
     * @param configuration: Configuration object for the BInk API.
     * @param isDebug: If true, enable debug logging.
     */
    fun init(context: Context, refreshToken: String, spreedlyEnvironmentKey: String, configuration: Configuration, isDebug: Boolean) {
        if (refreshToken.isBlank()) throw NullPointerException("User token must not be null or blank")
        if (spreedlyEnvironmentKey.isBlank()) throw NullPointerException("Spreedly Environment Key must not be null or blank")
        if (this::refreshToken.isInitialized && this::spreedlyEnvironmentKey.isInitialized && this::configuration.isInitialized) {
            this.binkLogger.log(BinkLogger.LogType.ERROR, "Bink Payments SDK has already been initialized with User Token:${this.refreshToken} and Spreedly Environment Key:${this.spreedlyEnvironmentKey}")
        } else {
            this.refreshToken = refreshToken
            this.spreedlyEnvironmentKey = spreedlyEnvironmentKey
            this.configuration = configuration
            this.isDebug = isDebug
            this.binkLogger = BinkLogger(if (isDebug) BinkLogger.LogType.DEBUG else BinkLogger.LogType.VERBOSE)

            this.binkLogger.log(BinkLogger.LogType.VERBOSE, "Bink Payments SDK Initialised")
            this.binkLogger.log(BinkLogger.LogType.DEBUG, "Refresh token set to $refreshToken")
            this.binkLogger.log(BinkLogger.LogType.DEBUG, "Spreedly Environment Token set to $spreedlyEnvironmentKey")
            this.binkLogger.log(BinkLogger.LogType.DEBUG, "Loyalty Plan ids: ${configuration.productionLoyaltyPlanId} & ${configuration.devLoyaltyPlanId}")

            startKoin {
                koin.setProperty("refreshToken", refreshToken)
                koin.setProperty("isDebug", isDebug)
                androidContext(context)
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
        if (!this::refreshToken.isInitialized || !this::spreedlyEnvironmentKey.isInitialized) {
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
        if (!this::refreshToken.isInitialized || !this::spreedlyEnvironmentKey.isInitialized) {
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
        if (!this::refreshToken.isInitialized || !this::spreedlyEnvironmentKey.isInitialized) {
            throw RuntimeException("The Bink Payments SDK needs to be initialized first")
        }

        val viewModel: BinkPaymentViewModel by lazy {
            (context as ComponentActivity).getViewModel()
        }

        viewModel.checkPllState(callback)
    }

    /**
     * Add a loyalty card through a trusted channel
     *
     * @param context: The context used for injecting the view model
     * @param loyaltyIdentity: The unique resource identifier for the Loyalty Plan to which the Loyalty Card belongs.
     * @param email: The email associated with the loyalty account
     * @param callback: Callback function that returns an exception if there is an error, or null if its successful.
     */
    fun setTrustedLoyaltyCard(context: Context, loyaltyIdentity: String, email: String, callback: (Exception?) -> Unit) {
        if (!this::refreshToken.isInitialized || !this::spreedlyEnvironmentKey.isInitialized) {
            throw RuntimeException("The Bink Payments SDK needs to be initialized first")
        }

        val viewModel: BinkPaymentViewModel by lazy {
            (context as ComponentActivity).getViewModel()
        }

        viewModel.setTrustedLoyaltyCard(286, loyaltyIdentity, email, callback)
    }


    /**
     * Replace a loyalty card through a trusted channel
     *
     * @param context: The context used for injecting the view model
     * @param loyaltyCardId: The unique indentifier of the loyalty card you're trying to replace
     * @param loyaltyIdentity: The unique resource identifier for the Loyalty Plan to which the Loyalty Card belongs.
     * @param email: The email associated with the loyalty account
     * @param callback: Callback function that returns an exception if there is an error, or null if its successful.
     */
    fun replaceTrustedLoyaltyCard(context: Context, loyaltyCardId: Int, loyaltyIdentity: String, email: String, callback: (Exception?) -> Unit) {
        if (!this::refreshToken.isInitialized || !this::spreedlyEnvironmentKey.isInitialized) {
            throw RuntimeException("The Bink Payments SDK needs to be initialized first")
        }

        val viewModel: BinkPaymentViewModel by lazy {
            (context as ComponentActivity).getViewModel()
        }

        viewModel.replaceTrustedLoyaltyCard(loyaltyCardId, loyaltyIdentity, email, callback)
    }


}