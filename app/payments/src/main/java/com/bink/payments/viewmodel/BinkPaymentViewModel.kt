package com.bink.payments.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bink.payments.BinkLogger
import com.bink.payments.BinkPayments
import com.bink.payments.data.WalletRepository
import com.bink.payments.model.wallet.*
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

    fun checkPllState(paymentAccount: PaymentAccount, callback: (PaymentAccountPllState?, Exception?) -> Unit) {
        if (userWallet != null) {
            val linkedLoyaltyCards = arrayListOf<LoyaltyCard>()
            val unlinkedLoyaltyCards = arrayListOf<LoyaltyCard>()

            userWallet?.loyaltyCards?.let { unlinkedLoyaltyCards.addAll(it) }

            paymentAccount.pllLinks?.forEach { link ->
                userWallet?.loyaltyCards?.firstOrNull { it.id == link.loyaltyCardId }?.let { loyaltyCard ->
                    if (link.status?.state == "active") {
                        linkedLoyaltyCards.add(loyaltyCard)
                        unlinkedLoyaltyCards.remove(loyaltyCard)
                    }
                }
            }

            callback(PaymentAccountPllState(linked = linkedLoyaltyCards, unlinked = unlinkedLoyaltyCards, timeChecked = System.currentTimeMillis()), null)
        } else {
            logger.log(currentLogType = BinkLogger.LogType.ERROR, message = "Please retrieve the user wallet first.")
            callback(null, Exception("Please retrieve the user wallet first."))
        }
    }

    fun checkPllState(loyaltyCard: LoyaltyCard, callback: (LoyaltyCardPllState?, Exception?) -> Unit) {
        if (userWallet != null) {
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

        } else {
            logger.log(currentLogType = BinkLogger.LogType.ERROR, message = "Please retrieve the user wallet first.")
            callback(null, Exception("Please retrieve the user wallet first."))
        }
    }
}