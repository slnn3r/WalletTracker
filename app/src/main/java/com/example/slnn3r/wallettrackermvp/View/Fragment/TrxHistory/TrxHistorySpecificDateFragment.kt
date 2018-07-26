package com.example.slnn3r.wallettrackermvp.View.Fragment.TrxHistory



import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.example.slnn3r.wallettrackermvp.R
import kotlinx.android.synthetic.main.fragment_trx_history_specific_date.*
import java.util.*
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.slnn3r.wallettrackermvp.Adapter.TrxHistorySpecificAdapter
import com.example.slnn3r.wallettrackermvp.Interface.PresenterInterface
import com.example.slnn3r.wallettrackermvp.Interface.ViewInterface
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.Transaction
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.TransactionCategory
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.UserProfile
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.WalletAccount
import com.example.slnn3r.wallettrackermvp.Presenter.Presenter
import kotlin.collections.ArrayList


class TrxHistorySpecificDateFragment : Fragment(), ViewInterface.TrxHistorySpecificView {

    private lateinit var presenter: PresenterInterface.Presenter

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

        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.trxHistoryFragmentTitle)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trx_history_specific_date, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup bottom navigation
        val navController = (context as Activity).findNavController(R.id.trxHistoryFragmentNavMenu)
        bottomNavigationFragmentView.setupWithNavController(navController)


        presenter = Presenter(this)
        val userProfile = presenter.getUserData(context!!)

        // populate Account Spinner
        presenter.checkWalletAccount(context!!,userProfile.UserUID)


        setupUI(userProfile)


        // Listener Setter
        THSYearSpinner.onItemSelectedListener = fixDays
        THSMonthSpinner.onItemSelectedListener = fixDays

        THSTrxTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                trxTypeSpinnerClick(userProfile)
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }
        }

        THSFilterButton.setOnClickListener{
            // populate RecycleView for 1st Time
            val accountData=presenter.getAccountData(context!!, userProfile.UserUID)

            presenter.setSelectedAccount(context!!, accountData[THSAccountSpinner.selectedItemPosition].WalletAccountName) //Save Select Account in SharedPreference for future use

            presenter.getTrxForSpecificDateFilter(context!!,
                    userProfile.UserUID,
                    accountData[THSAccountSpinner.selectedItemPosition].WalletAccountID,
                    THSTrxTypeSpinner.selectedItem.toString(),
                    THSTrxCategorySpinner.selectedItem.toString(),
                    THSDaySpinner.selectedItem.toString(),
                    THSMonthSpinner.selectedItem.toString(),
                    THSYearSpinner.selectedItem.toString()
            )

        }


    }


    // Function Implementation
    private fun trxTypeSpinnerClick(userProfile: UserProfile) {
        if(THSTrxTypeSpinner.selectedItem==getString(R.string.expense)){

            presenter.checkTransactionCategory(context!!, userProfile.UserUID, THSTrxTypeSpinner.selectedItem.toString())


        }else if(THSTrxTypeSpinner.selectedItem==getString(R.string.income)){

            presenter.checkTransactionCategory(context!!, userProfile.UserUID, THSTrxTypeSpinner.selectedItem.toString())

        }else{
            disableTrxTypeSpinner()
        }
    }

    private fun disableTrxTypeSpinner(){
        val spinnerItem = ArrayList<String>()

            spinnerItem.add(getString(R.string.allCategory))

        val dataAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, spinnerItem)

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        THSTrxCategorySpinner.adapter = dataAdapter

        THSTrxCategorySpinner.isEnabled = false
    }


    private fun setDays() {

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

            val daysArray = arrayOfNulls<String>(daysInMonth+1)

            daysArray[0] = getString(R.string.allDay)

            for (k in 1 until daysInMonth+1)
                daysArray[k] = "" + (k)

            val spinnerArrayAdapter = ArrayAdapter(context,
                    android.R.layout.simple_spinner_dropdown_item, daysArray)
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            THSDaySpinner.adapter = spinnerArrayAdapter

        }



    }


    private fun populateYears(minYear: Int, maxYear: Int) {

        val yearsArray = arrayOfNulls<String>(maxYear - minYear+2)

        yearsArray[0] = getString(R.string.allYear)


        var count = 1
        for (i in minYear..maxYear) {
            yearsArray[count] = "" + i
            count++
        }

        val spinnerArrayAdapter = ArrayAdapter(context,
                android.R.layout.simple_spinner_dropdown_item, yearsArray)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        THSYearSpinner.adapter = spinnerArrayAdapter

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

        presenter.getTrxForSpecificDateFilter(context!!,
                userProfile.UserUID,
                selectedAccountID,
                "All Type",
                "All Category",
                "All Days",
                "All Months",
                "All Years"
        )


        populateYears(Calendar.getInstance().get(Calendar.YEAR)-5, Calendar.getInstance().get(Calendar.YEAR))

    }


    // Presenter Callback
    override fun disableBottomNavWhileLoading(mainContext: Context){

        // Disable navigation through override the navigation path
        val navController = (mainContext as Activity).findNavController(R.id.navMenu)
        mainContext.bottomNavigationFragmentView.setupWithNavController(navController)
    }

    override fun enableBottomNavAfterLoading(mainContext: Context){

        // enable back the navigation by override the navigation path to the right one
        val navController = (mainContext as Activity).findNavController(R.id.trxHistoryFragmentNavMenu)
        mainContext.bottomNavigationFragmentView.setupWithNavController(navController)

    }

    override fun populateTrxHistorySpecificAccountSpinner(mainContext: Context, walletAccountList: ArrayList<WalletAccount>) {

        val categories = ArrayList<String>()

        walletAccountList.forEach {
            data->
            categories.add(data.WalletAccountName)
        }

        // Creating adapter for spinner
        val dataAdapter = ArrayAdapter(mainContext, android.R.layout.simple_spinner_item, categories)

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        val spinner = (mainContext as Activity).findViewById(R.id.THSAccountSpinner) as Spinner
        spinner.adapter = dataAdapter

        // Get SharedPreference saved Selection and Set to Spinner Selection
        val go = presenter.getSelectedAccount(mainContext)

        val spinnerPosition = dataAdapter.getPosition(go)
        spinner.setSelection(spinnerPosition)



    }

    override fun populateTrxHistorySpecificAccountSpinnerFail(mainContext: Context, errorMessage: String) {
        Toast.makeText(mainContext,errorMessage, Toast.LENGTH_LONG).show()
    }


    override fun populateTrxHistorySpecificCategorySpinner(mainContext: Context, trxCategoryList: ArrayList<TransactionCategory>) {

        val spinnerItem = ArrayList<String>()

        spinnerItem.add(mainContext.getString(R.string.allCategory))


        trxCategoryList.forEach {
            data ->
            spinnerItem.add(data.TransactionCategoryName)
        }

        val dataAdapter = ArrayAdapter(mainContext, android.R.layout.simple_spinner_item, spinnerItem)
        val tHSTrxCategorySpinner = (mainContext as Activity).findViewById(R.id.THSTrxCategorySpinner) as Spinner


        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        tHSTrxCategorySpinner.adapter = dataAdapter
        tHSTrxCategorySpinner.isEnabled = true
    }

    override fun populateTrxHistorySpecificCategorySpinnerFail(mainContext: Context, errorMessage: String) {
        Toast.makeText(mainContext,errorMessage, Toast.LENGTH_LONG).show()
    }


    override fun populateTrxHistorySpecificRecycleView(mainContext: Context, transactionList: ArrayList<Transaction>) {

        var income=0.0
        var expense=0.0
        var balance: Double

        transactionList.forEach {
            data->
            if(data.TransactionCategory.TransactionCategoryType=="Income"){
                income+=data.TransactionAmount
            }else{
                expense+=data.TransactionAmount
            }
        }

        balance= income-expense

        //Display Summary Figure
        val incomeView = (mainContext as Activity).findViewById(R.id.THSIncomeTextView) as TextView
        val expenseView = mainContext.findViewById(R.id.THSExpenseTextView) as TextView
        val balanceView = mainContext.findViewById(R.id.THSBalanceTextView) as TextView

        if(balance<0){
            balanceView.setTextColor(Color.RED)
        }else{
            balanceView.setTextColor(Color.GREEN)
        }

        incomeView.text=mainContext.getString(R.string.formatTHRIncome, income)
        expenseView.text=mainContext.getString(R.string.formatTHRExpense, expense)
        balanceView.text=mainContext.getString(R.string.formatTHRBalance, balance)

        //!!
        val trxRecyclerView = mainContext.findViewById(R.id.THSRecyclerView) as RecyclerView

        trxRecyclerView.layoutManager = LinearLayoutManager(mainContext)
        trxRecyclerView.adapter = TrxHistorySpecificAdapter(transactionList)

    }

    override fun populateTrxHistorySpecificRecycleViewFail(mainContext: Context, errorMessage: String) {
        Toast.makeText(mainContext,errorMessage, Toast.LENGTH_LONG).show()
    }

}
