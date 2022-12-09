package com.bink.payments.model


import com.bink.payments.utils.StringUtils
import com.bink.payments.utils.StringUtils.md5
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PaymentAccount(
    @Json(name = "card_nickname")
    val cardNickname: String?,
    @Json(name = "country")
    val country: String,
    @Json(name = "currency_code")
    val currencyCode: String,
    @Json(name = "expiry_month")
    val expiryMonth: String,
    @Json(name = "expiry_year")
    val expiryYear: String,
    @Json(name = "fingerprint")
    var fingerprint: String,
    @Json(name = "first_six_digits")
    var firstSixDigits: String,
    @Json(name = "last_four_digits")
    var lastFourDigits: String,
    @Json(name = "name_on_card")
    val nameOnCard: String,
    @Json(name = "token")
    var token: String,
) {
    companion object {
        const val TOKEN_LENGTH = 100

        fun tokenGenerator(): String {
            return StringUtils.randomString(TOKEN_LENGTH)
        }

        fun fingerprintGenerator(pan: String, expiryYear: String, expiryMonth: String): String {
            // Based a hash of the pan, it's the key identifier of the card
            return "$pan|$expiryMonth|$expiryYear".md5()
        }
    }

}