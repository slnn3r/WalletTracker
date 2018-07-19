package com.example.slnn3r.wallettrackermvp.View.Fragment.TrxManagement


import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

import com.example.slnn3r.wallettrackermvp.R
import kotlinx.android.synthetic.main.fragment_new_trx.*
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.app.DatePickerDialog
import java.text.SimpleDateFormat
import java.util.*
import android.app.TimePickerDialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Spinner
import android.widget.Toast
import com.example.slnn3r.wallettrackermvp.Interface.PresenterInterface
import com.example.slnn3r.wallettrackermvp.Interface.ViewInterface
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.Transaction
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.TransactionCategory
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.WalletAccount
import com.example.slnn3r.wallettrackermvp.Presenter.Presenter
import java.sql.Time
import kotlin.collections.ArrayList


class NewTrxFragment : Fragment(), ViewInterface.NewTrxView {


    private val myCalendar = Calendar.getInstance()
    private lateinit var simpleDateFormat:SimpleDateFormat

    private val mcurrentTime = Calendar.getInstance()
    private val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
    private val minute = mcurrentTime.get(Calendar.MINUTE)
    private lateinit var simpleTimeFormat:SimpleDateFormat

    private lateinit var presenter: PresenterInterface.Presenter



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.newTrxFragmentTitle)


        simpleDateFormat = SimpleDateFormat(getString(R.string.dateFormat), Locale.US)
        simpleTimeFormat = SimpleDateFormat(context?.getString(R.string.timeFormat))

        return inflater.inflate(R.layout.fragment_new_trx, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = Presenter(this)
        val userProfile = presenter.getUserData(context!!)

        ///// Populate Account Spinner Item
        presenter.checkWalletAccount(context!!, userProfile.UserUID )




        // Creating adapter for Type spinner
        val dataAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.defaultTrxTypeSpinner))
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        NewTrxTypeSpinner.adapter = dataAdapter


        // Receive Argumemt
        val trxTypeSelection = arguments?.getString(getString(R.string.trxTypePassArgKey))


        // Set Transaction Type based on Argument
        val spinnerPosition = dataAdapter.getPosition(trxTypeSelection)
        NewTrxTypeSpinner.setSelection(spinnerPosition)




        // Setup Spinner listener
        NewTrxTypeSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                if(NewTrxTypeSpinner.selectedItem==getString(R.string.expense)){

                    NewTrxTypeImageView.setImageDrawable(resources.getDrawable(R.drawable.expense_icon))
                    NewTrxTypeImageView.background = resources.getDrawable(R.drawable.fui_idp_button_background_email)
                    NewTrxSubmit.background= resources.getDrawable(R.drawable.fui_idp_button_background_email)

                    presenter.checkTransactionCategory(context!!, userProfile.UserUID, NewTrxTypeSpinner.selectedItem.toString())

                }else{
                    NewTrxTypeImageView.setImageDrawable(resources.getDrawable(R.drawable.income_icon))
                    NewTrxTypeImageView.background = resources.getDrawable(R.drawable.fui_idp_button_background_phone)
                    NewTrxSubmit.background= resources.getDrawable(R.drawable.fui_idp_button_background_phone)

                    presenter.checkTransactionCategory(context!!, userProfile.UserUID, NewTrxTypeSpinner.selectedItem.toString())

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
            NewTrxDateInput.setText(simpleDateFormat.format(myCalendar.time))
        }

        NewTrxDateInput.setOnClickListener {

                DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show()

        }

        // Initial Date
        NewTrxDateInput.setText(simpleDateFormat.format(myCalendar.time))



        // Setup Time picker
        NewTrxTimeInput.setOnClickListener{

            val mTimePicker: TimePickerDialog

            mTimePicker = TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->


                val time = Time(selectedHour, selectedMinute, 0)
                var s = simpleTimeFormat.format(time)


                NewTrxTimeInput.setText(s)


                }, hour, minute, false)//Yes 24 hour time
                mTimePicker.show()

        }


        val second = mcurrentTime.get(Calendar.SECOND)

        // Initial Time
        val time = Time(hour, minute, second)

        //format takes in a Date, and Time is a sublcass of Date
        val s = simpleTimeFormat.format(time)
        NewTrxTimeInput.setText(s)



        // Setup Button
        NewTrxSubmit.setOnClickListener{

                        // get the Transaction Category 1st, then get the wallet account id, only then start to build the NewTransaction Data

                        var selectedTrxCategory = presenter.getCategoryDataByName(context!!, userProfile.UserUID, NewTrxCategorySpinner.selectedItem.toString())
                        var selectedWalletAccount = presenter.getAccountDataByName(context!!, userProfile.UserUID, NewTrxSelectedAccSpinner.selectedItem.toString())

                        // store 24hour in database for ez sorting purpose
                        val notConvertedTime = NewTrxTimeInput.text.toString()
                        val date12Format = SimpleDateFormat("hh:mm:ss a")
                        val date24Format = SimpleDateFormat("HH:mm:ss")
                        val convertedTime = date24Format.format(date12Format.parse(notConvertedTime))

                        val uniqueID = UUID.randomUUID().toString()
                        var newTrxInput =

                                Transaction(
                                        uniqueID,NewTrxDateInput.text.toString()
                                        ,convertedTime
                                        ,NewTrxAmountInput.text.toString().toDouble()
                                        ,NewTrxRemarksInput.text.toString()
                                        ,selectedTrxCategory
                                        ,selectedWalletAccount
                                )

                        presenter.createNewTrx(context!!,newTrxInput)


        }

        // TextWatcher Validation
        NewTrxAmountInput.error=getString(R.string.promptToEnter)

        NewTrxAmountInput.addTextChangedListener(object: TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        Log.d("","")

                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                        // Force 2 Decimal Input only (SOLVED)
                        val text = NewTrxAmountInput.text.toString()
                        if (text.contains(".") && text.substring(text.indexOf(".") + 1).length > 2) {
                            NewTrxAmountInput.setText(text.substring(0, text.length - 1))
                            NewTrxAmountInput.setSelection(NewTrxAmountInput.text.length)
                        }

                        val validationResult = presenter.walletAccountBalanceValidation(context!!,text)

                        if(validationResult!=null){
                            NewTrxSubmit.isEnabled = false
                        }

                        NewTrxAmountInput.error=validationResult
                    }

                    override fun afterTextChanged(s: Editable?) {

                        if(NewTrxAmountInput.error==null){
                            NewTrxSubmit.isEnabled = true
                        }

                    }

                })


        NewTrxSubmit.isEnabled = false

    }



    override fun populateNewTrxCategorySpinner(mainContext: Context, trxCategoryList: ArrayList<TransactionCategory>) {

        val spinnerItem = ArrayList<String>()

        trxCategoryList.forEach {
            data ->
                spinnerItem.add(data.TransactionCategoryName)
        }

        val dataAdapter = ArrayAdapter(mainContext, android.R.layout.simple_spinner_item, spinnerItem)
        val newTrxCategorySpinner = (mainContext as Activity).findViewById(R.id.NewTrxCategorySpinner) as Spinner


        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        newTrxCategorySpinner.adapter = dataAdapter




    }

    override fun populateNewTrxCategorySpinnerFail(mainContext: Context, errorMessage: String) {

        Toast.makeText(mainContext,errorMessage, Toast.LENGTH_LONG).show()

    }

    override fun populateSelectedAccountSpinner(mainContext: Context, walletAccountList: ArrayList<WalletAccount>) {

        val categories = ArrayList<String>()

        walletAccountList.forEach {
            data->
            categories.add(data.WalletAccountName)
        }

        // Creating adapter for spinner
        val dataAdapter = ArrayAdapter(mainContext, android.R.layout.simple_spinner_item, categories)

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        val spinner = (mainContext as Activity).findViewById(R.id.NewTrxSelectedAccSpinner) as Spinner
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

    override fun populateSelectedAccountSpinnerFail(mainContext: Context, errorMessage: String) {

        Toast.makeText(mainContext,errorMessage, Toast.LENGTH_LONG).show()

    }


    override fun createNewTrxSuccess(mainContext: Context) {

        Toast.makeText(mainContext,mainContext.getString(R.string.createNewTrx), Toast.LENGTH_SHORT).show()
        (mainContext as Activity).onBackPressed()


    }

    override fun createNewTrxFail(mainContext: Context, errorMessage: String) {
        Toast.makeText(mainContext,errorMessage, Toast.LENGTH_LONG).show()
    }



}
