package com.example.slnn3r.wallettrackermvp.View.Fragment.TrxCategory


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.findNavController
import com.example.slnn3r.wallettrackermvp.Adapter.TrxCategoryAdapter

import com.example.slnn3r.wallettrackermvp.R
import com.example.slnn3r.wallettrackermvp.Utility.DummyDataCategoryListItem
import com.example.slnn3r.wallettrackermvp.Utility.FilterTrxTypeSpinnerItem
import kotlinx.android.synthetic.main.fragment_view_trx_category.*


class ViewTrxCategoryFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        (activity as? AppCompatActivity)?.supportActionBar?.title = "Transaction Category List"

        return inflater.inflate(R.layout.fragment_view_trx_category, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        VTCGoToCTC.setOnClickListener(){
            val navController = view.findNavController()
            navController.navigate(R.id.action_viewTrxCategoryFragment_to_createTrxCategoryFragment)

        }

        ///// Dummy RecycleView
        VTCRecyclerView.layoutManager = LinearLayoutManager(context)


        VTCRecyclerView.adapter = TrxCategoryAdapter(DummyDataCategoryListItem().getListItem())
        /////


        ///// Populate Spinner Item

        // Creating adapter for spinner
        val dataAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, FilterTrxTypeSpinnerItem().getSpinnerItem())

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        VTCTrxTypeSpinner.adapter = dataAdapter

        /////

    }



}
