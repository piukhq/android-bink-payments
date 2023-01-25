package com.bink.payments.screens

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.bink.payments.*
import com.bink.payments.ui.BinkPaymentsTheme
import com.bink.payments.viewmodel.PaymentCardViewModel
import org.koin.android.ext.android.inject

class BinkPaymentsActivity : ComponentActivity() {

    companion object {
        const val binkPaymentsOptionsName = "binkPaymentsOptions"
        const val spreedlyEnvKey = "spreedlyEnvironmentKey"
        val defaultBinkPaymentsOptions = BinkPaymentsOptions()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val paymentCardViewModel by inject<PaymentCardViewModel>()

        getUiOptions()?.let { paymentCardViewModel.setUiOptions(it) }
        intent.getStringExtra(spreedlyEnvKey)
            ?.let { paymentCardViewModel.setSpreedlyEnvironmentKey(it) }

        setContent {
            val paymentCardUiState by paymentCardViewModel.uiState.collectAsState()

            BinkPaymentsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = paymentCardUiState.binkPaymentsOptions.backgroundColor.toColor()
                ) {
                    CreatePaymentCardScreen(
                        paymentCardViewModel = paymentCardViewModel,
                        paymentCardUiState = paymentCardUiState
                    )

                    if (paymentCardUiState.showErrorDialog) {
                        Dialog(onDismissRequest = { paymentCardViewModel.toggleErrorDialog() }) {
                            ErrorDialog(
                                paymentCardUiState = paymentCardUiState,
                                paymentCardViewModel = paymentCardViewModel
                            )
                        }
                    }
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun getUiOptions(): BinkPaymentsOptions? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(binkPaymentsOptionsName, BinkPaymentsOptions::class.java)
        } else {
            intent.getParcelableExtra(binkPaymentsOptionsName) as? BinkPaymentsOptions
        }
    }

