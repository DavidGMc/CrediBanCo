package com.androidavid.credibanco.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.androidavid.credibanco.R
import com.androidavid.credibanco.db.Transaction
import com.androidavid.credibanco.db.TransactionDao
import com.androidavid.credibanco.db.TransactionsDatabase
import kotlinx.coroutines.launch


class SearchTransactionsFragment : Fragment() {
    private lateinit var transactionDao: TransactionDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_transactions, container, false)

        // Inicializa transactionDao aquí utilizando el contexto
        val database = TransactionsDatabase.getInstance(requireContext())
        transactionDao = database.transactionDao()

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val edt_rrn = view.findViewById<EditText>(R.id.edt_rrn)
        val btn_Search = view.findViewById<Button>(R.id.btn_Search)


        btn_Search.setOnClickListener {
            val rrnNumber = edt_rrn.text.toString()
            lifecycleScope.launch {

                // Realizar la busqueda de la transacción aprobada por número de recibo rrn
                searchTransactionByReceipt(rrnNumber = rrnNumber)


            }
        }
    }

    private suspend fun searchTransactionByReceipt(rrnNumber: String): Transaction? {
        // Utiliza el TransactionDao para buscar la transacción por número de recibo
        val transaction = transactionDao.getTransactionByRrn(rrnNumber)

        // Verifica si se encontró la transacción
        if (transaction != null && transaction.statusCode !="99") {
            // Construye un mensaje con los detalles de la transacción

            val message = "Detalles de la transacción:\n" +
                    "ID: ${transaction.id}\n"+
                    "Receipt ID: ${transaction.receiptId}\n" +
                    "RRN: ${transaction.rrn}\n" +
                    "Amount: ${transaction.amount}\n" +
                    "Card Number: ${transaction.card}\n" +
                    "Status: ${transaction.statusDescription}"



            // Muestra el mensaje en un Toast
            showToast(message)



            val bundle = Bundle()
            bundle.putLong("ID", transaction.id)
            bundle.putString("ReceiptID", transaction.receiptId)
            bundle.putString("RRN", transaction.rrn)
            bundle.putString("Amount", transaction.amount)
            bundle.putString("CardNumber", transaction.card)
            bundle.putString("StatusCode", transaction.statusCode)
            bundle.putString("StatusDescription", transaction.statusDescription)
// Agrega otros datos a bundle si es necesario

            val transactionDetailFragment = TransactionDetailFragment()
            transactionDetailFragment.arguments = bundle

// Navega al fragmento de detalle de transacción
            childFragmentManager.beginTransaction()
                ?.replace(R.id.container_search, transactionDetailFragment)
                ?.addToBackStack(null)
                ?.commit()


            // Devuelve la transacción encontrada
            return transaction
        } else {
            // Si no se encuentra la transacción, muestra un mensaje de error en el Toast
            showToast(getString(R.string.show_toast_anulada))

            // Devuelve nulo
            return null
        }
    }
    private fun showToast(message: String) {
        val context = requireContext() // Obtén el contexto del Fragment
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}