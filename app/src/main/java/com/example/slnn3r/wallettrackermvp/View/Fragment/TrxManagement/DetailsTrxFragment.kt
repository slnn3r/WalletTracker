package com.example.slnn3r.wallettrackermvp.View.Fragment.TrxManagement


import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
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
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.UserProfile
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

    private var firstLaunchCheck=0 // for check if it is first launch or not

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.detailsTransactionFragmentTitle)

        simpleDateFormat = SimpleDateFormat(getString(R.string.dateFormat), Locale.US)
        simpleTimeFormat = SimpleDateFormat(context?.getString(R.string.timeFormat12))

        return inflater.inflate(R.layout.fragment_details_trx, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = Presenter(this)

        val userProfile = presenter.getUserData(context!!)

        // Populate Account Spinner Item
        presenter.checkWalletAccount(context!!, userProfile.UserUID )


        // Receive Argumemt from Dashboard
        val transactionSelection = arguments?.getString(getString(R.string.transactionPassArgKey))

        // user GSON convert to object
        val gson = Gson()
        val transaction = gson.fromJson<Transaction>(transactionSelection, Transaction::class.java)

        // Initial UI
        setupInitialUI(transaction)

        setupTrxSpinner(transaction) // Setup TrxType Spinner
        setupDatePicker() // Setup Date Picker
        setupTimePicker() // Setup Time picker


        // Listener Setter
        DetailsTrxTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                trxTypeSpinnerClick(userProfile)
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }
        }

        DetailsTrxUpdateSubmit.setOnClickListener{
            updateSubmitClick(transaction, userProfile)
        }

        DetailsTrxDeleteSubmit.setOnClickListener{
            deleteSubmitClick(transaction)
        }


        // TextWatcher Validation
        DetailsTrxAmountInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("","")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateAmountInput()
            }

            override fun afterTextChanged(s: Editable?) {
                validationFinalized()
            }
        })
    }



    // Function Implementation
    private fun setupInitialUI(transaction: Transaction) {
        selectionCategoryID= transaction.TransactionCategory.TransactionCategoryID
        selectionCategoryName= transaction.TransactionCategory.TransactionCategoryName

        //Initial Value
        DetailsTrxTimeInput.setText(transaction.TransactionTime)
        DetailsTrxDateInput.setText(transaction.TransactionDate)
        DetailsTrxAmountInput.setText(transaction.TransactionAmount.toString())
        DetailsTrxRemarksInput.setText(transaction.TransactionRemark)
    }

    private fun setupTrxSpinner(transaction: Transaction) {
        // Creating adapter for TrxType spinner
        val dataAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.defaultTrxTypeSpinner))
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        DetailsTrxTypeSpinner.adapter = dataAdapter

        val spinnerPosition = dataAdapter.getPosition(transaction.TransactionCategory.TransactionCategoryType)
        DetailsTrxTypeSpinner.setSelection(spinnerPosition)
    }

    private fun setupDatePicker() {
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
    }

    private fun setupTimePicker() {
        DetailsTrxTimeInput.setOnClickListener{

            val mTimePicker: TimePickerDialog

            mTimePicker = TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->

                val time = Time(selectedHour, selectedMinute, 0)
                val s = simpleTimeFormat.format(time)

                DetailsTrxTimeInput.setText(s)


            }, hour, minute, false)//Yes 24 hour time
            mTimePicker.show()

        }
    }

    private fun trxTypeSpinnerClick(userProfile: UserProfile) {
        if(DetailsTrxTypeSpinner.selectedItem==getString(R.string.expense)){

            DetailsTrxTypeImageView.setImageDrawable(resources.getDrawable(R.drawable.expense_icon))
            DetailsTrxTypeImageView.background = resources.getDrawable(R.drawable.fui_idp_button_background_email)
            presenter.checkTransactionCategory(context!!, userProfile.UserUID, DetailsTrxTypeSpinner.selectedItem.toString())

        }else{
            DetailsTrxTypeImageView.setImageDrawable(resources.getDrawable(R.drawable.income_icon))
            DetailsTrxTypeImageView.background = resources.getDrawable(R.drawable.fui_idp_button_background_phone)
            presenter.checkTransactionCategory(context!!, userProfile.UserUID, DetailsTrxTypeSpinner.selectedItem.toString())

        }
    }

    private fun updateSubmitClick(transaction: Transaction, userProfile: UserProfile) {

        alertDialog.confirmationDialog(context!!,getString(R.string.dialogTitleUpdateTrx),getString(R.string.dialogMessageUpdateTrx),resources.getDrawable(android.R.drawable.ic_dialog_info),
                DialogInterface.OnClickListener { dialogBox, which ->

                    // get the Transaction Category 1st, then get the wallet account id, only then start to build the NewTransaction Data
                    var selectedTrxCategory = presenter.getCategoryDataByName(context!!, userProfile.UserUID, DetailsTrxCategorySpinner.selectedItem.toString())
                    val selectedWalletAccount = presenter.getAccountDataByName(context!!, userProfile.UserUID, DetailsTrxSelectedAccSpinner.selectedItem.toString())

                    // Check if the realm return any value, if not then it is the deleted category, so just use back the deleted data
                    if(selectedTrxCategory.TransactionCategoryID==""){
                        selectedTrxCategory = transaction.TransactionCategory
                    }

                    // store 24hour in database for ez sorting purpose
                    val notConvertedTime = DetailsTrxTimeInput.text.toString()
                    val date12Format = SimpleDateFormat(getString(R.string.timeFormat12))
                    val date24Format = SimpleDateFormat(getString(R.string.timeFormat24))
                    val convertedTime = date24Format.format(date12Format.parse(notConvertedTime))

                    val detailsTrxInput =

                            Transaction(
                                    transaction.TransactionID
                                    ,DetailsTrxDateInput.text.toString()
                                    ,convertedTime
                                    ,DetailsTrxAmountInput.text.toString().toDouble()
                                    ,DetailsTrxRemarksInput.text.toString()
                                    ,selectedTrxCategory
                                    ,selectedWalletAccount
                            )

                    presenter.updateDetailsTrx(context!!, detailsTrxInput)

                }).show()

    }

    private fun deleteSubmitClick(transaction: Transaction) {

        alertDialog.confirmationDialog(context!!,getString(R.string.dialogTitleDeleteTrx),getString(R.string.dialogMessageDeleteTrx),resources.getDrawable(android.R.drawable.ic_dialog_info),
                DialogInterface.OnClickListener { dialogBox, which ->

                presenter.deleteDetailsTrx(context!!, transaction.TransactionID)
                }).show()
    }

    private fun validateAmountInput(){
        // Force 2 Decimal Input only (SOLVED)
        val text = DetailsTrxAmountInput.text.toString()
        if (text.contains(".") && text.substring(text.indexOf(".") + 1).length > 2) {
            DetailsTrxAmountInput.setText(text.substring(0, text.length - 1))
            DetailsTrxAmountInput.setSelection(DetailsTrxAmountInput.text.length)
        }

        val validationResult = presenter.transactionInputValidation(context!!,DetailsTrxAmountInput.text.toString())

        if(validationResult!=null){
            DetailsTrxUpdateSubmit.isEnabled = false
        }

        DetailsTrxAmountInput.error=validationResult
    }

    private fun validationFinalized(){
        if(DetailsTrxAmountInput.error==null){
            DetailsTrxUpdateSubmit.isEnabled = true
        }
    }



    // Presenter Callback
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

        if(spinnerPosition<0&&firstLaunchCheck<1){

            spinnerItem.add(selectionCategoryName+mainContext.getString(R.string.deletedDataIndicator))
            detailsTrxCategorySpinner.setSelection(spinnerItem.size-1)


        }else{
            detailsTrxCategorySpinner.setSelection(spinnerPosition)
        }
        ////

        firstLaunchCheck+=1

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
/*        var count= 0
        categories.forEach {
            data ->
            if(go==data){
                spinner.setSelection(count)
            }
            count+=1
        }*/


        val spinnerPosition = dataAdapter.getPosition(go)
        spinner.setSelection(spinnerPosition)

    }

    override fun populateDetailTrxAccountSpinnerFail(mainContext: Context, errorMessage: String) {

        Toast.makeText(mainContext,errorMessage, Toast.LENGTH_LONG).show()

    }


    override fun updateDetailsTrxSuccess(mainContext: Context) {
        Toast.makeText(mainContext,mainContext.getString(R.string.updateTrxDetails), Toast.LENGTH_SHORT).show()
        (mainContext as Activity).onBackPressed()

    }

    override fun updateDetailsTrxFail(mainContext: Context, errorMessage: String) {
        Toast.makeText(mainContext,errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun deleteDetailsTrxSuccess(mainContext: Context) {

        Toast.makeText(mainContext,mainContext.getString(R.string.deleteTrxDetails), Toast.LENGTH_SHORT).show()
        (mainContext as Activity).onBackPressed()
    }

    override fun deleteDetailsTrxFail(mainContext: Context, errorMessage: String) {
        Toast.makeText(mainContext,errorMessage, Toast.LENGTH_LONG).show()
    }


}
