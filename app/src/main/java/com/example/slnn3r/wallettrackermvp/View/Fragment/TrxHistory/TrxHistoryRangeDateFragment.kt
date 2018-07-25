package com.example.slnn3r.wallettrackermvp.View.Fragment.TrxHistory


import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.slnn3r.wallettrackermvp.Adapter.TrxHistoryRangeAdapter
import com.example.slnn3r.wallettrackermvp.Interface.PresenterInterface
import com.example.slnn3r.wallettrackermvp.Interface.ViewInterface
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.Transaction
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.TransactionCategory
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.UserProfile
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.WalletAccount
import com.example.slnn3r.wallettrackermvp.Presenter.Presenter

import com.example.slnn3r.wallettrackermvp.R
import kotlinx.android.synthetic.main.fragment_trx_history_range_date.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TrxHistoryRangeDateFragment : Fragment(), ViewInterface.TrxHistoryRangeView {

    private val myCalendar = Calendar.getInstance()
    private lateinit var simpleDateFormat: SimpleDateFormat
    private lateinit var presenter: PresenterInterface.Presenter


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

        presenter = Presenter(this)
        val userProfile = presenter.getUserData(context!!)


        setupDatePicker()

        // populate Account Spinner
        presenter.checkWalletAccount(context!!,userProfile.UserUID)

        setupUI(userProfile)


        // Listener Setter
        THRTrxTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                trxTypeSpinnerClick(userProfile)
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }
        }

        THRFilterButton.setOnClickListener{

            // populate RecycleView for 1st Time
            val accountData=presenter.getAccountData(context!!, userProfile.UserUID)

            presenter.setSelectedAccount(context!!, accountData[THRAccountSpinner.selectedItemPosition].WalletAccountName) //Save Select Account in SharedPreference for future use

            presenter.getTrxForRangeDateFilter(context!!,
                    userProfile.UserUID,
                    accountData[THRAccountSpinner.selectedItemPosition].WalletAccountID,
                    THRTrxTypeSpinner.selectedItem.toString(),
                    THRTrxCategorySpinner.selectedItem.toString(),
                    THRStartDateInput.text.toString(),
                    THREndDateInput.text.toString()

            )

        }

    }


    // Function Implementation
    private fun trxTypeSpinnerClick(userProfile: UserProfile) {
        if(THRTrxTypeSpinner.selectedItem==getString(R.string.expense)){

            presenter.checkTransactionCategory(context!!, userProfile.UserUID, THRTrxTypeSpinner.selectedItem.toString())


        }else if(THRTrxTypeSpinner.selectedItem==getString(R.string.income)){

            presenter.checkTransactionCategory(context!!, userProfile.UserUID, THRTrxTypeSpinner.selectedItem.toString())

        }else{
            disableTrxTypeSpinner()
        }
    }

    private fun disableTrxTypeSpinner(){
        val spinnerItem = ArrayList<String>()

        spinnerItem.add("All Category")

        val dataAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, spinnerItem)

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        THRTrxCategorySpinner.adapter = dataAdapter

        THRTrxCategorySpinner.isEnabled = false
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

    private fun setupUI(userProfile: UserProfile){
        // populate RecycleView for 1st Time
        val accountData=presenter.getAccountData(context!!, userProfile.UserUID)

        val go = presenter.getSelectedAccount(context!!)

        var selectedAccountID=""

        accountData.forEach {
            data->
            if(data.WalletAccountName==go){
                selectedAccountID=data.WalletAccountID
            }
        }

        presenter.getTrxForRangeDateFilter(context!!,
                userProfile.UserUID,
                selectedAccountID,
                "All Type",
                "All Category",
                "All Days",
                "All Months"
        )


    }


    // Presenter Callback
    override fun disableBottomNavWhileLoading(mainContext: Context) {

        // Disable navigation through override the navigation path
        val navController = (mainContext as Activity).findNavController(R.id.navMenu)
        (mainContext as Activity).bottomNavigationFragmentView.setupWithNavController(navController)

    }

    override fun enableBottomNavAfterLoading(mainContext: Context) {

        // enable back the navigation by override the navigation path to the right one
        val navController = (mainContext as Activity).findNavController(R.id.trxHistoryFragmentNavMenu)
        (mainContext as Activity).bottomNavigationFragmentView.setupWithNavController(navController)

    }

    override fun populateTrxHistoryRangeAccountSpinner(mainContext: Context, walletAccountList: ArrayList<WalletAccount>) {

        val categories = ArrayList<String>()

        walletAccountList.forEach {
            data->
            categories.add(data.WalletAccountName)
        }

        // Creating adapter for spinner
        val dataAdapter = ArrayAdapter(mainContext, android.R.layout.simple_spinner_item, categories)

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        val spinner = (mainContext as Activity).findViewById(R.id.THRAccountSpinner) as Spinner
        spinner.adapter = dataAdapter

        // Get SharedPreference saved Selection and Set to Spinner Selection
        val go = presenter.getSelectedAccount(mainContext)

        val spinnerPosition = dataAdapter.getPosition(go)
        spinner.setSelection(spinnerPosition)

    }

    override fun populateTrxHistoryRangeAccountSpinnerFail(mainContext: Context, errorMessage: String) {
        Toast.makeText(mainContext,errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun populateTrxHistoryRangeCategorySpinner(mainContext: Context, trxCategoryList: ArrayList<TransactionCategory>) {

        val spinnerItem = ArrayList<String>()

        spinnerItem.add("All Category")


        trxCategoryList.forEach {
            data ->
            spinnerItem.add(data.TransactionCategoryName)
        }

        val dataAdapter = ArrayAdapter(mainContext, android.R.layout.simple_spinner_item, spinnerItem)
        val THRTrxCategorySpinner = (mainContext as Activity).findViewById(R.id.THRTrxCategorySpinner) as Spinner


        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        THRTrxCategorySpinner.adapter = dataAdapter
        THRTrxCategorySpinner.isEnabled = true

    }

    override fun populateTrxHistoryRangeCategorySpinnerFail(mainContext: Context, errorMessage: String) {
        Toast.makeText(mainContext,errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun populateTrxHistoryRangeRecycleView(mainContext: Context, transactionList: ArrayList<Transaction>) {

        //!!
        val TrxRecyclerView = (mainContext as Activity).findViewById(R.id.THRRecyclerView) as RecyclerView

        TrxRecyclerView.layoutManager = LinearLayoutManager(mainContext)
        TrxRecyclerView.adapter = TrxHistoryRangeAdapter(transactionList)
    }

    override fun populateTrxHistoryRangeRecycleViewFail(mainContext: Context, errorMessage: String) {
        Toast.makeText(mainContext,errorMessage, Toast.LENGTH_LONG).show()
    }



}
