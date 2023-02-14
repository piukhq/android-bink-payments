package com.bink.payments.model.wallet

data class LoyaltyCardPllState(
    val linked: List<PaymentAccount>,
    val unlinked: List<PaymentAccount>,
    val timeChecked: Long,
)