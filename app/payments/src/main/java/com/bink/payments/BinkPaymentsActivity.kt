package com.bink.payments

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bink.payments.model.PaymentAccount
import com.bink.payments.ui.BinkPaymentsTheme
import com.bink.payments.utils.EXPIRY_YEAR
import com.bink.payments.viewmodel.PaymentCardViewModel
import org.koin.android.ext.android.inject

class BinkPaymentsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val paymentCardViewModel by inject<PaymentCardViewModel>()

        setContent {
            BinkPaymentsTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Greeting(paymentCardViewModel)
                }
            }
        }
    }

    @Composable
    fun Greeting(paymentCardViewModel: PaymentCardViewModel) {
        Text(text = "BinkPayments SDK")

        val paymentAccount = getPaymentAccount()
        paymentCardViewModel.sendPaymentCardToSpreedly("5555 5555 5555 4444", paymentAccount, "1Lf7DiKgkcx5Anw7QxWdDxaKtTa")
    }

    private fun getPaymentAccount(): PaymentAccount {
        val nameOnCard = "Enoch"
        val nickname = "Sucks"
        val cardNumber = "5555 5555 5555 4444"
        val cardExpiry = "12/23".split("/")

        return PaymentAccount(
            cardNickname = nickname,
            country = "GB",
            currencyCode = "GBP",
            expiryMonth = cardExpiry[0],
            expiryYear = (cardExpiry[1].toInt() + EXPIRY_YEAR).toString(),
            fingerprint = PaymentAccount.fingerprintGenerator(
                cardNumber,
                cardExpiry[0],
                cardExpiry[1]
            ),
            firstSixDigits = cardNumber.substring(0, 6),
            lastFourDigits = cardNumber.substring(cardNumber.length - 4),
            nameOnCard = nameOnCard,
            token = PaymentAccount.tokenGenerator()
        )

    }
}