package com.example.slnn3r.wallettrackermvp.View.Fragment.TrxManagement


import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
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
import android.widget.Spinner
import com.example.slnn3r.wallettrackermvp.Presenter.Presenter


class DetailsTrxFragment : Fragment(), ViewInterface.DetailsTrxView {


    private val myCalendar = Calendar.getInstance()
    private val myFormat = "dd/MM/yy"
    private val simpleDateFormat = SimpleDateFormat(myFormat, Locale.US)

    private val mcurrentTime = Calendar.getInstance()
    private val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
    private val minute = mcurrentTime.get(Calendar.MINUTE)
    private val simpleTimeFormat = SimpleDateFormat("h:mma")

    private lateinit var presenter: PresenterInterface.Presenter
    private val alertDialog: AlertDialog = AlertDialog()

    private lateinit var selectionCategory: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        (activity as? AppCompatActivity)?.supportActionBar?.title = "Transaction Details"

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

        selectionCategory= transaction.TransactionCategory.TransactionCategoryName

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


                //NewTrxTimeInput.setText(selectedHour.toString() + ":" + selectedMinute)


                val time = Time(selectedHour, selectedMinute, 0)
                val s = simpleTimeFormat.format(time)

                DetailsTrxTimeInput.setText(s)


            }, hour, minute, false)//Yes 24 hour time
            mTimePicker.show()

        }


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

        val spinnerPosition = dataAdapter.getPosition(selectionCategory)
        detailsTrxCategorySpinner.setSelection(spinnerPosition)

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


}
