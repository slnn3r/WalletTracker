package com.example.slnn3r.wallettrackermvp.View.Fragment.TrxHistory


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController

import com.example.slnn3r.wallettrackermvp.R
import kotlinx.android.synthetic.main.fragment_trx_history_range_date.*

class TrxHistoryRangeDateFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trx_history_range_date, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button2.setOnClickListener{

            //val navController = view.findNavController()


            //navController.navigate(R.id.action_trxHistoryRangeDateFragment_to_detailsTrxFragmentforTrxHistory)

        }

    }

}
