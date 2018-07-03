package com.example.slnn3r.wallettrackermvp.View.Fragment.TrxCategory


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController

import com.example.slnn3r.wallettrackermvp.R
import com.example.slnn3r.wallettrackermvp.View.Activity.MenuActivity
import kotlinx.android.synthetic.main.fragment_create_trx_category.*

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */

class CreateTrxCategoryFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_trx_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        button3.setOnClickListener(){
            val navController = view.findNavController()
            navController.navigate(R.id.action_viewTrxCategoryFragment_to_detailsTrxCategoryFragment)
            (activity as MenuActivity).setupNavigationMode()
        }
    }

}
