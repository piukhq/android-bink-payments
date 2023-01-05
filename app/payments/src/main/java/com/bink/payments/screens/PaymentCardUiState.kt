package com.bink.payments.screens

data class PaymentCardUiState(
    val binkPaymentsOptions: BinkPaymentsOptions = BinkPaymentsActivity.defaultBinkPaymentsOptions,
    val cardNumber: String = "",
    val cardType: String = "",
    val cardExpiry: String = "",
    val nameOnCard: String = "",
    val cardNumberError: String = "",
    val cardExpiryError: String = "",
    val nameOnCardError: String = "",
    val showErrorDialog: Boolean = false,
)

enum class PaymentCardInputType {
    NUMBER,
    EXPIRY,
    NAME,
}