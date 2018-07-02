package com.example.slnn3r.wallettrackermvp.View.Activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController

import com.example.slnn3r.wallettrackermvp.R



class WalletAccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet_account)

        setupNavigation()

    }

    private fun setupNavigation() {
        val navController = findNavController(R.id.navWalletAccount)
        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp() =
            findNavController(R.id.navWalletAccount).navigateUp()


}
