package com.bink.payments.di

import com.bink.payments.data.ApiService
import com.bink.payments.utils.BASE_URL
import com.bink.payments.utils.BINK_PAYMENTS_OKHTTP
import com.bink.payments.utils.BINK_PAYMENTS_RETROFIT
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val networkModule = module {
    single(named(BINK_PAYMENTS_OKHTTP)) { provideDefaultOkHttpClient("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiIsImtpZCI6ImFjY2Vzcy1zZWNyZXQtMiJ9.eyJzdWIiOjM4MjgzLCJjaGFubmVsIjoiY29tLmJpbmsud2FsbGV0IiwiaWF0IjoxNjQ2NjQ3NDY0LCJleHAiOjE2NzgxODM0NjR9.yiF0v2Ufzj4eMTCyaR-q6NiX2KUUqSTe59OkUY5mM_Rdj1SWLv5rOw8h2ixgKJ_7JpZD-14qSu37-25UTKwSbQ") }
    single(named(BINK_PAYMENTS_RETROFIT)) { provideRetrofit(get(named(BINK_PAYMENTS_OKHTTP))) }
    single { provideApiService(get(named(BINK_PAYMENTS_RETROFIT))) }
}

fun provideDefaultOkHttpClient(userToken: String): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY

    val headerAuthorizationInterceptor = Interceptor { chain ->
        val request = chain.request().url.newBuilder().build()

        val newRequest = chain.request().newBuilder()
            .header("Content-Type", "application/json")
            .header("Authorization", " Bearer $userToken")
            .url(request)

        val response = chain.proceed(newRequest.build())
        response
    }

    return OkHttpClient.Builder()
        .addInterceptor(headerAuthorizationInterceptor)
        .addInterceptor(interceptor).build()
}

fun provideRetrofit(client: OkHttpClient): Retrofit {
    val retrofitBuilder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(client)

    return retrofitBuilder.build()
}

fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)