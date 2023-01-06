package com.bink.payments.di

import com.bink.payments.data.ApiService
import com.bink.payments.data.PaymentCardRepository
import com.bink.payments.data.SpreedlyService
import com.bink.payments.viewmodel.PaymentCardViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    single {
        providePaymentCardRepository(get(), get())
    }

    viewModel { PaymentCardViewModel(get()) }
}

fun providePaymentCardRepository(
    apiService: ApiService,
    spreedlyService: SpreedlyService,
): PaymentCardRepository = PaymentCardRepository(apiService, spreedlyService)

