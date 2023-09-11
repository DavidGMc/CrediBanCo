package com.androidavid.credibanco.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TransactionDao {
    @Insert
    suspend fun insertTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)
    @Update
    suspend fun updateTransaction(transaction: Transaction)


    @Query("SELECT * FROM transactions")
    suspend fun getAllTransactions(): List<Transaction>

    @Query("SELECT * FROM transactions WHERE statusCode = '00'")
    suspend fun getAllApprovedTransactions(): List<Transaction>

    @Query("SELECT * FROM transactions WHERE statusCode = '99'")
    suspend fun getAllAnuledTransactions(): List<Transaction>

    @Query("SELECT * FROM transactions WHERE receiptId = :receiptNumber")
    suspend fun getTransactionByReceipt(receiptNumber: String): Transaction?

    @Query("SELECT * FROM transactions WHERE rrn = :rrnNumber")
    suspend fun getTransactionByRrn(rrnNumber: String): Transaction?

    @Query( "UPDATE transactions SET statusCode =:statusCode,statusDescription=:statusDescription WHERE id= :id")
    suspend fun getUpdate(id: Long, statusCode:String, statusDescription:String)

       // Actualizar una transacción por su ID
   @Query("UPDATE transactions SET amount = :newAmount, card = :newCard, receiptId = :newReceiptId, rrn = :newRrn, statusCode = :newStatusCode, statusDescription = :newStatusDescription WHERE id = :transactionId")
    suspend fun updateTransaction(transactionId: Long,newAmount: String,newCard: String,newReceiptId: String,newRrn: String,newStatusCode: String,newStatusDescription: String)


}