package com.example.slnn3r.wallettrackermvp.View.Fragment.TrxHistory



import android.app.Activity
import android.os.Bundle

import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.slnn3r.wallettrackermvp.R
import kotlinx.android.synthetic.main.fragment_trx_history_specific_date.*
import android.widget.ArrayAdapter
import java.util.*
import android.widget.AdapterView
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.fragment_trx_history.*


class TrxHistorySpecificDateFragment : Fragment() {

    private var calendar: Calendar? = null

    private var fixDays: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            setDays()
        }

        override fun onNothingSelected(parent: AdapterView<*>) {
            //Another interface callback
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trx_history_specific_date, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        calendar = Calendar.getInstance()

        THSYearSpinner.onItemSelectedListener = fixDays
        THSMonthSpinner.onItemSelectedListener = fixDays

        populateYears(Calendar.getInstance().get(Calendar.YEAR)-5, Calendar.getInstance().get(Calendar.YEAR))


        THSFilterButton.setOnClickListener{

            //val navController = view.findNavController()
            //navController.navigate(R.id.action_trxHistorySpecificDateFragment_to_detailsTrxFragmentforTrxHistory)


            // enable back the navigation by override the navigation path to the right one
            val navController = (context as Activity).findNavController(R.id.trxHistoryFragmentNavMenu)
            (context as Activity).bottomNavigationFragmentView.setupWithNavController(navController)


        }


        // Disable navigation through override the navigation path
        val navController = (context as Activity).findNavController(R.id.navMenu)
        (context as Activity).bottomNavigationFragmentView.setupWithNavController(navController)

    }





    fun setDays() {

        if(THSYearSpinner.selectedItem.toString()==getString(R.string.allYear) || THSMonthSpinner.selectedItem.toString()==getString(R.string.allMonth)){

            THSDaySpinner.isEnabled=false
            THSMonthSpinner.isEnabled=false

            val days_array = arrayOfNulls<String>(1)
            days_array[0] = getString(R.string.allDay)

            val spinnerArrayAdapter = ArrayAdapter(context,
                    android.R.layout.simple_spinner_dropdown_item, days_array)
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            THSDaySpinner.adapter = spinnerArrayAdapter

            THSMonthSpinner.setSelection(0)

        }

        if(THSYearSpinner.selectedItem.toString()!=getString(R.string.allYear) && THSMonthSpinner.selectedItem.toString()==getString(R.string.allMonth)){
            THSMonthSpinner.isEnabled=true

        }

        if(THSMonthSpinner.selectedItem.toString()!=getString(R.string.allMonth) && THSYearSpinner.selectedItem.toString()!=getString(R.string.allYear)){

            THSDaySpinner.isEnabled=true

            val year = Integer.parseInt(THSYearSpinner.selectedItem.toString())
            val month = THSMonthSpinner.selectedItem.toString()

            val months = resources.getStringArray(R.array.months)

            val mycal = GregorianCalendar(year, months.indexOf(month)-1, 1)

            // Get the number of days in that month
            val daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH)

            val days_array = arrayOfNulls<String>(daysInMonth+1)

            days_array[0] = getString(R.string.allDay)

            for (k in 1 until daysInMonth+1)
                days_array[k] = "" + (k)

            val spinnerArrayAdapter = ArrayAdapter(context,
                    android.R.layout.simple_spinner_dropdown_item, days_array)
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            THSDaySpinner.adapter = spinnerArrayAdapter

        }





    }




    private fun populateYears(minYear: Int, maxYear: Int) {

        val years_array = arrayOfNulls<String>(maxYear - minYear+2)

        years_array[0] = getString(R.string.allYear)


        var count = 1
        for (i in minYear..maxYear) {
            years_array[count] = "" + i
            count++
        }

        val spinnerArrayAdapter = ArrayAdapter(context,
                android.R.layout.simple_spinner_dropdown_item, years_array)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        THSYearSpinner.adapter = spinnerArrayAdapter

    }

}
