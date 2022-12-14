package com.bink.payments.screens

data class PaymentCardUiState(
    val binkPaymentsOptions: BinkPaymentsOptions = BinkPaymentsActivity.defaultBinkPaymentsOptions,
    val cardNumber: String = "",
    val cardExpiry: String = "",
    val nameOnCard: String = "",
    val cardNickname: String = "",
)

enum class PaymentCardInputType {
    NUMBER,
    EXPIRY,
    NAME,
    NICKNAME
}