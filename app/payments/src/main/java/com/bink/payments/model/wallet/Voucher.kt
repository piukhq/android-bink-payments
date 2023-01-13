package com.bink.payments.model.wallet


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Voucher(
    @Json(name = "barcode_type")
    val barcodeType: Int?,
    @Json(name = "body_text")
    val bodyText: String?,
    @Json(name = "earn_type")
    val earnType: String?,
    @Json(name = "expiry_date")
    val expiryDate: Long?,
    @Json(name = "headline")
    val headline: String?,
    @Json(name = "issued_date")
    val issuedDate: Long?,
    @Json(name = "progress_display_text")
    val progressDisplayText: String?,
    @Json(name = "redeemed_date")
    val redeemedDate: Long?,
    @Json(name = "reward_text")
    val rewardText: String?,
    @Json(name = "state")
    val state: String?,
    @Json(name = "terms_and_conditions")
    val termsAndConditions: String?,
    @Json(name = "voucher_code")
    val voucherCode: String?,
)