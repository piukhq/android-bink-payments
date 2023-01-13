package com.bink.payments.model.wallet


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Transaction(
    @Json(name = "description")
    val description: String?,
    @Json(name = "display_value")
    val displayValue: String?,
    @Json(name = "id")
    val id: String?,
    @Json(name = "timestamp")
    val timestamp: Int?,
)