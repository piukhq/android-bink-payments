package com.bink.payments.model.trustedcards


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TrustedCardAdd(
    @Json(name = "account")
    val account: Account,
    @Json(name = "loyalty_plan_id")
    val loyaltyPlanId: Int
)