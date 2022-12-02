package com.bink.payments.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SpreedlyResponse(
    val transaction: SpreedlyTransaction
)

@JsonClass(generateAdapter = true)
data class SpreedlyTransaction(
    val payment_method: SpreedlyResponsePaymentMethod
)

@JsonClass(generateAdapter = true)
data class SpreedlyResponsePaymentMethod(
    val token: String,
    val fingerprint: String,
    val first_six_digits: String,
    val last_four_digits: String
)