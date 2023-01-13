package com.bink.payments.model.wallet


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PaymentCardPllLink(
    @Json(name = "loyalty_plan")
    val loyaltyPlan: String?,
    @Json(name = "loyalty_plan_id")
    val loyaltyPlanId: Int?,
    @Json(name = "status")
    val status: Status?,
)