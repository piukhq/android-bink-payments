package com.bink.payments.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bink.core.PaymentAccountUtil
import com.bink.core.PaymentCardType
import com.bink.payments.BinkLogger
import com.bink.payments.BinkPayments
import com.bink.payments.data.PaymentCardRepository
import com.bink.payments.model.PaymentAccount
import com.bink.payments.model.SpreedlyCreditCard
import com.bink.payments.model.SpreedlyPaymentCard
import com.bink.payments.model.SpreedlyPaymentMethod
import com.bink.payments.screens.BinkPaymentsOptions
import com.bink.payments.screens.PaymentCardUiState
import com.bink.payments.utils.EXPIRY_YEAR
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PaymentCardViewModel(private val addPaymentCardRepository: PaymentCardRepository) : ViewModel() {

    private var logger: BinkLogger = BinkPayments.getBinkLogger()

    private val _uiState = MutableStateFlow(PaymentCardUiState())
    val uiState: StateFlow<PaymentCardUiState> = _uiState.asStateFlow()

    init {
        logger.log(currentLogType = BinkLogger.LogType.DEBUG, message = "Payment Card ViewModel Initialized")
    }

    fun toggleErrorDialog() {
        _uiState.update { it.copy(showErrorDialog = !uiState.value.showErrorDialog) }
    }

    fun setUiOptions(binkPaymentsOptions: BinkPaymentsOptions) {
        _uiState.update { it.copy(binkPaymentsOptions = binkPaymentsOptions) }
    }

    fun setCardNumber(value: String) {
        _uiState.update {
            it.copy(cardNumber = value, cardNumberError = "")
        }
        if (PaymentAccountUtil.cardValidation(value) != PaymentCardType.NONE) {
            _uiState.update {
                it.copy(cardType = " (${PaymentAccountUtil.presentedCardType(value).name})")
            }
        } else {
            _uiState.update {
                it.copy(cardType = "")
            }
        }
    }

    fun checkCardNumber(value: String, isFocused: Boolean) {
        if (isFocused) {
            _uiState.update {
                it.copy(cardNumberError = "")
            }
            if (PaymentAccountUtil.cardValidation(value) != PaymentCardType.NONE) {
                _uiState.update {
                    it.copy(cardType = " (${PaymentAccountUtil.presentedCardType(value).name})")
                }
            }
        } else {
            if (value.isNotBlank()) {
                if (PaymentAccountUtil.cardValidation(_uiState.value.cardNumber) == PaymentCardType.NONE) {
                    _uiState.update {
                        it.copy(cardNumberError = "Invalid Card Number", cardType = "")
                    }
                }
            }
        }
    }

    fun setNameOnCard(value: String) {
        _uiState.update { it.copy(nameOnCard = value, nameOnCardError = "") }
    }

    fun checkNameOnCard(isFocused: Boolean) {
        if (isFocused) {
            _uiState.update {
                it.copy(nameOnCardError = "")
            }
        }
    }

    fun setCardExpiry(value: String) {
        //Only add a "/" if the user is adding a new number. Not when they're deleting.

        val formattedValue = if (value.length == 2 && value.length > _uiState.value.cardExpiry.length) {
            "$value/"
        } else {
            value
        }

        try {
            formattedValue.replace("/", "").toInt()

            if (formattedValue.length < 5) {
                _uiState.update { it.copy(cardExpiry = formattedValue, cardExpiryError = "") }
            }

            if (formattedValue.length == 5) {
                _uiState.update { it.copy(cardExpiry = formattedValue, cardExpiryError = if (!PaymentAccountUtil.dateValidation(formattedValue)) "Invalid Expiry Date" else "") }
            }

        } catch (e: Exception) {
            if (value.isBlank()) {
                _uiState.update { it.copy(cardExpiry = value, cardExpiryError = "") }
            } else {
                logger.log(currentLogType = BinkLogger.LogType.DEBUG, message = "Only numbers are required, $e")
                //User has not inputted a number.
            }
        }
    }

    fun checkCardExpiry(value: String, isFocused: Boolean) {
        if (isFocused) {
            _uiState.update {
                it.copy(cardExpiryError = "")
            }
        } else {
            if (value.isNotBlank()) {
                _uiState.update {
                    it.copy(cardExpiryError = if (!PaymentAccountUtil.dateValidation(value)) "Invalid Expiry Date" else "")
                }
            }
        }
    }

    private fun getPaymentAccount(): PaymentAccount? {
        var hasError = false

        if (PaymentAccountUtil.cardValidation(uiState.value.cardNumber) == PaymentCardType.NONE) {
            _uiState.update {
                it.copy(cardNumberError = "Invalid Card Number")
            }

            logger.log(currentLogType = BinkLogger.LogType.DEBUG, message = "Invalid Card Number")

            hasError = true
        }

        if (uiState.value.nameOnCard.isBlank()) {
            _uiState.update {
                it.copy(nameOnCardError = "Invalid Invalid Card Name")
            }

            logger.log(currentLogType = BinkLogger.LogType.DEBUG, message = "Invalid Card Name")

            hasError = true
        }

        if (!PaymentAccountUtil.dateValidation(uiState.value.cardExpiry)) {
            _uiState.update {
                it.copy(cardExpiryError = "Invalid Expiry Date")
            }

            logger.log(currentLogType = BinkLogger.LogType.DEBUG, message = "Invalid Expiry Date")

            hasError = true
        }

        if (hasError) {
            return null
        }

        val cardNumber = uiState.value.cardNumber
        val nameOnCard = uiState.value.nameOnCard
        val cardExpiry = uiState.value.cardExpiry.split("/")

        return PaymentAccount(
            country = "GB",
            currencyCode = "GBP",
            expiryMonth = cardExpiry[0],
            expiryYear = (cardExpiry[1].toInt() + EXPIRY_YEAR).toString(),
            fingerprint = PaymentAccount.fingerprintGenerator(
                cardNumber,
                cardExpiry[0],
                cardExpiry[1]
            ),
            firstSixDigits = cardNumber.substring(0, 6),
            lastFourDigits = cardNumber.substring(cardNumber.length - 4),
            nameOnCard = nameOnCard,
            token = PaymentAccount.tokenGenerator()
        )
    }

    /**
     * Tokenize payment card with spreedly and send it to the Bink API.
     *
     * @param spreedlyEnvironmentKey: The key required to use the Spreedly API.
     */
    fun sendPaymentCardToSpreedly(spreedlyEnvironmentKey: String) {
        logger.log(currentLogType = BinkLogger.LogType.DEBUG, message = "Sending payment card to spreedly")

        getPaymentAccount()?.let { paymentAccount ->
            val spreedlyCreditCard = SpreedlyCreditCard(
                uiState.value.cardNumber,
                paymentAccount.expiryMonth,
                paymentAccount.expiryYear,
                paymentAccount.nameOnCard
            )

            val spreedlyPaymentMethod = SpreedlyPaymentMethod(spreedlyCreditCard, "true")
            val spreedlyPaymentCard = SpreedlyPaymentCard(spreedlyPaymentMethod)

            viewModelScope.launch {
                try {
                    val spreedlyResponse = addPaymentCardRepository.sendPaymentCardToSpreedly(
                        spreedlyPaymentCard,
                        spreedlyEnvironmentKey
                    )

                    spreedlyResponse.let { response ->

                        logger.log(currentLogType = BinkLogger.LogType.DEBUG, message = "Card successfully tokenized with spreedly")

                        paymentAccount.apply {
                            token = response.transaction.payment_method.token
                            fingerprint = response.transaction.payment_method.fingerprint
                            firstSixDigits = response.transaction.payment_method.first_six_digits
                            lastFourDigits = response.transaction.payment_method.last_four_digits
                        }

                        addPaymentCardRepository.addPaymentCard(paymentAccount)

                        logger.log(currentLogType = BinkLogger.LogType.DEBUG, message = "Payment card ${paymentAccount.nameOnCard} successfully added")
                    }
                } catch (e: Exception) {
                    logger.log(currentLogType = BinkLogger.LogType.ERROR, message = "${e.message}")
                    toggleErrorDialog()
                }
            }
        }
    }

}