package com.androidavid.credibanco.model

// respuesta de autorizacion

data class AuthorizationResponse(
    val receiptId: String?,
    val rrn: String?,
    val statusCode: String,
    val statusDescription: String
)
