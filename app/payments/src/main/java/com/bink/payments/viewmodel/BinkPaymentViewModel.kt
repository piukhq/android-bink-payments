package com.bink.payments.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bink.payments.BinkLogger
import com.bink.payments.BinkPayments
import com.bink.payments.data.WalletRepository
import com.bink.payments.model.trustedcards.*
import com.bink.payments.model.wallet.LoyaltyCardPllState
import com.bink.payments.model.wallet.PaymentAccount
import com.bink.payments.model.wallet.UserWallet
import kotlinx.coroutines.launch

class BinkPaymentViewModel(private val walletRepository: WalletRepository) : ViewModel() {

    private var logger: BinkLogger = BinkPayments.getBinkLogger()
    private var userWallet: UserWallet? = null

    fun getWallet(callback: (UserWallet) -> Unit) {
        viewModelScope.launch {
            try {
                val wallet = walletRepository.getWallet()
                userWallet = wallet
                logger.log(currentLogType = BinkLogger.LogType.DEBUG, message = "Retrieved user wallet")
                callback(wallet)
            } catch (e: Exception) {
                logger.log(currentLogType = BinkLogger.LogType.ERROR, message = "${e.message}")
            }
        }
    }

    fun checkPllState(callback: (LoyaltyCardPllState?, Exception?) -> Unit) {
        if (userWallet == null) {
            logger.log(currentLogType = BinkLogger.LogType.ERROR, message = "Retrieving the user wallet first.")
            getWallet {
                checkPllState(callback)
            }
        } else {
            val loyaltyCard = userWallet!!.loyaltyCards[0] //TODO: This will be replaced once we've added trusted cards
            val linkedPaymentAccounts = arrayListOf<PaymentAccount>()
            val unlinkedPaymentAccounts = arrayListOf<PaymentAccount>()

            userWallet?.paymentAccounts?.let { unlinkedPaymentAccounts.addAll(it) }

            loyaltyCard.pllLinks?.forEach { link ->
                userWallet?.paymentAccounts?.firstOrNull { it.id == link.paymentAccountId }?.let { paymentAccount ->
                    if (link.status?.state == "active") {
                        linkedPaymentAccounts.add(paymentAccount)
                        unlinkedPaymentAccounts.remove(paymentAccount)
                    }
                }
            }

            callback(LoyaltyCardPllState(linked = linkedPaymentAccounts, unlinked = unlinkedPaymentAccounts, timeChecked = System.currentTimeMillis()), null)

        }
    }

    fun setTrustedLoyaltyCard(loyaltyPlanId: Int, loyaltyIdentity: String, email: String, callback: (Exception?) -> Unit) {
        Log.d("From viewModel", "Attempting to set trusted loyalty card")
        val account = Account(
            authoriseFields = AuthoriseFields(listOf(Credential(
                credentialSlug = "email",
                value = email
            ))),
            merchantFields = MerchantFields(accountId = loyaltyIdentity)
        )

        val trustedCardAdd = TrustedCardAdd(
            account = account,
            loyaltyPlanId = loyaltyPlanId
        )

        viewModelScope.launch {
            try {
                walletRepository.addTrustedLoyaltyCard(trustedCardAdd)
                logger.log(currentLogType = BinkLogger.LogType.VERBOSE, message = "Added trusted card for $email")
                callback(null)
            } catch (e: Exception) {
                logger.log(currentLogType = BinkLogger.LogType.ERROR, message = "${e.message}")
                callback(e)
            }
        }
    }

    fun replaceTrustedLoyaltyCard(loyaltyCardId: Int, loyaltyIdentity: String, email: String, callback: (Exception?) -> Unit) {
        val account = Account(
            authoriseFields = AuthoriseFields(listOf(Credential(
                credentialSlug = "email",
                value = email
            ))),
            merchantFields = MerchantFields(accountId = loyaltyIdentity)
        )

        val trustedCardReplace = TrustedCardReplace(
            account = account
        )

        viewModelScope.launch {
            try {
                walletRepository.replaceTrustedLoyaltyCard(loyaltyCardId.toString(), trustedCardReplace)
                logger.log(currentLogType = BinkLogger.LogType.VERBOSE, message = "Replaced trusted card for $email")
                callback(null)
            } catch (e: Exception) {
                logger.log(currentLogType = BinkLogger.LogType.ERROR, message = "${e.message}")
                callback(e)
            }
        }
    }
}