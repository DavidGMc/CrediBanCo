package com.androidavid.credibanco.repository

import com.androidavid.credibanco.db.Transaction
import com.androidavid.credibanco.db.TransactionDao

class TransactionRepository (private val transactionDao: TransactionDao){

    suspend fun getTransactionByReceipt(receiptNumber: String): Transaction? {
        return transactionDao.getTransactionByReceipt(receiptNumber)
    }
}