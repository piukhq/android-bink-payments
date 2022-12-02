package com.bink.payments.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bink.payments.data.PaymentCardRepository
import com.bink.payments.model.PaymentAccount
import com.bink.payments.model.SpreedlyCreditCard
import com.bink.payments.model.SpreedlyPaymentCard
import com.bink.payments.model.SpreedlyPaymentMethod
import kotlinx.coroutines.launch

class PaymentCardViewModel(private val addPaymentCardRepository: PaymentCardRepository) :
    ViewModel() {

    fun sendPaymentCardToSpreedly(cardNumber: String, paymentAccount: PaymentAccount, spreedlyEnvironmentKey: String) {
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
                    paymentAccount.apply {
                        token = response.transaction.payment_method.token
                        fingerprint = response.transaction.payment_method.fingerprint
                        firstSixDigits = response.transaction.payment_method.first_six_digits
                        lastFourDigits = response.transaction.payment_method.last_four_digits
                    }

                    addPaymentCardRepository.addPaymentCard(paymentAccount)
                    //TODO: Success
                }
            } catch (e: Exception) {
                //TODO: Error
            }
        }
    }


}