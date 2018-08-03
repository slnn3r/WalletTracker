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
import kotlin.collections.ArrayList


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

        // Initial UI
        setupInitialUI()


        // Listener Setter
        CWACreateSubmit.setOnClickListener{
            createSubmitClick()
        }


        //// TextWatcher Validation
        CWAAccNameInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("","")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateAccNameInput(accountNameList)
            }

            override fun afterTextChanged(s: Editable?) {
                validationFinalized()
            }
        })

        CWAAccBalanceInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("","")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateAccBalanceInput()
            }

            override fun afterTextChanged(s: Editable?) {
                validationFinalized()
            }
        })
    }


    // Function Implementation
    private fun createSubmitClick() {
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

    private fun validateAccNameInput(accountNameList: ArrayList<WalletAccount>) {
        val validationResult = presenter.walletAccountNameValidation(context!!,CWAAccNameInput.text.toString(),accountNameList,null)

        if(validationResult!=null){
            CWACreateSubmit.isEnabled = false
        }

        CWAAccNameInput.error=validationResult
    }

    private fun validationFinalized(){
        if(CWAAccNameInput.error==null&&CWAAccBalanceInput.error==null){
            CWACreateSubmit.isEnabled = true
        }
    }

    private fun validateAccBalanceInput(){
        // Force 2 Decimal Input only (SOLVED)
        val text = CWAAccBalanceInput.text.toString()
        if (text.contains(".") && text.substring(text.indexOf(".") + 1).length > 2) {
            CWAAccBalanceInput.setText(text.substring(0, text.length - 1))
            CWAAccBalanceInput.setSelection(CWAAccBalanceInput.text.length)
        }

        val validationResult = presenter.walletAccountBalanceValidation(context!!,text)

        if(validationResult!=null){
            CWACreateSubmit.isEnabled = false
        }

        CWAAccBalanceInput.error=validationResult
    }

    private fun setupInitialUI() {
        CWAAccNameInput.error=getString(R.string.promptToEnter)
        CWAAccBalanceInput.error=getString(R.string.promptToEnter)
        CWACreateSubmit.isEnabled = false
    }


    // Presenter Callback
    override fun createWalletAccountSuccess(mainContext: Context) {

        Toast.makeText(mainContext,mainContext.getString(R.string.createWalletAccountSuccess),Toast.LENGTH_LONG).show()
        (mainContext as Activity).onBackPressed()
    }

    override fun createWalletAccountFail(mainContext: Context, errorMessage: String) {

        Toast.makeText(mainContext,mainContext.getString(R.string.createWalletAccountFail)+errorMessage,Toast.LENGTH_LONG).show()
    }
}
