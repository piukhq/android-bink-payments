package com.bink.payments.data

import com.bink.payments.model.PaymentAccount
import com.bink.payments.model.SpreedlyPaymentCard
import com.bink.payments.model.SpreedlyResponse
import okhttp3.ResponseBody

class PaymentCardRepository(
    private val apiService: ApiService,
    private val spreedlyService: SpreedlyService,
) {

    suspend fun addPaymentCard(paymentAccount: PaymentAccount): ResponseBody {
        return apiService.addPaymentCard(paymentAccount)
    }

    suspend fun sendPaymentCardToSpreedly(
        spreedlyCard: SpreedlyPaymentCard,
        environmentKey: String,
    ): SpreedlyResponse {
        return spreedlyService.postPaymentCardToSpreedly(spreedlyCard, environmentKey)
    }
}