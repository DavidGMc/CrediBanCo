package com.androidavid.credibanco.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "http://192.168.1.104:8080/api/payments/" //ip desde celular (localhost)

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val paymentService: PaymentApiService by lazy {
        retrofit.create(PaymentApiService::class.java)
    }
}