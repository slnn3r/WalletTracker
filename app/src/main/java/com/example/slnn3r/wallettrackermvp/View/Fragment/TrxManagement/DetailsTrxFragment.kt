package com.example.slnn3r.wallettrackermvp.View.Fragment.TrxManagement


import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.slnn3r.wallettrackermvp.Interface.PresenterInterface
import com.example.slnn3r.wallettrackermvp.Interface.ViewInterface
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.Transaction
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.TransactionCategory
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.WalletAccount

import com.example.slnn3r.wallettrackermvp.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_details_trx.*
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.example.slnn3r.wallettrackermvp.Utility.AlertDialog
import com.example.slnn3r.wallettrackermvp.Presenter.Presenter


class DetailsTrxFragment : Fragment(), ViewInterface.DetailsTrxView {


    private val myCalendar = Calendar.getInstance()
    private lateinit var simpleDateFormat:SimpleDateFormat

    private val mcurrentTime = Calendar.getInstance()
    private val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
    private val minute = mcurrentTime.get(Calendar.MINUTE)
    private lateinit var simpleTimeFormat:SimpleDateFormat

    private lateinit var presenter: PresenterInterface.Presenter
    private val alertDialog: AlertDialog = AlertDialog()

    private lateinit var selectionCategoryID: String
    private lateinit var selectionCategoryName: String


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.detailsTransactionFragmentTitle)

        simpleDateFormat = SimpleDateFormat(getString(R.string.dateFormat), Locale.US)
        simpleTimeFormat = SimpleDateFormat(context?.getString(R.string.timeFormat))

        return inflater.inflate(R.layout.fragment_details_trx, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = Presenter(this)
        val userProfile = presenter.getUserData(context!!)

        ///// Populate Spinner Item
        presenter.checkWalletAccount(context!!, userProfile.UserUID )


        // Receive Argumemt from Dashboard
        val transactionSelection = arguments?.getString(getString(R.string.transactionPassArgKey))

        // user GSON convert to object
        val gson = Gson()
        val transaction = gson.fromJson<Transaction>(transactionSelection, Transaction::class.java)

        selectionCategoryID= transaction.TransactionCategory.TransactionCategoryID
        selectionCategoryName= transaction.TransactionCategory.TransactionCategoryName

        ////!!!! populate AccountSpinner, then TrxType, Category based on TrxType, lastly, selectItem based on pass Arg


        // Creating adapter for TrxType spinner
        val dataAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.defaultTrxTypeSpinner))
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        DetailsTrxTypeSpinner.adapter = dataAdapter

        val spinnerPosition = dataAdapter.getPosition(transaction.TransactionCategory.TransactionCategoryType)
        DetailsTrxTypeSpinner.setSelection(spinnerPosition)




        /////

        // Setup Spinner listener
        DetailsTrxTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                if(DetailsTrxTypeSpinner.selectedItem==getString(R.string.expense)){

                    DetailsTrxTypeImageView.setImageDrawable(getResources().getDrawable(R.drawable.expense_icon))
                    DetailsTrxTypeImageView.setBackground(getResources().getDrawable(R.drawable.fui_idp_button_background_email))
                    presenter.checkTransactionCategory(context!!, userProfile.UserUID, DetailsTrxTypeSpinner.selectedItem.toString())



                }else{
                    DetailsTrxTypeImageView.setImageDrawable(getResources().getDrawable(R.drawable.income_icon))
                    DetailsTrxTypeImageView.setBackground(getResources().getDrawable(R.drawable.fui_idp_button_background_phone))
                    presenter.checkTransactionCategory(context!!, userProfile.UserUID, DetailsTrxTypeSpinner.selectedItem.toString())

                }

            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        }


        // Setup Date Picker

        val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            DetailsTrxDateInput.setText(simpleDateFormat.format(myCalendar.time))
        }

        DetailsTrxDateInput.setOnClickListener {

            DatePickerDialog(context, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show()

        }



        // Setup Time picker
        DetailsTrxTimeInput.setOnClickListener{

            val mTimePicker: TimePickerDialog

            mTimePicker = TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->

                val time = Time(selectedHour, selectedMinute, 0)
                var s = simpleTimeFormat.format(time)

                DetailsTrxTimeInput.setText(s)


            }, hour, minute, false)//Yes 24 hour time
            mTimePicker.show()

        }


        // Setup Button
        DetailsTrxUpdateSubmit.setOnClickListener{

                        // get the Transaction Category 1st, then get the wallet account id, only then start to build the NewTransaction Data
                        var selectedTrxCategory = presenter.getCategoryDataByName(context!!, userProfile.UserUID, DetailsTrxCategorySpinner.selectedItem.toString())
                        var selectedWalletAccount = presenter.getAccountDataByName(context!!, userProfile.UserUID, DetailsTrxSelectedAccSpinner.selectedItem.toString())

                        // Check if the realm return any value, if not then it is the deleted category, so just use back the deleted data
                        if(selectedTrxCategory.TransactionCategoryID==""){
                            selectedTrxCategory = transaction.TransactionCategory
                        }

                        // store 24hour in database for ez sorting purpose
                        val notConvertedTime = DetailsTrxTimeInput.text.toString()
                        val date12Format = SimpleDateFormat("hh:mm:ss a")
                        val date24Format = SimpleDateFormat("HH:mm:ss")
                        val convertedTime = date24Format.format(date12Format.parse(notConvertedTime))

                        var DetailsTrxInput =

                                Transaction(
                                        transaction.TransactionID
                                        ,DetailsTrxDateInput.text.toString()
                                        ,convertedTime
                                        ,DetailsTrxAmountInput.text.toString().toDouble()
                                        ,DetailsTrxRemarksInput.text.toString()
                                        ,selectedTrxCategory
                                        ,selectedWalletAccount
                                )

                        presenter.updateDetailsTrx(context!!, DetailsTrxInput)

        }

        DetailsTrxDeleteSubmit.setOnClickListener{
            presenter.deleteDetailsTrx(context!!, transaction.TransactionID)
        }


        // TextWatcher Validation
        DetailsTrxAmountInput.error=getString(R.string.promptToEnter)

        DetailsTrxAmountInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("","")

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                // Force 2 Decimal Input only (SOLVED)
                val text = DetailsTrxAmountInput.text.toString()
                if (text.contains(".") && text.substring(text.indexOf(".") + 1).length > 2) {
                    DetailsTrxAmountInput.setText(text.substring(0, text.length - 1))
                    DetailsTrxAmountInput.setSelection(DetailsTrxAmountInput.text.length)
                }

                val validationResult = presenter.walletAccountBalanceValidation(context!!,text)

                if(validationResult!=null){
                    DetailsTrxUpdateSubmit.isEnabled = false
                }

                DetailsTrxAmountInput.error=validationResult
            }

            override fun afterTextChanged(s: Editable?) {

                if(DetailsTrxAmountInput.error==null){
                    DetailsTrxUpdateSubmit.isEnabled = true
                }

            }

        })




        //Initial Value
        DetailsTrxTimeInput.setText(transaction.TransactionTime)
        DetailsTrxDateInput.setText(transaction.TransactionDate)
        DetailsTrxAmountInput.setText(transaction.TransactionAmount.toString())
        DetailsTrxRemarksInput.setText(transaction.TransactionRemark)






    }


    override fun populateDetailTrxCategorySpinner(mainContext: Context, trxCategoryList: ArrayList<TransactionCategory>) {

        val spinnerItem = ArrayList<String>()

        trxCategoryList.forEach {
            data ->
            spinnerItem.add(data.TransactionCategoryName)
        }

        val dataAdapter = ArrayAdapter(mainContext, android.R.layout.simple_spinner_item, spinnerItem)
        val detailsTrxCategorySpinner = (mainContext as Activity).findViewById(R.id.DetailsTrxCategorySpinner) as Spinner


        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        detailsTrxCategorySpinner.adapter = dataAdapter

        //// Check if the ID exist or not, If Exist spinner item will refer to it, else, add this selection indicated Deleted
        var tempRef=""
        trxCategoryList.forEach {
            data ->
                if(selectionCategoryID==data.TransactionCategoryID){
                    tempRef=data.TransactionCategoryName
                }
        }

        val spinnerPosition = dataAdapter.getPosition(tempRef)

        if(spinnerPosition<0){

            spinnerItem.add(selectionCategoryName+" (Deleted)")
            detailsTrxCategorySpinner.setSelection(spinnerItem.size-1)


        }else{
            detailsTrxCategorySpinner.setSelection(spinnerPosition)
        }
        ////

    }

    override fun populateDetailTrxCategorySpinnerFail(mainContext: Context, errorMessage: String) {

        Toast.makeText(mainContext,errorMessage, Toast.LENGTH_LONG).show()


    }

    override fun populateDetailTrxAccountSpinner(mainContext: Context, walletAccountList: ArrayList<WalletAccount>) {

        val categories = ArrayList<String>()

        walletAccountList.forEach {
            data->
            categories.add(data.WalletAccountName)
        }

        // Creating adapter for spinner
        val dataAdapter = ArrayAdapter(mainContext, android.R.layout.simple_spinner_item, categories)

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        val spinner = (mainContext as Activity).findViewById(R.id.DetailsTrxSelectedAccSpinner) as Spinner
        spinner.adapter = dataAdapter

        // Get SharedPreference saved Selection and Set to Spinner Selection
        val go = presenter.getSelectedAccount(mainContext)
        var count= 0
        categories.forEach {
            data ->
            if(go==data){
                spinner.setSelection(count)
            }
            count+=1
        }


    }

    override fun populateDetailTrxAccountSpinnerFail(mainContext: Context, errorMessage: String) {

        Toast.makeText(mainContext,errorMessage, Toast.LENGTH_LONG).show()

    }


    override fun updateDetailsTrxSuccess(mainContext: Context) {
        Toast.makeText(mainContext,"update", Toast.LENGTH_SHORT).show()
        (mainContext as Activity).onBackPressed()

    }

    override fun updateDetailsTrxFail(mainContext: Context, errorMessage: String) {
        Toast.makeText(mainContext,errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun deleteDetailsTrxSuccess(mainContext: Context) {

        Toast.makeText(mainContext,"delete", Toast.LENGTH_SHORT).show()
        (mainContext as Activity).onBackPressed()
    }

    override fun deleteDetailsTrxFail(mainContext: Context, errorMessage: String) {
        Toast.makeText(mainContext,errorMessage, Toast.LENGTH_LONG).show()
    }


}
