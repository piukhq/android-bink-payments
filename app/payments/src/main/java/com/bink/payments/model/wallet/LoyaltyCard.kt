package com.bink.payments.model.wallet


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoyaltyCard(
    @Json(name = "balance")
    val balance: Balance?,
    @Json(name = "card")
    val card: Card?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "loyalty_plan_id")
    val loyaltyPlanId: Int?,
    @Json(name = "pll_links")
    val pllLinks: List<LoyaltyCardPllLink>?,
    @Json(name = "status")
    val status: Status?,
    @Json(name = "transactions")
    val transactions: List<Transaction>?,
    @Json(name = "vouchers")
    val vouchers: List<Voucher>?,
)