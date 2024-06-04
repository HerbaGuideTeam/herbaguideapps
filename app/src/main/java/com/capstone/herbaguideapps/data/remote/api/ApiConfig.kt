package com.capstone.herbaguideapps.data.remote.api

import com.capstone.herbaguideapps.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    private const val BASE_URL = BuildConfig.BASE_URL

    private val loggingInterceptor = if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
    } else {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    fun getAuthServices(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }

    fun getApiServices(token: String): ApiService {
        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            val requestHeaders = req.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(requestHeaders)
        }

        val clientWithAuthInterceptor = client.newBuilder()
            .addInterceptor(authInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(clientWithAuthInterceptor)
            .build()

        return retrofit.create(ApiService::class.java)
    }

    fun getNewsApiService(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.NEWS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}