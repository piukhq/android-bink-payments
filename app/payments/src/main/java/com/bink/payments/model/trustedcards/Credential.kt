package com.bink.payments.model.trustedcards


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Credential(
    @Json(name = "credential_slug")
    val credentialSlug: String,
    @Json(name = "value")
    val value: String
)