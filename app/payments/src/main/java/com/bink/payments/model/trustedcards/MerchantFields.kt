package com.bink.payments.model.trustedcards

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MerchantFields(
    @Json(name = "account_id")
    val accountId: String,
)