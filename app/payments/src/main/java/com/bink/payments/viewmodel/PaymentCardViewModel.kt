package com.bink.payments.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bink.payments.BinkLogger
import com.bink.payments.BinkPayments
import com.bink.payments.data.PaymentCardRepository
import com.bink.payments.model.PaymentAccount
import com.bink.payments.model.SpreedlyCreditCard
import com.bink.payments.model.SpreedlyPaymentCard
import com.bink.payments.model.SpreedlyPaymentMethod
import kotlinx.coroutines.launch

class PaymentCardViewModel(private val addPaymentCardRepository: PaymentCardRepository) : ViewModel() {

    private var logger: BinkLogger = BinkPayments.getBinkLogger()

    init {
        logger.log(currentLogType = BinkLogger.LogType.DEBUG, message = "Payment Card ViewModel Initialized")
    }

    /**
     * Tokenize payment card with spreedly and send it to the Bink API.
     *
     * @param cardNumber: Users long card number.
     * @param paymentAccount: Object containing all required data for Bink API.
     * @param spreedlyEnvironmentKey: The key required to use the Spreedly API.
     */
    fun sendPaymentCardToSpreedly(cardNumber: String, paymentAccount: PaymentAccount, spreedlyEnvironmentKey: String) {
        logger.log(currentLogType = BinkLogger.LogType.DEBUG, message = "Sending payment card to spreedly")
        val spreedlyCreditCard = SpreedlyCreditCard(
            cardNumber,
            paymentAccount.expiryMonth,
            paymentAccount.expiryYear,
            paymentAccount.nameOnCard
        )

        val spreedlyPaymentMethod = SpreedlyPaymentMethod(spreedlyCreditCard, "true")
        val spreedlyPaymentCard = SpreedlyPaymentCard(spreedlyPaymentMethod)

        viewModelScope.launch {
            try {
                val spreedlyResponse = addPaymentCardRepository.sendPaymentCardToSpreedly(
                    spreedlyPaymentCard,
                    spreedlyEnvironmentKey
                )

                spreedlyResponse.let { response ->

                    logger.log(currentLogType = BinkLogger.LogType.DEBUG, message = "Card successfully tokenized with spreedly")

                    paymentAccount.apply {
                        token = response.transaction.payment_method.token
                        fingerprint = response.transaction.payment_method.fingerprint
                        firstSixDigits = response.transaction.payment_method.first_six_digits
                        lastFourDigits = response.transaction.payment_method.last_four_digits
                    }

                    addPaymentCardRepository.addPaymentCard(paymentAccount)

                    logger.log(currentLogType = BinkLogger.LogType.DEBUG, message = "Payment card ${paymentAccount.nameOnCard} successfully added")
                }
            } catch (e: Exception) {
                logger.log(currentLogType = BinkLogger.LogType.ERROR, message = "${e.message}")
                //TODO: Error
                /**
                 * Show alert with title "Error Adding Card" and
                 * message "There was a problem adding your payment card.
                 * Please try again." with an OK button for dismissal of the alert.
                 */
            }
        }
    }

}