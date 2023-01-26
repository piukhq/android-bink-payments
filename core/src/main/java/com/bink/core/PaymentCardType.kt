package com.bink.core

enum class PaymentCardType(
    val type: String, val len: Int, val prefix: String, val format: String
) {
    NONE(
        "NONE", 16, "", "0000000000000000"
    ),
    AMEX(
        "American Express", 15, "34|37", "0000 000000 00000"
    ),
    VISA(
        "Visa", 16, "4", "0000 0000 0000 0000"
    ),
    MASTERCARD(
        "Mastercard", 16, "51-5599999999999999", "0000 0000 0000 0000"
    ),
    MASTERCARD_BIN(
        "Mastercard", 16, "2221-2720999999999999", "0000 0000 0000 0000"
    )
}
