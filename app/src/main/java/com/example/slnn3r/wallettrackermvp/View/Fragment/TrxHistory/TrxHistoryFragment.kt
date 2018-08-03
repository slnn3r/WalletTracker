package com.example.slnn3r.wallettrackermvp.View.Fragment.TrxHistory

import android.os.Bundle

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.slnn3r.wallettrackermvp.R


class TrxHistoryFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.trxHistoryFragmentTitle)

        return inflater.inflate(R.layout.fragment_trx_history, container, false)
    }
}
