package com.bink.payments.model.trustedcards


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Account(
    @Json(name = "authorise_fields")
    val authoriseFields: AuthoriseFields,
    @Json(name = "merchant_fields")
    val merchantFields: MerchantFields,
)