package com.bink.payments.model.wallet


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Card(
    @Json(name = "barcode")
    val barcode: String?,
    @Json(name = "barcode_type")
    val barcodeType: Int?,
    @Json(name = "card_number")
    val cardNumber: String?,
    @Json(name = "colour")
    val colour: String?,
)