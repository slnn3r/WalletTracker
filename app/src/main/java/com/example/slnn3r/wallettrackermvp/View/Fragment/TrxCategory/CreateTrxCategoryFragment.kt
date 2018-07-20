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
import android.widget.Toast
import com.example.slnn3r.wallettrackermvp.Interface.PresenterInterface
import com.example.slnn3r.wallettrackermvp.Interface.ViewInterface
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.TransactionCategory
import com.example.slnn3r.wallettrackermvp.Presenter.Presenter

import com.example.slnn3r.wallettrackermvp.R
import kotlinx.android.synthetic.main.fragment_create_trx_category.*
import java.util.*
import com.example.slnn3r.wallettrackermvp.Utility.AlertDialog
import kotlin.collections.ArrayList


class CreateTrxCategoryFragment : Fragment(), ViewInterface.CreateTrxCategoryView {

    private lateinit var presenter: PresenterInterface.Presenter
    private val alertDialog:AlertDialog= AlertDialog()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.createTrxCategoryFragmentTitle)

        return inflater.inflate(R.layout.fragment_create_trx_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = Presenter(this)

        // for database validation (no same name input)
        val userID = presenter.getUserData(context!!)
        val categoryNameList = presenter.getCategoryData(context!!,userID.UserUID)

        setupInitialUI() // Initial UI


        // Listener Setter
        CTCTrxTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                trxTypeSpinnerClick()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }
        }

        CTCCreateSubmit.setOnClickListener{
            createSubmitClick()
        }


        // TextWatcher Validation
        CTCCategoryNameInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("","")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateCategoryName(categoryNameList)
            }

            override fun afterTextChanged(s: Editable?) {
                validationFinalized()
            }

        })

    }



    // Function Implementation
    private fun setupInitialUI() {
        CTCCreateSubmit.isEnabled=false
        CTCCategoryNameInput.error = getString(R.string.promptToEnter)
    }

    private fun trxTypeSpinnerClick(){

        if(CTCTrxTypeSpinner.selectedItem==getString(R.string.expense)){
            CTCImageView.setImageDrawable(resources.getDrawable(R.drawable.expense_icon))
            CTCImageView.background = resources.getDrawable(R.drawable.fui_idp_button_background_email)
        }else{
            CTCImageView.setImageDrawable(resources.getDrawable(R.drawable.income_icon))
            CTCImageView.background = resources.getDrawable(R.drawable.fui_idp_button_background_phone)
        }
    }

    private fun createSubmitClick() {

        alertDialog.confirmationDialog(context!!,getString(R.string.dialogTitleCreateCategory),getString(R.string.dialogMessageCreateCategory),resources.getDrawable(android.R.drawable.ic_dialog_info),
                DialogInterface.OnClickListener { dialogBox, which ->

                    val uniqueID = UUID.randomUUID().toString()

                    // Get SharedPreference data
                    val userProfile = presenter.getUserData(context!!)
                    val userID = userProfile.UserUID

                    val trxCategoryInput= TransactionCategory(
                            uniqueID,
                            CTCCategoryNameInput.text.toString(),
                            CTCTrxTypeSpinner.selectedItem.toString(),
                            getString(R.string.statusNotDefault),
                            userID
                    )

                    presenter.createTransactionCategory(context!!, trxCategoryInput)

                }).show()
    }

    private fun validateCategoryName(categoryNameList: ArrayList<TransactionCategory>) {

        val validationResult = presenter.transactionCategoryNameValidation(context!!,CTCCategoryNameInput.text.toString(),categoryNameList,null)

        if(validationResult!=null){
            CTCCreateSubmit.isEnabled = false
        }

        CTCCategoryNameInput.error=validationResult
    }

    private fun validationFinalized(){

        if(CTCCategoryNameInput.error==null){
            CTCCreateSubmit.isEnabled = true
        }
    }




    // Presenter Callback
    override fun createTrxCategorySuccess(mainContext: Context) {

        Toast.makeText(mainContext,mainContext.getString(R.string.createTrxCategorySuccess), Toast.LENGTH_LONG).show()
        (mainContext as Activity).onBackPressed()
    }

    override fun createTrxCategoryFail(mainContext: Context, errorMessage: String) {
        Toast.makeText(mainContext,mainContext.getString(R.string.createTrxCategoryFail)+errorMessage,Toast.LENGTH_LONG).show()
    }
}