    @Composable
    private fun CreatePaymentCardScreen(
        paymentCardViewModel: PaymentCardViewModel,
        paymentCardUiState: PaymentCardUiState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            TopBar(paymentCardUiState = paymentCardUiState)
            CardDetailsInput(
                paymentCardViewModel = paymentCardViewModel,
                paymentCardUiState = paymentCardUiState
            )
        }
    }

    @Composable
    private fun TopBar(paymentCardUiState: PaymentCardUiState) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(color = paymentCardUiState.binkPaymentsOptions.binkPaymentsTheme.primaryColor.toColor())
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentWidth()
            ) {
                IconButton(
                    onClick = {
                        onBackPressedDispatcher.onBackPressed()
                    },
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(
                            id = paymentCardUiState.binkPaymentsOptions.toolBarOptions.backButtonIcon
                        ),
                        contentDescription = "Back",
                        tint = paymentCardUiState.binkPaymentsOptions.binkPaymentsTheme.secondaryColor.toColor()
                    )
                }
                Text(
                    text = paymentCardUiState.binkPaymentsOptions.toolBarOptions.backButtonTitle,
                    textAlign = TextAlign.Center,

                    style = TextStyle(
                        fontFamily = paymentCardUiState.binkPaymentsOptions.font.fontFamily(),
                        fontSize = 16.sp,
                        color = paymentCardUiState.binkPaymentsOptions.binkPaymentsTheme.secondaryColor.toColor()
                    )
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {

                Text(
                    text = paymentCardUiState.binkPaymentsOptions.toolBarOptions.toolBarTitle,
                    style = TextStyle(
                        fontFamily = paymentCardUiState.binkPaymentsOptions.font.fontFamily(),
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center,
                        color = paymentCardUiState.binkPaymentsOptions.binkPaymentsTheme.secondaryColor.toColor()
                    )
                )

            }
        }
    }

    @Composable
    private fun CardDetailsInput(
        paymentCardViewModel: PaymentCardViewModel,
        paymentCardUiState: PaymentCardUiState
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Spacer(modifier = Modifier.size(16.dp))

            CardDetailsInputField(
                inputType = PaymentCardInputType.NUMBER,
                paymentCardUiState = paymentCardUiState,
                callback = { value, isFocused ->
                    value?.let {
                        paymentCardViewModel.setCardNumber(value)

                        isFocused?.let {
                            paymentCardViewModel.checkCardNumber(value, isFocused)
                        }
                    }
                })
            CardDetailsInputField(
                inputType = PaymentCardInputType.NAME,
                paymentCardUiState = paymentCardUiState,
                callback = { value, isFocused ->
                    value?.let {
                        paymentCardViewModel.setNameOnCard(value)

                        isFocused?.let {
                            paymentCardViewModel.checkNameOnCard(isFocused)
                        }
                    }
                })
            CardDetailsInputField(
                inputType = PaymentCardInputType.EXPIRY,
                paymentCardUiState = paymentCardUiState,
                callback = { value, isFocused ->
                    value?.let {
                        paymentCardViewModel.setCardExpiry(value)

                        isFocused?.let {
                            paymentCardViewModel.checkCardExpiry(value, isFocused)
                        }
                    }
                })

            ToggleBox(paymentCardUiState = paymentCardUiState)

            Button(
                onClick = {
                    paymentCardViewModel.sendPaymentCardToSpreedly()
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = paymentCardUiState.binkPaymentsOptions.binkPaymentsTheme.primaryColor.toColor())
            ) {
                Text(
                    text = "POST CARD",

                    style = TextStyle(
                        fontFamily = paymentCardUiState.binkPaymentsOptions.font.fontFamily(),
                        color = paymentCardUiState.binkPaymentsOptions.binkPaymentsTheme.secondaryColor.toColor()
                    )
                )
            }

        }
    }

    @Composable
    private fun ErrorDialog(
        paymentCardUiState: PaymentCardUiState,
        paymentCardViewModel: PaymentCardViewModel
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(
                    color = paymentCardUiState.binkPaymentsOptions.backgroundColor.toColor(),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(16.dp)
        ) {
            Text(
                text = "Error Adding Card",
                style = TextStyle(
                    fontFamily = paymentCardUiState.binkPaymentsOptions.font.fontFamily(),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .padding(bottom = 4.dp)
            )
            Text(
                text = "There was a problem adding your payment card, please try again.",
                style = TextStyle(
                    fontFamily = paymentCardUiState.binkPaymentsOptions.font.fontFamily(),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .padding(bottom = 4.dp)
            )
            Button(
                onClick = {
                    paymentCardViewModel.toggleErrorDialog()
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = paymentCardUiState.binkPaymentsOptions.binkPaymentsTheme.primaryColor.toColor())
            ) {
                Text(
                    text = "OK",

                    style = TextStyle(
                        fontFamily = paymentCardUiState.binkPaymentsOptions.font.fontFamily(),
                        color = paymentCardUiState.binkPaymentsOptions.binkPaymentsTheme.secondaryColor.toColor()
                    )
                )
            }
        }
    }

    @Composable
    private fun CardDetailsInputField(
        inputType: PaymentCardInputType,
        paymentCardUiState: PaymentCardUiState,
        callback: (String?, Boolean?) -> Unit
    ) {
        val uiOptions = paymentCardUiState.binkPaymentsOptions.inputFieldOptions
        val isUppercaseHints = uiOptions.upperCaseHints

        val hint = when (inputType) {
            PaymentCardInputType.NUMBER -> {
                if (!isUppercaseHints) "Card Number${paymentCardUiState.cardType}" else "CARD NUMBER${paymentCardUiState.cardType.uppercase()}"
            }
            PaymentCardInputType.NAME -> {
                if (!isUppercaseHints) "Name on Card" else "NAME ON CARD"

            }
            PaymentCardInputType.EXPIRY -> {
                if (!isUppercaseHints) "Card Expiry (MM/YY)" else "CARD EXPIRY (MM/YY)"
            }
        }

        val textFieldValue =
            when (inputType) {
                PaymentCardInputType.NUMBER -> {
                    TextFieldValue(
                        text = paymentCardUiState.cardNumber,
                        TextRange(paymentCardUiState.cardNumber.length)
                    )
                }
                PaymentCardInputType.NAME -> {
                    TextFieldValue(
                        text = paymentCardUiState.nameOnCard,
                        TextRange(paymentCardUiState.nameOnCard.length)
                    )
                }
                PaymentCardInputType.EXPIRY -> {
                    TextFieldValue(
                        text = paymentCardUiState.cardExpiry,
                        TextRange(paymentCardUiState.cardExpiry.length)
                    )
                }
            }


        val errorMessage = when (inputType) {
            PaymentCardInputType.NUMBER -> {
                paymentCardUiState.cardNumberError
            }
            PaymentCardInputType.NAME -> {
                paymentCardUiState.nameOnCardError
            }
            PaymentCardInputType.EXPIRY -> {
                paymentCardUiState.cardExpiryError
            }
        }

        val textFieldColours = if (uiOptions.borderStyle == InputFieldBorderStyle.BOX) {
            TextFieldDefaults.textFieldColors(
                backgroundColor = Color(uiOptions.backgroundColor),
                textColor = uiOptions.textColor.toColor(),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        } else {
            TextFieldDefaults.textFieldColors(
                backgroundColor = uiOptions.backgroundColor.toColor(),
                textColor = uiOptions.textColor.toColor(),
                focusedIndicatorColor = uiOptions.borderColor.toColor(),
                unfocusedIndicatorColor = uiOptions.borderColor.toColor()
            )
        }

        val textFieldModifier = if (uiOptions.borderStyle == InputFieldBorderStyle.BOX) {
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .border(
                    width = uiOptions.borderWidth.dp,
                    color = uiOptions.borderColor.toColor(),
                    shape = RoundedCornerShape(8.dp)
                )
                .onFocusChanged {
                    callback(textFieldValue.text, it.isFocused)
                }
        } else {
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .onFocusChanged {
                    callback(textFieldValue.text, it.isFocused)
                }
        }

        val keyboardOptions = if (inputType == PaymentCardInputType.NAME) {
            KeyboardOptions(keyboardType = KeyboardType.Text)
        } else {
            KeyboardOptions(keyboardType = KeyboardType.Number)
        }

        if (uiOptions.hintStyle == InputFieldHintStyle.HEADER) {
            Text(
                text = hint,
                textAlign = TextAlign.Left,
                style = TextStyle(
                    fontFamily = paymentCardUiState.binkPaymentsOptions.font.fontFamily(),
                    color = uiOptions.hintTextColor.toColor(),
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
                    callback(it.text, null)
                },
                colors = textFieldColours,
                textStyle = TextStyle(fontFamily = paymentCardUiState.binkPaymentsOptions.font.fontFamily()),
                keyboardOptions = keyboardOptions,
                modifier = textFieldModifier,
            )
        } else {
            TextField(
                value = textFieldValue,
                singleLine = true,
                onValueChange = {
                    callback(it.text, null)
                },
                colors = textFieldColours,
                textStyle = TextStyle(fontFamily = paymentCardUiState.binkPaymentsOptions.font.fontFamily()),
                label = {
                    if (uiOptions.hintStyle == InputFieldHintStyle.INLINE) {
                        Text(
                            text = hint,
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontFamily = paymentCardUiState.binkPaymentsOptions.font.fontFamily(),
                                color = uiOptions.hintTextColor.toColor(),
                                fontWeight = FontWeight.Light,
                                fontSize = 14.sp
                            ),
                        )
                    }
                },
                keyboardOptions = keyboardOptions,
                modifier = textFieldModifier,
            )
        }

        AnimatedVisibility(visible = errorMessage.isNotBlank()) {
            Text(
                text = errorMessage,
                textAlign = TextAlign.Left,
                style = TextStyle(
                    fontFamily = paymentCardUiState.binkPaymentsOptions.font.fontFamily(),
                    color = Color.Red,
                    fontSize = 16.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

        }

    }

    @Composable
    private fun ToggleBox(paymentCardUiState: PaymentCardUiState) {
        val checkedState = remember { mutableStateOf(true) }
        if (paymentCardUiState.binkPaymentsOptions.inputFieldOptions.checkBoxStyle == CheckBoxStyle.BOX) {
            Checkbox(
                checked = checkedState.value,
                onCheckedChange = { checkedState.value = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = paymentCardUiState.binkPaymentsOptions.binkPaymentsTheme.primaryColor.toColor(),
                    checkmarkColor = paymentCardUiState.binkPaymentsOptions.binkPaymentsTheme.secondaryColor.toColor(),
                    uncheckedColor = paymentCardUiState.binkPaymentsOptions.binkPaymentsTheme.primaryColor.toColor()
                        .copy(alpha = 0.5f)
                )
            )
        } else {
            Switch(
                checked = checkedState.value,
                onCheckedChange = { checkedState.value = it },
                colors = SwitchDefaults.colors(
                    checkedTrackColor = paymentCardUiState.binkPaymentsOptions.binkPaymentsTheme.primaryColor.toColor(),
                    checkedTrackAlpha = 1f,
                    checkedThumbColor = paymentCardUiState.binkPaymentsOptions.binkPaymentsTheme.secondaryColor.toColor(),
                    uncheckedThumbColor = paymentCardUiState.binkPaymentsOptions.binkPaymentsTheme.secondaryColor.toColor(),
                    uncheckedTrackColor = paymentCardUiState.binkPaymentsOptions.binkPaymentsTheme.primaryColor.toColor(),
                    uncheckedTrackAlpha = 0.5f
                )
            )
        }
    }

}