package com.androidavid.credibanco.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.androidavid.credibanco.R
import com.androidavid.credibanco.api.ApiClient.paymentService
import com.androidavid.credibanco.db.Transaction
import com.androidavid.credibanco.db.TransactionDao
import com.androidavid.credibanco.db.TransactionsDatabase
import com.androidavid.credibanco.model.AnnulmentRequest
import com.androidavid.credibanco.model.AnnulmentResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TransactionDetailFragment : Fragment() {
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

        val view :View= inflater.inflate(R.layout.fragment_transaction_detail, container, false)


        val database = TransactionsDatabase.getInstance(requireContext())
        transactionDao = database.transactionDao()


        var tv_id= view.findViewById<TextView>(R.id.tv_Id)
        val tv_receiptId = view.findViewById<TextView>(R.id.tv_ReceipId)
        val tv_rrn = view.findViewById<TextView>(R.id.tv_Rrn)
        val tv_amount = view.findViewById<TextView>(R.id.tv_Amount)
        val tv_card = view.findViewById<TextView>(R.id.tv_Card)
        var tv_statusCode = view.findViewById<TextView>(R.id.tv_Status)
        var tv_statusDesc = view.findViewById<TextView>(R.id.tv_StatusDes)


        val args = arguments
        var transaction: Transaction? = null
        if (args != null) {

            val receiptId = args.getString("ReceiptID", "")
            val rrn = args.getString("RRN", "")
            val amount = args.getString("Amount", "")
            val card = args.getString("CardNumber", "")
            val statusCode = args.getString("StatusCode", "")
            val statusDescription = args.getString("StatusDescription", "")
            val id= args.getLong("ID",)
            var ident:Long= id

            tv_id.text=  "$ident"
            tv_receiptId.text = "$receiptId"
            tv_rrn.text = "$rrn"
            tv_amount.text = "$amount"
            tv_card.text = "$card"
            tv_statusCode.text = "$statusCode"
            tv_statusDesc.text = "$statusDescription"


            // Configura otras vistas con los detalles de la transacción
        }




        // Configura el botón para anular la transacción
        val cancelButton = view.findViewById<Button>(R.id.btn_CancelTransaction)
        cancelButton.setOnClickListener {
            // Muestra un diálogo de confirmación al usuario
            val confirmationDialog = AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.confirmacion_alert_trasacction))
                .setMessage(getString(R.string.msg_dialog_anular_transaccion))
                .setPositiveButton(getString(R.string.yes)) { dialog, _ ->

                    //

                    val commerceCode= "000123"
                    val terminalCode= "000ABC"
                    // Llamada asíncrona para anular la transacción
                    val credentials = "$commerceCode$terminalCode"
                    val base64Credentials = "Basic " + encodeToBase64(credentials)
                    val anularRequest = AnnulmentRequest(receiptId = tv_receiptId.text.toString(),tv_rrn.text.toString())
                    //val call = paymentService.annulTransaction(base64Credentials,paymentRequest)
                    paymentService.annulTransaction(base64Credentials, anularRequest)
                        .enqueue(object : Callback<AnnulmentResponse> {
                        override fun onResponse(call: Call<AnnulmentResponse>, response: Response<AnnulmentResponse>) {
                            if (response.isSuccessful) {
                                val annulmentResponse = response.body()


                                if (annulmentResponse != null && annulmentResponse.statusCode == "00") {
                                    tv_statusCode

                                    var newstatusCode = "99"
                                    var newDescription= "Anulada"
                                    // La transacción se anuló con éxito en el servidor
                                    lifecycleScope.launch(Dispatchers.IO) {
                                        // Actualiza la transacción con los nuevos valores
                                        val transaction = Transaction(
                                            tv_id.text.toString().toLong(),
                                            tv_amount.text.toString(),
                                            tv_card.text.toString(),
                                            tv_receiptId.text.toString(),
                                            tv_rrn.text.toString(),
                                            newstatusCode,
                                            newDescription,                                       )

                                       updateTransaction(transaction)

                                    }
                                    // Muestra un mensaje de éxito
                                    showToast("Transacción anulada con éxito")
                                } else {
                                    // La anulación no fue exitosa, muestra un mensaje de error
                                    showToast("La anulación de la transacción falló")
                                }
                            } else {
                                // Respuesta del servidor no exitosa, muestra un mensaje de error
                                showToast("Error en la solicitud al servidor")
                            }
                        }

                        override fun onFailure(call: Call<AnnulmentResponse>, t: Throwable) {
                            // Error en la solicitud al servidor, muestra un mensaje de error
                            showToast("Error en la solicitud al servidor: ${t.message}")
                        }
                    })
                }
                .setNegativeButton("No") { dialog, _ ->
                    // El usuario canceló la anulación, no se hace nada
                    dialog.dismiss()
                }
                .create()

            // Muestra el diálogo de confirmación
            confirmationDialog.show()
        }
     // Función para actualizar la transacción en la base de datos local


        return view


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun showToast(message: String) {
        val context = requireContext() // Obtén el contexto del Fragment
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun deleteTransaction(deletedTransaction: Transaction) {
        // Utiliza el DAO de Room para actualizar la transacción en la base de datos local
        lifecycleScope.launch {
            transactionDao.deleteTransaction(deletedTransaction)
        }
    }
    fun encodeToBase64(input: String): String {
        val bytes = input.toByteArray(Charsets.UTF_8)
        val base64 = Base64.encodeToString(bytes, Base64.NO_WRAP)
        return base64
    }
    private fun updateTransaction(updatedTransaction: Transaction) {
        // Utiliza el DAO de Room para actualizar la transacción en la base de datos local
        lifecycleScope.launch {
            transactionDao.updateTransaction(
                updatedTransaction.id,
                updatedTransaction.amount,
                updatedTransaction.card,
                updatedTransaction.receiptId,
                updatedTransaction.rrn,
                updatedTransaction.statusCode,
                updatedTransaction.statusDescription
            )
        }
    }





}