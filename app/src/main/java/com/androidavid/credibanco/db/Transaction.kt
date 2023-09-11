package com.androidavid.credibanco.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: String,
    val card: String,
    val receiptId: String,
    val rrn: String,
    var statusCode: String,
    var statusDescription: String
)
