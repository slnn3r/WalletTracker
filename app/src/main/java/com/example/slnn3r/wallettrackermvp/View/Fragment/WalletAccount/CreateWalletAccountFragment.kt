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
import kotlinx.android.synthetic.main.fragment_create_wallet_account.*
import java.util.*
import com.example.slnn3r.wallettrackermvp.Utility.AlertDialog




class CreateWalletAccountFragment : Fragment(), ViewInterface.CreateWalletAccountView {


    private lateinit var presenter: PresenterInterface.Presenter
    private val alertDialog:AlertDialog= AlertDialog()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.createWalletAccountFragmentTitle)

        return inflater.inflate(R.layout.fragment_create_wallet_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = Presenter(this)

        // for database validation (no same name input)
        val userID = presenter.getUserData(context!!)
        val accountNameList = presenter.getAccountData(context!!,userID.UserUID)

        CWACreateSubmit.setOnClickListener{

            alertDialog.confirmationDialog(context!!,getString(R.string.dialogTitleCreateAccount),getString(R.string.dialogMessageCreateAccount),resources.getDrawable(android.R.drawable.ic_dialog_info),
                    DialogInterface.OnClickListener { dialogBox, which ->

                        val uniqueID = UUID.randomUUID().toString()

                        // Get SharedPreference data
                        presenter = Presenter(this)
                        val userProfile = presenter.getUserData(context!!)
                        val userID = userProfile.UserUID


                        val walletAccountInput= WalletAccount(
                                uniqueID,
                                CWAAccNameInput.text.toString(),
                                CWAAccBalanceInput.text.toString().toDouble(),
                                userID,
                                getString(R.string.statusNotDefault)
                        )

                        presenter.createWalletAccount(context!!, walletAccountInput)

                    }).show()


        }


        //// TextWatcher Validation

        CWAAccNameInput.error=getString(R.string.promptToEnter)
        CWAAccBalanceInput.error=getString(R.string.promptToEnter)

        CWAAccNameInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("","")

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                // Call Presenter, Presenter Return Boolean + Error Message
                val rex = getString(R.string.regExNoCharacterOnly).toRegex()

                if (CWAAccNameInput.length()>getString(R.string.maxAccNameInputField).toInt()){
                    CWAAccNameInput.error= getString(R.string.accNameInputErrorMaxLength)
                    CWACreateSubmit.isEnabled = false
                }else if(!CWAAccNameInput.text.toString().matches(rex)){
                    CWAAccNameInput.error= getString(R.string.accNameInputErrorInvalid)
                    CWACreateSubmit.isEnabled = false

                }else if(accountNameList.size>0) {

                    var detectMatched=0

                    accountNameList.forEach{
                        data->
                            if(data.WalletAccountName.equals(CWAAccNameInput.text.toString(),ignoreCase = true)){
                                detectMatched+=1
                            }
                    }

                    if(detectMatched>0){
                        CWAAccNameInput.error= getString(R.string.accNameUsedError)
                        CWACreateSubmit.isEnabled = false
                    }else{
                        CWAAccNameInput.error=null
                    }

                }else if(accountNameList.size==0){ //when retrieve nothing database error

                    CWAAccNameInput.error= getString(R.string.accNameRetreiveError)
                    CWACreateSubmit.isEnabled = false

                }else{
                    CWAAccNameInput.error=null
                }

            }

            override fun afterTextChanged(s: Editable?) {

                if(CWAAccNameInput.error==null&&CWAAccBalanceInput.error==null){
                    CWACreateSubmit.isEnabled = true
                }

            }

        })

        CWAAccBalanceInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("","")

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val text = CWAAccBalanceInput.text.toString()
                if (text.contains(".") && text.substring(text.indexOf(".") + 1).length > 2) {
                    CWAAccBalanceInput.setText(text.substring(0, text.length - 1))
                    CWAAccBalanceInput.setSelection(CWAAccBalanceInput.text.length)
                }         // issue, number input not 2 decimal place (SOLVED)


                if (CWAAccBalanceInput.text.toString().isEmpty()){
                    CWAAccBalanceInput.error=getString(R.string.promptToEnter)
                    CWACreateSubmit.isEnabled = false
                }else{
                    CWAAccBalanceInput.error=null

                }

            }

            override fun afterTextChanged(s: Editable?) {

                if(CWAAccNameInput.error==null&&CWAAccBalanceInput.error==null){
                    CWACreateSubmit.isEnabled = true
                }

            }

        })


        CWACreateSubmit.isEnabled = false


    }


    override fun createWalletAccountSuccess(mainContext: Context) {

        Toast.makeText(mainContext,mainContext.getString(R.string.createWalletAccountSuccess),Toast.LENGTH_LONG).show()

        (mainContext as Activity).onBackPressed()


    }

    override fun createWalletAccountFail(mainContext: Context, errorMessage: String) {

        Toast.makeText(mainContext,mainContext.getString(R.string.createWalletAccountFail)+errorMessage,Toast.LENGTH_LONG).show()


    }


}
