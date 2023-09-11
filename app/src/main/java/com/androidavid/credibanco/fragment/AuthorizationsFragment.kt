package com.androidavid.credibanco.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.androidavid.credibanco.R
import com.androidavid.credibanco.api.ApiClient
import com.androidavid.credibanco.api.ApiClient.paymentService
import com.androidavid.credibanco.db.Transaction
import com.androidavid.credibanco.db.TransactionDao
import com.androidavid.credibanco.db.TransactionsDatabase
import com.androidavid.credibanco.model.AuthorizationRequest
import com.androidavid.credibanco.model.AuthorizationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat


class AuthorizationsFragment : Fragment() {
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
        val rootView: View = inflater.inflate(R.layout.fragment_authorizations, container, false)


        val edt_Id = rootView.findViewById<EditText>(R.id.edt_Id)
        val edt_commerceCode = rootView.findViewById<EditText>(R.id.edt_CommerceCode)
        val edt_TerminalCode = rootView.findViewById<EditText>(R.id.edt_TerminalCode)
        val edt_Amount = rootView.findViewById<EditText>(R.id.edt_Amount)
        val edt_Card = rootView.findViewById<EditText>(R.id.edt_Card)
        val btn_Autorize = rootView.findViewById<Button>(R.id.btn_Authorize)
       // val tv = rootView.findViewById<TextView>(R.id.tv)
        val progressbar = rootView.findViewById<ProgressBar>(R.id.progressBar)

        val database = TransactionsDatabase.getInstance(requireContext())
        transactionDao = database.transactionDao()
        progressbar.visibility =(View.INVISIBLE)

        btn_Autorize.setOnClickListener {
            val id = edt_Id.text.toString()
            val commerceCode = edt_commerceCode.text.toString()
            val terminalCode = edt_TerminalCode.text.toString()
            val amount = edt_Amount.text.toString()
            val card = edt_Card.text.toString()


            val credentials = "$commerceCode$terminalCode"
            val base64Credentials = "Basic " + encodeToBase64(credentials)
            val paymentRequest = AuthorizationRequest(id, commerceCode, terminalCode, amount, card)
            val call = paymentService.authorizePayment(base64Credentials, paymentRequest)

            call.enqueue(object : Callback<AuthorizationResponse> {
                override fun onResponse(
                    call: Call<AuthorizationResponse>,
                    response: Response<AuthorizationResponse>
                ) {
                    if (response.isSuccessful) {

                        val authorizationResponse = response.body()
                        val message = "Respuesta exitosa: $authorizationResponse"
                        alertDialog()
                        showToast(message)
                        if (authorizationResponse != null && authorizationResponse.statusCode == "00") {
                            // Crear una coroutine en segundo plano

                            lifecycleScope.launch(Dispatchers.IO) {
                                val transaction = Transaction(
                                    receiptId = authorizationResponse.receiptId.toString(),
                                    rrn = authorizationResponse.rrn.toString(),
                                    amount = amount,
                                    card = card,
                                    statusCode = authorizationResponse.statusCode,
                                    statusDescription = authorizationResponse.statusDescription
                                )

                                // Insertar la transacción en la base de datos Room
                                insertTransaction(transaction)
                            }

                        }
                        // Realizar cualquier acción adicional después de la autorización exitosa
                    } else {
                        Log.e(
                            "Response",
                            "Error en la respuesta del servicio: ${response.message()}"
                        )
                        val messageNeg = "Autorizacion Denegada: ${response.message()}"
                        showToast(messageNeg)
                    }

                }
                override fun onFailure(call: Call<AuthorizationResponse>, t: Throwable) {

                }
            })

        }
        return rootView
    }

    // Function  insert  transaction in bd Room
    private fun insertTransaction(transaction: Transaction) {
        lifecycleScope.launch {
            transactionDao.insertTransaction(transaction)
        }

    }

    private fun encodeToBase64(input: String): String {
        val bytes = input.toByteArray(Charsets.UTF_8)
        val base64 = Base64.encodeToString(bytes, Base64.NO_WRAP)
        return base64
    }

    private fun showToast(message: String) {
        val context = requireContext()
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    private fun alertDialog(){
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.informeDialog))
            .setMessage(getString(R.string.exit_dialog))
            .setPositiveButton(getString(R.string.aceptar)){ view, _ ->
                //

                view.dismiss()
            }
            .setCancelable(false)
            .create()
        dialog.show()
    }
}