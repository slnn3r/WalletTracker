package com.example.slnn3r.wallettrackermvp.View.Fragment.WalletAccount


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.slnn3r.wallettrackermvp.R


class CreateWalletAccountFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        (activity as? AppCompatActivity)?.supportActionBar?.title = "Create Wallet Account"


        return inflater.inflate(R.layout.fragment_create_wallet_account, container, false)
    }


}
