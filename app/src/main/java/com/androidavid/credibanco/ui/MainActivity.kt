package com.androidavid.credibanco.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.androidavid.credibanco.R
import com.androidavid.credibanco.databinding.ActivityMainBinding
import com.androidavid.credibanco.fragment.AuthorizationsFragment
import com.androidavid.credibanco.fragment.CancelTransactionsFragment
import com.androidavid.credibanco.fragment.ListTransactionFragment
import com.androidavid.credibanco.fragment.SearchTransactionsFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavView.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.menu_autorize_transactions -> setCurrentItemsFragment(AuthorizationsFragment())
                R.id.menu_search_transactions -> setCurrentItemsFragment(SearchTransactionsFragment())
                R.id.menu_list_transactions -> setCurrentItemsFragment(ListTransactionFragment())
                R.id.menu_remove_transactions -> setCurrentItemsFragment(CancelTransactionsFragment())
                else -> {
                    false
                }
            }
             true
        }
        binding.bottomNavView.selectedItemId= R.id.menu_autorize_transactions

    }

    private fun setCurrentItemsFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.container_view,fragment)
                    .addToBackStack(null)
                    .commit()
            }
    }

}