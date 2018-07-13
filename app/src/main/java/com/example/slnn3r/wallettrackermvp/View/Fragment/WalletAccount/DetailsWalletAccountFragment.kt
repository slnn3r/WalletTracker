package com.example.slnn3r.wallettrackermvp.View.Fragment.WalletAccount


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
import android.widget.Toast
import com.example.slnn3r.wallettrackermvp.Interface.PresenterInterface
import com.example.slnn3r.wallettrackermvp.Interface.ViewInterface
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.WalletAccount
import com.example.slnn3r.wallettrackermvp.Presenter.Presenter

import com.example.slnn3r.wallettrackermvp.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_details_wallet_account.*
import com.example.slnn3r.wallettrackermvp.Utility.AlertDialog


class DetailsWalletAccountFragment : Fragment(), ViewInterface.DetailsWalletAccountView {


    private lateinit var presenter: PresenterInterface.Presenter
    private val alertDialog:AlertDialog= AlertDialog()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.detailsWalletAccountFragmentTitle)

        return inflater.inflate(R.layout.fragment_details_wallet_account, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ///////
        // Receive Argumemt
        val walletAccountSelection = arguments?.getString(getString(R.string.walletAccountPassArgKey))

        // user GSON convert to object
        val gson = Gson()

        val walletAccount = gson.fromJson<WalletAccount>(walletAccountSelection, WalletAccount::class.java)

        DWAAccNameInput.setText(walletAccount.WalletAccountName)
        DWAAccBalanceInput.setText(walletAccount.WalletAccountInitialBalance.toString())

        if(walletAccount.WalletAccountStatus==getString(R.string.statusDefault)){
            DWADeleteSubmit.isEnabled = false
            DWADeleteSubmit.text = getString(R.string.unDeletableAccount)
        }

        ///////

        presenter = Presenter(this)

        // for database validation (no same name input)
        val userID = presenter.getUserData(context!!)
        val accountNameList = presenter.getAccountData(context!!,userID.UserUID)

        DWAUpdateSubmit.setOnClickListener{

            alertDialog.confirmationDialog(context!!,getString(R.string.dialogTitleUpdateAccount),getString(R.string.dialogMessageUpdateAccount),resources.getDrawable(android.R.drawable.ic_dialog_info),
                    DialogInterface.OnClickListener { dialogBox, which ->

                        val walletAccountInput = WalletAccount(walletAccount.WalletAccountID,DWAAccNameInput.text.toString(),DWAAccBalanceInput.text.toString().toDouble(),walletAccount.UserUID,walletAccount.WalletAccountStatus)

                        presenter.updateWalletAccount(context!!, walletAccountInput)

                    }).show()

        }

        DWADeleteSubmit.setOnClickListener{

            alertDialog.confirmationDialog(context!!,getString(R.string.dialogTitleDeleteAccount),getString(R.string.dialogMessageDeleteAccount),resources.getDrawable(android.R.drawable.ic_dialog_alert),
                    DialogInterface.OnClickListener { dialogBox, which ->

                        presenter.deleteWalletAccount(context!!, walletAccount.WalletAccountID)

                    }).show()

        }


        //// TextWatcher Validation

        DWAAccNameInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("","")

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {



                val rex = getString(R.string.regExNoCharacterOnly).toRegex()

                if (DWAAccNameInput.length()>15){
                    DWAAccNameInput.error=getString(R.string.accNameInputErrorMaxLength)
                    DWAUpdateSubmit.isEnabled = false
                }else if(!DWAAccNameInput.text.toString().matches(rex)){
                    DWAAccNameInput.error=getString(R.string.accNameInputErrorInvalid)
                    DWAUpdateSubmit.isEnabled = false

                }else if(accountNameList.size>0) {

                    var detectMatched=0

                    accountNameList.forEach{
                        data->
                        if(data.WalletAccountName.equals(DWAAccNameInput.text.toString(),ignoreCase = true) && data.WalletAccountID!=walletAccount.WalletAccountID){
                            detectMatched+=1
                        }
                    }

                    if(detectMatched>0){
                        DWAAccNameInput.error= getString(R.string.accNameUsedError)
                        DWAUpdateSubmit.isEnabled = false
                    }else{
                        DWAAccNameInput.error=null
                    }

                }else if(accountNameList.size==0){ //when retrieve nothing database error

                    DWAAccNameInput.error= getString(R.string.accNameRetreiveError)
                    DWAUpdateSubmit.isEnabled = false

                }else{
                    DWAAccNameInput.error=null
                }

            }

            override fun afterTextChanged(s: Editable?) {

                if(DWAAccNameInput.error==null && DWAAccBalanceInput.error==null){
                    DWAUpdateSubmit.isEnabled = true

                }

            }

        })

        DWAAccBalanceInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("","")

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val text = DWAAccBalanceInput.text.toString()
                if (text.contains(".") && text.substring(text.indexOf(".") + 1).length > 2) {
                    DWAAccBalanceInput.setText(text.substring(0, text.length - 1))
                    DWAAccBalanceInput.setSelection(DWAAccBalanceInput.text.length)
                }         // issue, number input not 2 decimal place (SOLVED)


                if (DWAAccBalanceInput.text.toString().isEmpty()){
                    DWAAccBalanceInput.error=getString(R.string.promptToEnter)
                    DWAUpdateSubmit.isEnabled = false
                }else{
                    DWAAccBalanceInput.error=null

                }


            }

            override fun afterTextChanged(s: Editable?) {

                if(DWAAccNameInput.error==null && DWAAccBalanceInput.error==null){
                    DWAUpdateSubmit.isEnabled = true

                }


            }

        })


    }


    override fun updateWalletAccountSuccess(mainContext: Context) {

        Toast.makeText(mainContext,mainContext.getString(R.string.updateWalletAccountSuccess),Toast.LENGTH_LONG).show()
        (mainContext as Activity).onBackPressed()

    }

    override fun updateWalletAccountFail(mainContext: Context, errorMessage: String) {

        Toast.makeText(mainContext,mainContext.getString(R.string.updateWalletAccountFail)+errorMessage,Toast.LENGTH_LONG).show()

    }

    override fun deleteWalletAccountSuccess(mainContext: Context) {

        Toast.makeText(mainContext,mainContext.getString(R.string.deleteWalletAccountSuccess),Toast.LENGTH_LONG).show()
        (mainContext as Activity).onBackPressed()
    }

    override fun deleteWalletAccountFail(mainContext: Context, errorMessage: String) {
        Toast.makeText(mainContext,mainContext.getString(R.string.deleteWalletAccountFail)+errorMessage,Toast.LENGTH_LONG).show()
    }

}
