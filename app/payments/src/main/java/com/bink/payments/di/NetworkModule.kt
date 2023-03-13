package com.bink.payments.di

import android.content.Context
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.bink.payments.BinkPayments
import com.bink.payments.data.ApiService
import com.bink.payments.utils.BASE_URL
import com.bink.payments.utils.BINK_PAYMENTS_OKHTTP
import com.bink.payments.utils.BINK_PAYMENTS_RETROFIT
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val networkModule = module {
    single(named(BINK_PAYMENTS_OKHTTP)) {
        provideDefaultOkHttpClient(
            androidContext(),
            getProperty("isDebug")
        )
    }
    single(named(BINK_PAYMENTS_RETROFIT)) { provideRetrofit(get(named(BINK_PAYMENTS_OKHTTP))) }
    single { provideApiService(get(named(BINK_PAYMENTS_RETROFIT))) }
}

private var accessToken: String? = null

fun provideDefaultOkHttpClient(context: Context, isDebug: Boolean): OkHttpClient {
    val tokenAuthenticator = TokenAuthenticator()
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY

    val headerAuthorizationInterceptor = Interceptor { chain ->
        val request = chain.request().url.newBuilder().build()
        Log.d("Interceptor Log", "New request")

        val newRequest = chain.request().newBuilder()
            .header("Content-Type", "application/json")
            .header("Authorization", " Bearer ${getAuthBearer(request)}")
            .url(request)

        Log.d("Interceptor Log", "Proceeding request")
        val response = chain.proceed(newRequest.build())
        Log.d("Interceptor Log", "Got response")
        checkInterception(context = context, isDebug = isDebug, response = response)
        response
    }

    return OkHttpClient.Builder()
        .authenticator(tokenAuthenticator)
        .addInterceptor(headerAuthorizationInterceptor)
        .addInterceptor(interceptor).build()
}

private fun getAuthBearer(request: HttpUrl): String {
    val url = request.toUrl().toString()
    Log.d("Interceptor Log", "Getting auth bearer")
    return if (url.contains("token")) {
        //If trying to re-auth
        Log.d("Interceptor Log", "return ${BinkPayments.getRefreshToken()}")
        "Bearer ${BinkPayments.getRefreshToken()}"
    } else {
        //Any other call
        Log.d("Interceptor Log", "return $accessToken")
        "$accessToken"
    }
}

private fun checkInterception(context: Context, isDebug: Boolean, response: Response) {
    val responseCode = response.code
    Log.d("Interceptor Log", "Check interception, code: $responseCode")
    if (isDebug) {
        Looper.prepare()
        Toast.makeText(context.applicationContext, "$responseCode", Toast.LENGTH_SHORT).show()
        Looper.loop()
    }
}

fun provideRetrofit(client: OkHttpClient): Retrofit {
    val retrofitBuilder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(client)

    return retrofitBuilder.build()
}

fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

class TokenAuthenticator : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        Log.d("Interceptor Log", "Challenge size is ${response.challenges().size}")

        for (challenge in response.challenges()){
            Log.d("Interceptor Log", "Challenge is $challenge")
            if (challenge.equals("OkHttp-Preemptive")){
                Log.d("Interceptor Log", "Preemtive challenge")

            }
        }

        return when {
            response.retryCount > 1 -> null
            else -> response.request.newBuilder()
                .header("Content-Type", "application/json")
                .header("Authorization", getNewToken())
                .build();
        }
    }

    private fun getNewToken(): String {
        Log.d("Interceptor Log", "Set new token")
        val newToken =
            "Bearer eyJhbGciOiJIUzUxMiIsImtpZCI6ImFjY2Vzcy1zZWNyZXQtMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOjEyNDc1MywiY2hhbm5lbCI6ImNvbS50cnVzdGVkLmJpbmsud2FsbGV0IiwiaXNfdGVzdGVyIjpmYWxzZSwiaXNfdHJ1c3RlZF9jaGFubmVsIjp0cnVlLCJpYXQiOjE2NzgyOTIxMTMsImV4cCI6MTY3ODI5MzkxM30.bQ-7KfRJ-YDapAXQCvnnzdyVlbt6foV-Dc1o7F2NCTeD9Ff5ZT6R5-BDB-H6E81zvWR8T1JUFPBy7pugcwzJ2g"
        accessToken = newToken
        return newToken
    }

    private val Response.retryCount: Int
        get() {
            var currentResponse = priorResponse
            var result = 0
            while (currentResponse != null) {
                result++
                currentResponse = currentResponse.priorResponse
            }
            return result
        }
}