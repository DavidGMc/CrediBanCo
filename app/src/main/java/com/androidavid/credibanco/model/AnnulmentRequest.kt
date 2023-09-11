package com.androidavid.credibanco.model

// solicitud para anular

data class AnnulmentRequest(
    val receiptId: String,
    val rrn: String
)
