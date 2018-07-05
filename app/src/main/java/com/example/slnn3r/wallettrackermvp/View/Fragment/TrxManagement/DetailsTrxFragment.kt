package com.example.slnn3r.wallettrackermvp.View.Fragment.TrxManagement


import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter

import com.example.slnn3r.wallettrackermvp.R
import com.example.slnn3r.wallettrackermvp.Utility.DummyDataCategorySpinner
import com.example.slnn3r.wallettrackermvp.Utility.SelectionTrxTypeSpinnerItem
import kotlinx.android.synthetic.main.fragment_details_trx.*
import java.text.SimpleDateFormat
import java.util.*


class DetailsTrxFragment : Fragment() {

    val myCalendar = Calendar.getInstance()
    val myFormat = "dd/MM/yy"
    val sdf = SimpleDateFormat(myFormat, Locale.US)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        (activity as? AppCompatActivity)?.supportActionBar?.title = "Transaction Details"

        return inflater.inflate(R.layout.fragment_details_trx, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

///// Populate Spinner Item

        // Creating adapter for Type spinner
        val dataAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, SelectionTrxTypeSpinnerItem().getSpinnerItem())

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        DetailsTrxTypeSpinner.adapter = dataAdapter

        // Creating adapter for Category spinner
        val data2Adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, DummyDataCategorySpinner().getSpinnerItem())

        // Drop down layout style - list view with radio button
        data2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        DetailsTrxCategorySpinner.adapter = data2Adapter

        /////

        // Setup Spinner listener
        DetailsTrxTypeSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                if(DetailsTrxTypeSpinner.selectedItem=="Expense"){

                    DetailsTrxTypeImageView.setImageDrawable(getResources().getDrawable(R.drawable.expense_icon))
                    DetailsTrxTypeImageView.setBackground(getResources().getDrawable(R.drawable.fui_idp_button_background_email))

                }else{
                    DetailsTrxTypeImageView.setImageDrawable(getResources().getDrawable(R.drawable.income_icon))
                    DetailsTrxTypeImageView.setBackground(getResources().getDrawable(R.drawable.fui_idp_button_background_phone))

                }

            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        })


        // Setup Date Picker

        val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            DetailsTrxDateInput.setText(sdf.format(myCalendar.getTime()))
        }

        DetailsTrxDateInput.setOnClickListener() {

            DatePickerDialog(context, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show()

        }




    }


}
