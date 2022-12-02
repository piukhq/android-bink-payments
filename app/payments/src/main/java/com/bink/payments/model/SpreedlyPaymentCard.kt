package com.bink.payments.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SpreedlyPaymentCard(
    val payment_method: SpreedlyPaymentMethod
)

@JsonClass(generateAdapter = true)
data class SpreedlyPaymentMethod(
    val credit_card: SpreedlyCreditCard,
    val retained: String
)

@JsonClass(generateAdapter = true)
data class SpreedlyCreditCard(
    val number: String?,
    val month: String?,
    val year: String?,
    val full_name: String?
)
