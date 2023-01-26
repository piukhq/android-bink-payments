package com.bink.core

import com.bink.core.PaymentAccountUtil.ccSanitize
import com.bink.core.PaymentAccountUtil.numberSanitize
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import kotlin.random.Random

object PaymentAccountUtil {

    private const val ENCRYPTION_TYPE_MD5 = "MD5"
    private const val ENCRYPTION_PAD_CHAR = '0'
    private const val ENCRYPTION_SIGN_NUM = 1
    private const val ENCRYPTION_RADIX = 16
    private const val ENCRYPTION_LENGTH = 32

    private const val SEPARATOR_PIPE = "|"
    private const val SEPARATOR_HYPHEN = "-"
    private const val SEPARATOR_SLASH = "/"
    private const val REGEX_DECIMAL_OR_SLASH = "[^\\d/]"
    const val REGEX_DECIMAL_ONLY = "[^\\d]"
    const val EMPTY_STRING = ""
    const val SPACE = " "
    const val DIGITS_VISA_MASTERCARD = 16
    const val DIGITS_AMERICAN_EXPRESS = 15

    fun generateToken(length: Int): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("");
    }

    fun fingerprintGenerator(pan: String, expiryYear: String, expiryMonth: String): String {
        // Based a hash of the pan, it's the key identifier of the card
        return "$pan|$expiryMonth|$expiryYear".md5()
    }

    private fun String.md5(): String {
        val md = MessageDigest.getInstance(ENCRYPTION_TYPE_MD5)
        return BigInteger(ENCRYPTION_SIGN_NUM, md.digest(toByteArray()))
            .toString(ENCRYPTION_RADIX)
            .padStart(ENCRYPTION_LENGTH, ENCRYPTION_PAD_CHAR)
    }

    fun cardValidation(value: String): PaymentCardType {
        if (!value.isValidLuhnFormat()) {
            return PaymentCardType.NONE
        }
        val sanitizedInput = ccSanitize(value)
        val paymentType = presentedCardType(sanitizedInput)
        return if (sanitizedInput.length == paymentType.len &&
            sanitizedInput.isValidLuhnFormat()
        ) {
            paymentType
        } else {
            PaymentCardType.NONE
        }
    }

    fun presentedCardType(value: String): PaymentCardType {
        val sanitizedInput = numberSanitize(value)
        if (sanitizedInput.isEmpty()) {
            return PaymentCardType.NONE
        }
        PaymentCardType.values().forEach {
            if (it != PaymentCardType.NONE &&
                it.len >= sanitizedInput.length
            ) {
                val splits = it.prefix.split(SEPARATOR_PIPE)
                for (prefix in splits) {
                    if (splits.size <= 1 ||
                        prefix.length != 1 ||
                        sanitizedInput.length <= prefix.length
                    ) {
                        val range = prefix.split(SEPARATOR_HYPHEN)
                        if (range.size > 1 &&
                            sanitizedInput >= range[0] &&
                            sanitizedInput <= range[1]
                        ) {
                            return it
                        } else if (sanitizedInput.length >= prefix.length &&
                            sanitizedInput.substring(0, prefix.length) == prefix
                        ) {
                            return it
                        }
                    }
                }
            }
        }
        return PaymentCardType.NONE
    }

    fun dateValidation(value: String): Boolean {
        val new = formatDate(value)
        if (new.isNotEmpty()) {
            val split = new.split(SEPARATOR_SLASH)
            if (split.size > 1 &&
                split[0].isNotBlank() &&
                split[1].isNotBlank()
            ) {
                val month = split[0].toInt()
                val year = split[1].toInt() + 2000
                if (month < 1 ||
                    month > 12
                ) {
                    return false
                }
                val cal = Calendar.getInstance()
                // presuming that a card can't expire more than 10 years in the future
                // the average expiry is about 3 years, but giving more in case
                if (year < cal.get(Calendar.YEAR) ||
                    year > cal.get(Calendar.YEAR) + 10
                ) {
                    return false
                } else if (year == cal.get(Calendar.YEAR) &&
                    month <= cal.get(Calendar.MONTH)
                ) {
                    return false
                }
                return true
            }
        }
        return false
    }

    fun formatDate(value: String): String {
        val builder = StringBuilder()
        try {
            val new = value.replace(REGEX_DECIMAL_OR_SLASH.toRegex(), EMPTY_STRING)
            if (new.isNotEmpty()) {
                val parts = new.split(SEPARATOR_SLASH)
                val year: String
                var month: String
                if (parts.size == 1) {
                    val len = kotlin.math.max(0, value.length - 2)
                    month = new.substring(0, len)
                    year = new.substring(len)
                } else {
                    month = parts[0]
                    year = parts[1]
                }
                month = "00$month"
                builder.append(month.substring(month.length - 2))
                builder.append(SEPARATOR_SLASH)
                builder.append(year)
            }
            return builder.toString()
        } catch (e: StringIndexOutOfBoundsException) {
            return ""
        }
    }

    fun numberSanitize(value: String): String = value.replace(REGEX_DECIMAL_ONLY.toRegex(), EMPTY_STRING)

    fun ccSanitize(value: String) = value.replace(SPACE, EMPTY_STRING)

}


fun String.isValidLuhnFormat(): Boolean {
    val sanitizedInput = ccSanitize(this)
    return when {
        sanitizedInput != numberSanitize(this) -> false
        sanitizedInput.luhnLengthInvalid() -> false
        sanitizedInput.luhnValidPopulated() -> sanitizedInput.luhnChecksum() % 10 == 0
        else -> false
    }
}

private fun String.luhnValidPopulated() = all(Char::isDigit) && length > 1

private fun String.luhnLengthInvalid() =
    !(length == PaymentAccountUtil.DIGITS_VISA_MASTERCARD ||
            length == PaymentAccountUtil.DIGITS_AMERICAN_EXPRESS)

private fun String.luhnChecksum() = luhnMultiply().sum()

private fun String.luhnMultiply() = digits().mapIndexed { i, j ->
    when {
        (length - i + 1) % 2 == 0 -> j
        j >= 5 -> j * 2 - 9
        else -> j * 2
    }
}

private fun String.digits() = map(Character::getNumericValue)

