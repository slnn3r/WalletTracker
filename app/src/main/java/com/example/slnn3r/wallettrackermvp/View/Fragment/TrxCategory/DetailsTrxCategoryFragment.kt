package com.example.slnn3r.wallettrackermvp.View.Fragment.TrxCategory


import android.app.Activity
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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.slnn3r.wallettrackermvp.Interface.PresenterInterface
import com.example.slnn3r.wallettrackermvp.Interface.ViewInterface
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.TransactionCategory
import com.example.slnn3r.wallettrackermvp.Presenter.Presenter

import com.example.slnn3r.wallettrackermvp.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_details_trx_category.*
import com.example.slnn3r.wallettrackermvp.Utility.AlertDialog


class DetailsTrxCategoryFragment : Fragment(), ViewInterface.DetailsTrxCategoryView {


    private lateinit var presenter: PresenterInterface.Presenter
    private val alertDialog:AlertDialog  = AlertDialog()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.detailsTrxCategoryFragmentTitle)


        return inflater.inflate(R.layout.fragment_details_trx_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Creating adapter for Type spinner
        val dataAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.defaultTrxTypeSpinner))
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        DTCTrxTypeSpinner.adapter = dataAdapter

        ///////
        // Receive Argumemt
        val trxCategorySelection = arguments?.getString(getString(R.string.trxCategoryPassArgKey))

        // user GSON convert to object
        val gson = Gson()

        val trxCategory = gson.fromJson<TransactionCategory>(trxCategorySelection, TransactionCategory::class.java)


        DTCCategoryNameInput.setText(trxCategory.TransactionCategoryName)

        // Set Transaction Type based on Argument
        val spinnerPosition = dataAdapter.getPosition(trxCategory.TransactionCategoryType)
        DTCTrxTypeSpinner.setSelection(spinnerPosition)


        if(trxCategory.TransactionCategoryStatus==getString(R.string.statusDefault)){
            DTCDeleteSubmit.isEnabled = false
            DTCDeleteSubmit.text = getString(R.string.unDeletableCategory)
        }


        ///////
        presenter = Presenter(this)

        // for database validation (no same name input)
        val userID = presenter.getUserData(context!!)
        val categoryNameList = presenter.getCategoryData(context!!,userID.UserUID)

        DTCUpdateSubmit.setOnClickListener{

            alertDialog.confirmationDialog(context!!,getString(R.string.dialogTitleUpdateCategory),getString(R.string.dialogMessageUpdateCategory),resources.getDrawable(android.R.drawable.ic_dialog_info),
                    DialogInterface.OnClickListener { dialogBox, which ->

                        val trxCategoryInput = TransactionCategory(trxCategory.TransactionCategoryID, DTCCategoryNameInput.text.toString(), DTCTrxTypeSpinner.selectedItem.toString(), trxCategory.TransactionCategoryStatus, trxCategory.UserUID)
                        presenter.updateTransactionCategory(context!!, trxCategoryInput)

                    }).show()
        }


        DTCDeleteSubmit.setOnClickListener{


            alertDialog.confirmationDialog(context!!,getString(R.string.dialogTitleDeleteCategory),getString(R.string.dialogMessageDeleteCategory),resources.getDrawable(android.R.drawable.ic_dialog_alert),
                    DialogInterface.OnClickListener { dialogBox, which ->

                        presenter.deleteTransactionCategory(context!!, trxCategory.TransactionCategoryID)

                    }).show()


        }



        // Setup Spinner listener
        DTCTrxTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                if(DTCTrxTypeSpinner.selectedItem==getString(R.string.expense)){

                    DTCImageView.setImageDrawable(resources.getDrawable(R.drawable.expense_icon))
                    DTCImageView.background = resources.getDrawable(R.drawable.fui_idp_button_background_email)

                }else{
                    DTCImageView.setImageDrawable(resources.getDrawable(R.drawable.income_icon))
                    DTCImageView.background = resources.getDrawable(R.drawable.fui_idp_button_background_phone)

                }

            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        }


        //// TextWatcher Validation


        DTCCategoryNameInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("","")

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val rex = getString(R.string.regExNoCharacterOnly).toRegex()

                if (DTCCategoryNameInput.length()>getString(R.string.maxAccNameInputField).toInt()){
                    DTCCategoryNameInput.error= getString(R.string.categoryNameInputErrorMaxLength)
                    DTCUpdateSubmit.isEnabled = false
                }else if(!DTCCategoryNameInput.text.toString().matches(rex)){
                    DTCCategoryNameInput.error= getString(R.string.categoryNameInputErrorInvalid)
                    DTCUpdateSubmit.isEnabled = false

                }else if(categoryNameList.size>0) {

                    var detectMatched=0

                    categoryNameList.forEach{
                        data->
                        if(data.TransactionCategoryName.equals(DTCCategoryNameInput.text.toString(),ignoreCase = true) && data.TransactionCategoryID!=trxCategory.TransactionCategoryID){
                            detectMatched+=1
                        }

                    }

                    if(detectMatched>0){
                        DTCCategoryNameInput.error= getString(R.string.categoryNameUsedError)
                        DTCUpdateSubmit.isEnabled = false
                    }else{
                        DTCCategoryNameInput.error=null
                    }

                }else if(categoryNameList.size==0){ //when retrieve nothing database error

                    DTCCategoryNameInput.error= getString(R.string.categoryNameRetreiveError)
                    DTCUpdateSubmit.isEnabled = false

                }else{
                    DTCCategoryNameInput.error=null
                }

            }

            override fun afterTextChanged(s: Editable?) {

                if(DTCCategoryNameInput.error==null){
                    DTCUpdateSubmit.isEnabled = true
                }

            }

        })





    }



    override fun updateTrxCategorySuccess(mainContext: Context) {

        Toast.makeText(mainContext,mainContext.getString(R.string.updateTrxCategorySuccess), Toast.LENGTH_LONG).show()
        (mainContext as Activity).onBackPressed()

    }

    override fun updateTrxCategoryFail(mainContext: Context, errorMessage: String) {

        Toast.makeText(mainContext,mainContext.getString(R.string.updateTrxCategoryFail)+errorMessage,Toast.LENGTH_LONG).show()

    }

    override fun deleteTrxCategorySuccess(mainContext: Context) {

        Toast.makeText(mainContext,mainContext.getString(R.string.deleteTrxCategorySuccess), Toast.LENGTH_LONG).show()
        (mainContext as Activity).onBackPressed()
    }

    override fun deleteTrxCategoryFail(mainContext: Context, errorMessage: String) {

        Toast.makeText(mainContext,mainContext.getString(R.string.deleteTrxCategoryFail)+errorMessage,Toast.LENGTH_LONG).show()

    }




}
