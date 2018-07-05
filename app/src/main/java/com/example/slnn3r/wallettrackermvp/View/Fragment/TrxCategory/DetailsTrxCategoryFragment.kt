package com.example.slnn3r.wallettrackermvp.View.Fragment.TrxCategory


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter

import com.example.slnn3r.wallettrackermvp.R
import com.example.slnn3r.wallettrackermvp.Utility.SelectionTrxTypeSpinnerItem
import kotlinx.android.synthetic.main.fragment_details_trx_category.*


class DetailsTrxCategoryFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        (activity as? AppCompatActivity)?.supportActionBar?.title = "Transaction Category Details"


        return inflater.inflate(R.layout.fragment_details_trx_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        ///// Populate Spinner Item

        // Creating adapter for spinner
        val dataAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, SelectionTrxTypeSpinnerItem().getSpinnerItem())

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        DTCTrxTypeSpinner.adapter = dataAdapter

        /////


        // Setup Spinner listener
        DTCTrxTypeSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                if(DTCTrxTypeSpinner.selectedItem=="Expense"){

                    DTCImageView.setImageDrawable(getResources().getDrawable(R.drawable.expense_icon))
                    DTCImageView.setBackground(getResources().getDrawable(R.drawable.fui_idp_button_background_email))

                }else{
                    DTCImageView.setImageDrawable(getResources().getDrawable(R.drawable.income_icon))
                    DTCImageView.setBackground(getResources().getDrawable(R.drawable.fui_idp_button_background_phone))

                }

            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        })

    }


}
