package com.androidavid.credibanco.api

import com.androidavid.credibanco.model.AnnulmentRequest
import com.androidavid.credibanco.model.AnnulmentResponse
import com.androidavid.credibanco.model.AuthorizationRequest
import com.androidavid.credibanco.model.AuthorizationResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface PaymentApiService {

    // para la autorización de pagos
    @POST("authorization")
    @Headers("Content-Type: application/json")
    fun authorizePayment(
        @Header("Authorization") basicAuth: String,
        @Body request: AuthorizationRequest): Call<AuthorizationResponse>

    /*fun authorizePayment(
        @Header("Authorization") basicAuth: String
    ): Call<AuthorizationResponse>*/
    @POST("annulment")
    @Headers("Content-Type: application/json")
    fun annulTransaction(
        @Header("Authorization") basicAuth: String,
        @Body annulmentRequest: AnnulmentRequest): Call<AnnulmentResponse>


}