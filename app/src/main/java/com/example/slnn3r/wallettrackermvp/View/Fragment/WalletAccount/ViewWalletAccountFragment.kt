package com.example.slnn3r.wallettrackermvp.View.Fragment.WalletAccount


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController

import com.example.slnn3r.wallettrackermvp.R
import kotlinx.android.synthetic.main.fragment_view_wallet_account.*



class ViewWalletAccountFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_view_wallet_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button2.setOnClickListener(){

            val navController = view.findNavController()
            navController.navigate(R.id.goto_createWalletAccountFragment)

        }



    }



}
