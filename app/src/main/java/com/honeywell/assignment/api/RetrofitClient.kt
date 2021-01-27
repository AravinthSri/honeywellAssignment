package com.honeywell.assignment.api


import com.honeywell.assignment.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://hn.algolia.com/api/v1/"

    fun retrofit(): HoneyWellServices {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level =
                        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.BODY
                }).build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HoneyWellServices::class.java)
    }
}