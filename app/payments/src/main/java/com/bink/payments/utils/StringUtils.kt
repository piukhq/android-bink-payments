package com.bink.payments.utils

import java.math.BigInteger
import java.security.MessageDigest
import kotlin.random.Random

object StringUtils {
    private const val ENCRYPTION_TYPE_MD5 = "MD5"
    private const val ENCRYPTION_PAD_CHAR = '0'
    private const val ENCRYPTION_SIGN_NUM = 1
    private const val ENCRYPTION_RADIX = 16
    private const val ENCRYPTION_LENGTH = 32

    fun randomString(length: Int): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("");
    }

    fun String.md5(): String {
        val md = MessageDigest.getInstance(ENCRYPTION_TYPE_MD5)
        return BigInteger(ENCRYPTION_SIGN_NUM, md.digest(toByteArray()))
            .toString(ENCRYPTION_RADIX)
            .padStart(ENCRYPTION_LENGTH, ENCRYPTION_PAD_CHAR)
    }
}
