package com.bink.payments.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bink.payments.data.WalletRepository
import kotlinx.coroutines.launch

class BinkPaymentViewModel(private val walletRepository: WalletRepository) : ViewModel() {

    fun getWallet() {
        viewModelScope.launch {
            try {
                val wallet = walletRepository.getWallet()
            } catch (e: Exception) {
            }
        }
    }

}