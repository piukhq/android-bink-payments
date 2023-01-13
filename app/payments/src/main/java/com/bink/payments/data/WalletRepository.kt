package com.bink.payments.data

import com.bink.payments.model.wallet.UserWallet

class WalletRepository(
    private val apiService: ApiService,
) {
    suspend fun getWallet(): UserWallet = apiService.getWallet()
}