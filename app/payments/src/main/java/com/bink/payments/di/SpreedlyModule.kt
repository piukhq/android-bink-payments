package com.bink.payments.di

import com.bink.payments.data.SpreedlyService
import com.bink.payments.utils.SPREEDLY_OKHTTP
import com.bink.payments.utils.SPREEDLY_RETROFIT
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val spreedlyModule = module {
    single(named(SPREEDLY_OKHTTP)) { provideSpreedlyOkHttpClient() }
    single(named(SPREEDLY_RETROFIT)) { provideRetrofit(get(named(SPREEDLY_OKHTTP))) }
    single { provideSpreedlyService(get(named(SPREEDLY_RETROFIT))) }
}

fun provideSpreedlyOkHttpClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY

    val headerAuthorizationInterceptor = Interceptor { chain ->
        val request = chain.request().url.newBuilder().build()

        val newRequest = chain.request().newBuilder()
            .header("Content-Type", "application/json")
            .url(request)
            .build()
        val response = chain.proceed(newRequest)
        response
    }

    return OkHttpClient.Builder()
        .addInterceptor(headerAuthorizationInterceptor)
        .addInterceptor(interceptor).build()
}

fun provideSpreedlyService(retrofit: Retrofit): SpreedlyService =
    retrofit.create(SpreedlyService::class.java)