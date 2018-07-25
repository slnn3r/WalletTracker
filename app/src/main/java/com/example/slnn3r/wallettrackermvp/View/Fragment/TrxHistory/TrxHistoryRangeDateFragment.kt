package com.example.slnn3r.wallettrackermvp.View.Fragment.TrxHistory


import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.slnn3r.wallettrackermvp.Interface.ViewInterface

import com.example.slnn3r.wallettrackermvp.R
import kotlinx.android.synthetic.main.fragment_trx_history_range_date.*
import java.text.SimpleDateFormat
import java.util.*

class TrxHistoryRangeDateFragment : Fragment(), ViewInterface.TrxHistoryRangeView {

    private val myCalendar = Calendar.getInstance()
    private lateinit var simpleDateFormat: SimpleDateFormat


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        simpleDateFormat = SimpleDateFormat(getString(R.string.dateFormat), Locale.US)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trx_history_range_date, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup bottom navigation
        val navController = (context as Activity).findNavController(R.id.trxHistoryFragmentNavMenu)
        bottomNavigationFragmentView.setupWithNavController(navController)

        setupDatePicker()

        THRFilterButton.setOnClickListener{

            //val navController = view.findNavController()


            //navController.navigate(R.id.action_trxHistoryRangeDateFragment_to_detailsTrxFragmentforTrxHistory)

        }

    }

    private fun setupDatePicker() {
        val startDate = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            THRStartDateInput.setText(simpleDateFormat.format(myCalendar.time))
        }

        val endDate = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            THREndDateInput.setText(simpleDateFormat.format(myCalendar.time))
        }


        THRStartDateInput.setOnClickListener {

            DatePickerDialog(context, startDate, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show()

        }


        THREndDateInput.setOnClickListener {

            DatePickerDialog(context, endDate, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show()

        }

        // Initial Date
        THRStartDateInput.setText(simpleDateFormat.format(myCalendar.time))
        THREndDateInput.setText(simpleDateFormat.format(myCalendar.time))
    }

}
