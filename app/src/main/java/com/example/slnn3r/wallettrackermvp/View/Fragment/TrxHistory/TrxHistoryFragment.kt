package com.example.slnn3r.wallettrackermvp.View.Fragment.TrxHistory

import android.app.Activity
import android.os.Bundle

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import androidx.navigation.ui.setupWithNavController
import androidx.navigation.findNavController


import com.example.slnn3r.wallettrackermvp.R
import kotlinx.android.synthetic.main.fragment_trx_history.*


class TrxHistoryFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.trxHistoryFragmentTitle)


        return inflater.inflate(R.layout.fragment_trx_history, container, false)
    }

}
