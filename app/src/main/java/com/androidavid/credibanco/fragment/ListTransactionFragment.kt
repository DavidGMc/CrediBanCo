package com.androidavid.credibanco.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidavid.credibanco.R
import com.androidavid.credibanco.adapter.TransactionsAdapter
import com.androidavid.credibanco.db.TransactionDao
import com.androidavid.credibanco.db.TransactionsDatabase
import kotlinx.coroutines.launch


class ListTransactionFragment : Fragment() {

    private lateinit var transactionDao: TransactionDao
    private lateinit var recyclerView: RecyclerView
    private lateinit var listener:TransactionsAdapter.OnItemClickListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_list_transaction, container, false)
        recyclerView = view.findViewById(R.id.rv_transactions_aprobadas)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar el RecyclerView y el adaptador
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager

        // Inicializa la base de datos Room y obtén una instancia de TransactionDao
        val database = TransactionsDatabase.getInstance(requireContext())
        transactionDao = database.transactionDao()

        // Consultar todas las transacciones aprobadas

        lifecycleScope.launch {
            val approvedTransactions = transactionDao.getAllApprovedTransactions()

            // Convertir la lista aprobada a una lista mutable
            val mutableApprovedTransactions = approvedTransactions.toMutableList()

            // Crear un adaptador y configurar el RecyclerView
            val adapter = TransactionsAdapter(mutableApprovedTransactions)
            recyclerView.adapter = adapter



// Notifica al adaptador que los datos han cambiado
            adapter.notifyDataSetChanged()


        }

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListTransactionFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}