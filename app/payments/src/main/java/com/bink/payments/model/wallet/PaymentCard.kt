package com.bink.payments.model.wallet


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PaymentCard(
    @Json(name = "card_nickname")
    val cardNickname: String?,
    @Json(name = "expiry_month")
    val expiryMonth: String?,
    @Json(name = "expiry_year")
    val expiryYear: String?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "name_on_card")
    val nameOnCard: String?,
    @Json(name = "pll_links")
    val pllLinks: List<PaymentCardPllLink>?,
    @Json(name = "status")
    val status: String?,
    @Json(name = "last_four_digits")
    val lastFourDigits: Int?,
    @Json(name = "provider")
    val provider: String?,
)