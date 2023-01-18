package com.bink.payments.model.wallet

data class LoyaltyCardPllState(
    val linked: ArrayList<PaymentAccount>,
    val unlinked: ArrayList<PaymentAccount>,
    val timeChecked: Long,
)