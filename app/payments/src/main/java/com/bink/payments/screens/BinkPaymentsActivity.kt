package com.bink.payments.screens

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bink.core.PaymentAccountUtil
import com.bink.payments.*
import com.bink.payments.model.PaymentAccount
import com.bink.payments.ui.BinkPaymentsTheme
import com.bink.payments.utils.EXPIRY_YEAR
import com.bink.payments.viewmodel.PaymentCardViewModel
import org.koin.android.ext.android.inject

class BinkPaymentsActivity : ComponentActivity() {

    companion object {
        const val binkPaymentsOptionsName = "binkPaymentsOptions"
        val defaultBinkPaymentsOptions = BinkPaymentsOptions()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val paymentCardViewModel by inject<PaymentCardViewModel>()

        getUiOptions()?.let { paymentCardViewModel.setUiOptions(it) }

        setContent {
            val paymentCardUiState by paymentCardViewModel.uiState.collectAsState()

            BinkPaymentsTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = paymentCardUiState.binkPaymentsOptions.backgroundColor) {
                    CreatePaymentCardScreen(paymentCardViewModel = paymentCardViewModel, paymentCardUiState = paymentCardUiState)
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun getUiOptions(): BinkPaymentsOptions? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(binkPaymentsOptionsName, BinkPaymentsOptions::class.java)
        } else {
            intent.getSerializableExtra(binkPaymentsOptionsName) as? BinkPaymentsOptions
        }
    }

    @Composable
    private fun CreatePaymentCardScreen(paymentCardViewModel: PaymentCardViewModel, paymentCardUiState: PaymentCardUiState) {

        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())) {

            TopBar(paymentCardUiState = paymentCardUiState)
            CardDetailsInput(paymentCardViewModel = paymentCardViewModel, paymentCardUiState = paymentCardUiState)

        }
    }

    @Composable
    private fun TopBar(paymentCardUiState: PaymentCardUiState) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(color = paymentCardUiState.binkPaymentsOptions.toolBarOptions.toolBarColor)) {

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .fillMaxHeight()
                .wrapContentWidth()) {
                IconButton(
                    onClick = {
                        onBackPressedDispatcher.onBackPressed()
                    },
                ) {
                    Icon(
                        imageVector = paymentCardUiState.binkPaymentsOptions.toolBarOptions.backButtonIcon,
                        contentDescription = "Back", tint = Color.White
                    )
                }
                Text(text = paymentCardUiState.binkPaymentsOptions.toolBarOptions.backButtonTitle,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    style = TextStyle(
                        fontFamily = paymentCardUiState.binkPaymentsOptions.font,
                        fontSize = 16.sp
                    ))
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()) {

                Text(text = paymentCardUiState.binkPaymentsOptions.toolBarOptions.toolBarTitle,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    style = TextStyle(
                        fontFamily = paymentCardUiState.binkPaymentsOptions.font,
                        fontSize = 22.sp
                    ))
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


//            Button(onClick = {
//                paymentCardViewModel.sendPaymentCardToSpreedly("5555 5555 5555 4444", paymentAccount, "1Lf7DiKgkcx5Anw7QxWdDxaKtTa")
//            }) {
//
//            }

        }
    }

    @Composable
    private fun CardDetailsInputField(inputType: PaymentCardInputType, paymentCardUiState: PaymentCardUiState, callback: (String) -> Unit) {
        val uiOptions = paymentCardUiState.binkPaymentsOptions.inputFieldOptions
        val isUppercaseHints = uiOptions.upperCaseHints

        val hint = when (inputType) {
            PaymentCardInputType.NUMBER -> {
                if (!isUppercaseHints) "Card Number" else "CARD NUMBER"
            }
            PaymentCardInputType.NAME -> {
                if (!isUppercaseHints) "Name on Card" else "NAME ON CARD"

            }
            PaymentCardInputType.NICKNAME -> {
                if (!isUppercaseHints) "Card Nickname" else "CARD NICKNAME"

            }
            PaymentCardInputType.EXPIRY -> {
                if (!isUppercaseHints) "Card Expiry" else "CARD EXPIRY"
            }
        }

        val textFieldValue = when (inputType) {
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
        }

        val textFieldColours = if (uiOptions.borderStyle == InputFieldBorderStyle.BOX) {
            TextFieldDefaults.textFieldColors(
                backgroundColor = uiOptions.backgroundColor,
                textColor = uiOptions.textColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        } else {
            TextFieldDefaults.textFieldColors(
                backgroundColor = uiOptions.backgroundColor,
                textColor = uiOptions.textColor,
                focusedIndicatorColor = uiOptions.borderColor,
                unfocusedIndicatorColor = uiOptions.borderColor
            )
        }

        val textFieldModifier = if (uiOptions.borderStyle == InputFieldBorderStyle.BOX) {
            Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .border(width = uiOptions.borderWidth.dp, color = uiOptions.borderColor, shape = RoundedCornerShape(8.dp))
        } else {
            Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        }

        if (uiOptions.hintStyle == InputFieldHintStyle.HEADER) {
            Text(
                text = hint,
                textAlign = TextAlign.Left,
                color = uiOptions.hintTextColor,
                style = TextStyle(
                    fontFamily = paymentCardUiState.binkPaymentsOptions.font,
                    fontSize = 18.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )

            TextField(
                value = textFieldValue,
                singleLine = true,
                onValueChange = {
                    callback(it)
                },
                colors = textFieldColours,
                modifier = textFieldModifier
            )
        } else {
            TextField(
                value = textFieldValue,
                singleLine = true,
                onValueChange = {
                    callback(it)
                },
                colors = textFieldColours,
                label = {
                    if (uiOptions.hintStyle == InputFieldHintStyle.INLINE) {
                        Text(
                            text = hint,
                            textAlign = TextAlign.Center,
                            color = uiOptions.hintTextColor,
                            style = TextStyle(
                                fontFamily = paymentCardUiState.binkPaymentsOptions.font,
                                fontWeight = FontWeight.Light,
                                fontSize = 14.sp
                            ),
                        )
                    }
                },
                modifier = textFieldModifier
            )
        }


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