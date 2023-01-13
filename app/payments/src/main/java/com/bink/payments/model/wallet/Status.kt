package com.bink.payments.model.wallet


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Status(
    @Json(name = "description")
    val description: String?,
    @Json(name = "slug")
    val slug: String?,
    @Json(name = "state")
    val state: String?,
)