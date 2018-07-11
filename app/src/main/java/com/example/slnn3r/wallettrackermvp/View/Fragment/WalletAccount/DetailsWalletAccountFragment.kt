package com.example.slnn3r.wallettrackermvp.View.Fragment.WalletAccount


import android.app.Activity
import android.content.Context
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

class DetailsWalletAccountFragment : Fragment(), ViewInterface.DetailsWalletAccountView {


    private lateinit var presenter: PresenterInterface.Presenter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        (activity as? AppCompatActivity)?.supportActionBar?.title = "Wallet Account Details"


        return inflater.inflate(R.layout.fragment_details_wallet_account, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ///////
        // Receive Argumemt
        val WalletAccountSelection = arguments?.getString("walletAccountSelection")

        // user GSON convert to object
        val gson = Gson()

        val walletAccount = gson.fromJson<WalletAccount>(WalletAccountSelection, WalletAccount::class.java)


        DWAAccNameInput.setText(walletAccount.WalletAccountName.toString())
        DWAAccBalanceInput.setText(walletAccount.WalletAccountInitialBalance.toString())

        if(walletAccount.WalletAccountStatus=="Default"){
            DWADeleteSubmit.isEnabled = false
            DWADeleteSubmit.text = "Cannot Delete Default Account"
        }

        ///////

        presenter = Presenter(this)

        DWAUpdateSubmit.setOnClickListener(){

            val walletAccountInput = WalletAccount(walletAccount.WalletAccountID,DWAAccNameInput.text.toString(),DWAAccBalanceInput.text.toString().toDouble(),walletAccount.userUID,walletAccount.WalletAccountStatus)

            presenter.updateWalletAccount(context!!, walletAccountInput)
        }

        DWADeleteSubmit.setOnClickListener(){

            presenter.deleteWalletAccount(context!!, walletAccount.WalletAccountID)

        }


        //// TextWatcher Validation

        DWAAccNameInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("","")

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


                val rex = "^[a-zA-Z\\s]+".toRegex()

                if (DWAAccNameInput.length()>15){
                    DWAAccNameInput.error="Max length 15 Character only"
                    DWAUpdateSubmit.isEnabled = false
                }else if(!DWAAccNameInput.text.toString().matches(rex)){
                    DWAAccNameInput.error="Invalid Name"
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
                    DWAAccBalanceInput.error="Please enter a value"
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

        Toast.makeText(mainContext,"Update Complete",Toast.LENGTH_LONG).show()
        (mainContext as Activity).onBackPressed()

    }

    override fun updateWalletAccountFail(mainContext: Context, errorMessage: String) {

        Toast.makeText(mainContext,"Update "+errorMessage,Toast.LENGTH_LONG).show()

    }

    override fun deleteWalletAccountSuccess(mainContext: Context) {

        Toast.makeText(mainContext,"Delete Complete",Toast.LENGTH_LONG).show()
        (mainContext as Activity).onBackPressed()
    }

    override fun deleteWalletAccountFail(mainContext: Context, errorMessage: String) {
        Toast.makeText(mainContext,"Delete "+errorMessage,Toast.LENGTH_LONG).show()
    }

}
