package com.bink.payments.data

import com.bink.payments.model.PaymentAccountCreate
import com.bink.payments.model.wallet.UserWallet
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("payment_accounts")
    suspend fun addPaymentCard(@Body paymentAccount: PaymentAccountCreate): ResponseBody

    @GET("wallet")
    suspend fun getWallet(): UserWallet
}