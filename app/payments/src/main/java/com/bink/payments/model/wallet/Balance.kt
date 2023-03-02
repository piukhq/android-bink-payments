package com.bink.payments.model.wallet


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Balance(
    @Json(name = "current_display_value")
    val currentDisplayValue: String?,
    @Json(name = "updated_at")
    val updatedAt: Int?,
)