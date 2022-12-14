package com.bink.payments.data

import com.bink.payments.model.SpreedlyPaymentCard
import com.bink.payments.model.SpreedlyResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface SpreedlyService {

    @POST("https://core.spreedly.com/v1/payment_methods.json")
    suspend fun postPaymentCardToSpreedly(
        @Body spreedlyCard: SpreedlyPaymentCard,
        @Query("environment_key") environmentKey: String,
    ): SpreedlyResponse

}