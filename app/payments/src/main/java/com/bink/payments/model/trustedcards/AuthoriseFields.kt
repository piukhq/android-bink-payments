package com.bink.payments.model.trustedcards


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthoriseFields(
    @Json(name = "credentials")
    val credentials: List<Credential>,
)