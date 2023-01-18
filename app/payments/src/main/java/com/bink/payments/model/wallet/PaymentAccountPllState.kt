package com.bink.payments.model.wallet

data class PaymentAccountPllState(
    val linked: ArrayList<LoyaltyCard>,
    val unlinked: ArrayList<LoyaltyCard>,
    val timeChecked: Long,
)