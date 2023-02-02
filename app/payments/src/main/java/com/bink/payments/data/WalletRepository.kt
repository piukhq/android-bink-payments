package com.bink.payments.data

import com.bink.payments.model.trustedcards.TrustedCardAdd
import com.bink.payments.model.trustedcards.TrustedCardReplace
import com.bink.payments.model.wallet.UserWallet
import okhttp3.ResponseBody

class WalletRepository(
    private val apiService: ApiService,
) {
    suspend fun getWallet(): UserWallet = apiService.getWallet()

    suspend fun addTrustedLoyaltyCard(trustedCardAdd: TrustedCardAdd): ResponseBody {
        return apiService.addTrustedLoyaltyCard(trustedCardAdd)
    }

    suspend fun replaceTrustedLoyaltyCard(id: String, trustedCardReplace: TrustedCardReplace): ResponseBody {
        return apiService.replaceTrustedLoyaltyCard(id, trustedCardReplace)
    }
}