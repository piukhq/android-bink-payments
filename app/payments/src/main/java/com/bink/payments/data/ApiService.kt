package com.bink.payments.data

import com.bink.payments.model.PaymentAccount
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("payment_accounts")
    suspend fun addPaymentCard(@Body paymentAccount: PaymentAccount): ResponseBody
}