package com.bink.payments.model.wallet


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Join(
    @Json(name = "loyalty_card_id")
    val loyaltyCardId: Int?,
    @Json(name = "loyalty_plan_id")
    val loyaltyPlanId: Int?,
    @Json(name = "status")
    val status: Status?,
)