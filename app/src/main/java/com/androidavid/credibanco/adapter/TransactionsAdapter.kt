package com.androidavid.credibanco.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidavid.credibanco.R
import com.androidavid.credibanco.db.Transaction

class TransactionsAdapter(private val transactions: MutableList<Transaction>):RecyclerView.Adapter<TransactionsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = transactions.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = transactions[position]

        // Configurar vistas dentro del elemento de lista con los datos de la transacción
        holder.textViewReceiptId.text = "Receipt ID: ${transaction.receiptId}"
        holder.textViewRRN.text = "RRN: ${transaction.rrn}"
        holder.textViewAmount.text = "Amount: ${transaction.amount}"
        holder.textViewCard.text = "Card Number: ${transaction.card}"
        holder.textViewStatus.text = "Status: ${transaction.statusDescription}"
    }
    class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val textViewReceiptId: TextView = itemView.findViewById(R.id.textViewReceiptId)
        val textViewRRN: TextView = itemView.findViewById(R.id.textViewRRN)
        val textViewAmount: TextView = itemView.findViewById(R.id.textViewAmount)
        val textViewCard: TextView = itemView.findViewById(R.id.textViewCard)
        val textViewStatus: TextView = itemView.findViewById(R.id.textViewStatus)

    }



}