package com.bink.core

import junit.framework.Assert.*
import org.junit.Test

class PaymentAccountUtilTest {

    @Test
    fun testCcSanitizeShort() {
        assertEquals("12345678", PaymentAccountUtil.ccSanitize("1234 5678"))
    }

    @Test
    fun testCcSanitizeMedium() {
        assertEquals("123456789012", PaymentAccountUtil.ccSanitize("1234 5678 9012"))
    }

    @Test
    fun testSanitizeMedium() {
        assertEquals("123456789012", PaymentAccountUtil.numberSanitize("1234 5678 9012"))
    }

    @Test
    fun testSanitizeLetters() {
        assertEquals("1234", PaymentAccountUtil.numberSanitize("1234ABCD"))
    }

    @Test
    fun testLuhnEmptyCard() {
        assertFalse("".isValidLuhnFormat())
    }

    @Test
    fun singleDigitStringsCannotBeValid() {
        assertFalse("1".isValidLuhnFormat())
    }

    @Test
    fun singleZeroIsInvalid() {
        assertFalse("0".isValidLuhnFormat())
    }

    @Test
    fun invalidWithNonDigit() {
        assertFalse("055a 444 285".isValidLuhnFormat())
    }

    @Test
    fun invalidLength() {
        assertFalse("0555 1444 2285".isValidLuhnFormat())
    }

    @Test
    fun visaTooLong() {
        assertEquals(PaymentCardType.NONE, PaymentAccountUtil.cardValidation("4242 4242 4242 4242 4"))
    }

    @Test
    fun amexTooLong() {
        assertEquals(PaymentCardType.NONE, PaymentAccountUtil.cardValidation("3424 424242 424242 4"))
    }

    @Test
    fun invalidCard() {
        assertEquals(PaymentCardType.NONE, PaymentAccountUtil.cardValidation("1242 4242 4242 4242"))
    }

    @Test
    fun validVisa() {
        assertEquals(PaymentCardType.VISA, PaymentAccountUtil.cardValidation("4242 4242 4242 4242"))
    }

    @Test
    fun validMasterCard() {
        assertEquals(PaymentCardType.MASTERCARD, PaymentAccountUtil.cardValidation("5336 1653 2182 8811"))
    }

    @Test
    fun validAmEx() {
        assertEquals(PaymentCardType.AMEX, PaymentAccountUtil.cardValidation("3400 00000 000009"))
    }

    @Test
    fun checkPresentedVisa() {
        assertEquals(PaymentCardType.VISA, PaymentAccountUtil.presentedCardType("4242"))
    }

    @Test
    fun checkPresentedMasterCard() {
        assertEquals(PaymentCardType.MASTERCARD, PaymentAccountUtil.presentedCardType("51"))
    }

    @Test
    fun checkPresentedMasterCardBIN() {
        assertEquals(PaymentCardType.MASTERCARD_BIN, PaymentAccountUtil.presentedCardType("27"))
    }

    @Test
    fun checkPresentedAmEx() {
        assertEquals(PaymentCardType.AMEX, PaymentAccountUtil.presentedCardType("34"))
    }

    @Test
    fun checkPresentedAmExFail() {
        assertEquals(PaymentCardType.NONE, PaymentAccountUtil.presentedCardType("31"))
    }

    @Test
    fun emptyDateTest() {
        assertFalse(PaymentAccountUtil.dateValidation(""))
    }

    @Test
    fun garbageDateTest() {
        assertFalse(PaymentAccountUtil.dateValidation("abcde"))
    }

    @Test
    fun testSimpleDateFormat() {
        assertEquals("01/01", PaymentAccountUtil.formatDate("101"))
    }

    @Test
    fun testDateFormat() {
        assertEquals("11/21", PaymentAccountUtil.formatDate("11/21"))
    }

    @Test
    fun validateMonthTooSmall() {
        assertFalse(PaymentAccountUtil.dateValidation("00/20"))
    }

    @Test
    fun validateMonthTooLarge() {
        assertFalse(PaymentAccountUtil.dateValidation("13/20"))
    }

    @Test
    fun validateYearTooSmall() {
        assertFalse(PaymentAccountUtil.dateValidation("10/10"))
    }

    @Test
    fun validateYearTooLarge() {
        assertFalse(PaymentAccountUtil.dateValidation("10/40"))
    }

    @Test
    fun validateYearCorrect() {
        assertTrue(PaymentAccountUtil.dateValidation("12/23"))
    }

    @Test
    fun testMasterCardTwoLow() {
        assertEquals(PaymentAccountUtil.presentedCardType("2221"), PaymentCardType.MASTERCARD_BIN)
    }

    @Test
    fun testMasterCardTwoLowFail() {
        assertEquals(PaymentAccountUtil.presentedCardType("2220"), PaymentCardType.NONE)
    }

    @Test
    fun testMasterCardTwoHigh() {
        assertEquals(PaymentAccountUtil.presentedCardType("272099"), PaymentCardType.MASTERCARD_BIN)
    }

    @Test
    fun testMasterCardTwoHighFail() {
        assertEquals(PaymentAccountUtil.presentedCardType("2721"), PaymentCardType.NONE)
    }

    @Test
    fun testMasterCardTwoMid() {
        assertEquals(PaymentAccountUtil.presentedCardType("23"), PaymentCardType.MASTERCARD_BIN)
    }

    @Test
    fun testExpiryMissingYear() {
        assertFalse(PaymentAccountUtil.dateValidation("12/"))
    }

    @Test
    fun testExpiryMissingMonth() {
        assertFalse(PaymentAccountUtil.dateValidation("/19"))
    }

    @Test
    fun testExpiryMissingMonthAndYear() {
        assertFalse(PaymentAccountUtil.dateValidation("/"))
    }

}