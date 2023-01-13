package com.bink.payments.model.wallet


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoyaltyCardPllLink(
    @Json(name = "payment_account_id")
    val paymentAccountId: Int?,
    @Json(name = "payment_scheme")
    val paymentScheme: String?,
    @Json(name = "status")
    val status: Status?,
)