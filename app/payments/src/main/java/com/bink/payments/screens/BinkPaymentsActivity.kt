package com.bink.payments.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                    CreatePaymentCardScreen(paymentCardViewModel = paymentCardViewModel)
                }
            }
        }
    }

    @Composable
    private fun CreatePaymentCardScreen(paymentCardViewModel: PaymentCardViewModel) {
        val paymentCardUiState by paymentCardViewModel.uiState.collectAsState()

        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())) {

            TopBar()
            CardDetailsInput(paymentCardViewModel = paymentCardViewModel, paymentCardUiState = paymentCardUiState)

        }
    }

    @Composable
    private fun TopBar() {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(color = MaterialTheme.colors.primary)) {

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .fillMaxHeight()
                .wrapContentWidth()) {
                IconButton(
                    onClick = {
                        onBackPressedDispatcher.onBackPressed()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back", tint = Color.White
                    )
                }
                Text(text = "Parent Title", textAlign = TextAlign.Center, color = Color.White, style = MaterialTheme.typography.h3)
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()) {

                Text(text = "Title", style = MaterialTheme.typography.h1, textAlign = TextAlign.Center, color = Color.White)
            }
        }
    }

    @Composable
    private fun CardDetailsInput(paymentCardViewModel: PaymentCardViewModel, paymentCardUiState: PaymentCardUiState) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

            Spacer(modifier = Modifier.size(16.dp))

            CardDetailsInputField(inputType = PaymentCardInputType.NUMBER, paymentCardUiState = paymentCardUiState, callback = {
                paymentCardViewModel.setCardNumber(it)
            })
            CardDetailsInputField(inputType = PaymentCardInputType.NAME, paymentCardUiState = paymentCardUiState, callback = {
                paymentCardViewModel.setNameOnCard(it)
            })
            CardDetailsInputField(inputType = PaymentCardInputType.NICKNAME, paymentCardUiState = paymentCardUiState, callback = {
                paymentCardViewModel.setCardNickname(it)
            })
            CardDetailsInputField(inputType = PaymentCardInputType.EXPIRY, paymentCardUiState = paymentCardUiState, callback = {
                paymentCardViewModel.setCardExpiry(it)
            })

        }
    }

    @Composable
    private fun CardDetailsInputField(inputType: PaymentCardInputType, paymentCardUiState: PaymentCardUiState, callback: (String) -> Unit) {
        val hint = when (inputType) {
            PaymentCardInputType.NUMBER -> {
                "Card Number"
            }
            PaymentCardInputType.NAME -> {
                "Name on Card"
            }
            PaymentCardInputType.NICKNAME -> {
                "Card Nickname"
            }
            PaymentCardInputType.EXPIRY -> {
                "Card Expiry"
            }
        }

        TextField(
            value = when (inputType) {
                PaymentCardInputType.NUMBER -> {
                    paymentCardUiState.cardNumber
                }
                PaymentCardInputType.NAME -> {
                    paymentCardUiState.nameOnCard
                }
                PaymentCardInputType.NICKNAME -> {
                    paymentCardUiState.cardNickname
                }
                PaymentCardInputType.EXPIRY -> {
                    paymentCardUiState.cardExpiry
                }
            },
            singleLine = true,
            onValueChange = {
                callback(it)
            },
            shape = RoundedCornerShape(8.dp),

            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            label = {
                Text(
                    text = hint,
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    style = TextStyle(
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp
                    ),
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
    }


//    @Composable
//    fun Greeting(paymentCardViewModel: PaymentCardViewModel) {
//        Text(text = "BinkPayments SDK")
//        val paymentAccount = getPaymentAccount()
//        paymentCardViewModel.sendPaymentCardToSpreedly("5555 5555 5555 4444", paymentAccount, "1Lf7DiKgkcx5Anw7QxWdDxaKtTa")
//    }

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