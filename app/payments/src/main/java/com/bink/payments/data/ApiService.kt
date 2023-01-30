package com.bink.payments.data

import com.bink.payments.model.PaymentAccountCreate
import com.bink.payments.model.trustedcards.TrustedCardAdd
import com.bink.payments.model.trustedcards.TrustedCardReplace
import com.bink.payments.model.wallet.UserWallet
import okhttp3.ResponseBody
import retrofit2.http.*

interface ApiService {
    @POST("payment_accounts")
    suspend fun addPaymentCard(@Body paymentAccount: PaymentAccountCreate): ResponseBody

    @GET("wallet")
    suspend fun getWallet(): UserWallet

    @POST("loyalty_cards/add_trusted")
    suspend fun addTrustedLoyaltyCard(@Body trustedCardAdd: TrustedCardAdd): ResponseBody

    @PUT("loyalty_cards/(card_id)/add_trusted")
    suspend fun updateTrustedLoyaltyCard(
        @Path("card_id") cardId: String,
        @Body trustedCardReplace: TrustedCardReplace,
    ): ResponseBody 
}

