package com.example.slnn3r.wallettrackermvp.View.Fragment.TrxCategory


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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.slnn3r.wallettrackermvp.Interface.PresenterInterface
import com.example.slnn3r.wallettrackermvp.Interface.ViewInterface
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.TransactionCategory
import com.example.slnn3r.wallettrackermvp.Presenter.Presenter

import com.example.slnn3r.wallettrackermvp.R
import com.example.slnn3r.wallettrackermvp.Utility.SelectionTrxTypeSpinnerItem
import com.example.slnn3r.wallettrackermvp.View.Activity.MenuActivity
import kotlinx.android.synthetic.main.fragment_create_trx_category.*
import java.util.*


class CreateTrxCategoryFragment : Fragment(), ViewInterface.CreateTrxCategoryView {

    private lateinit var presenter: PresenterInterface.Presenter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.createTrxCategoryFragmentTitle)


        return inflater.inflate(R.layout.fragment_create_trx_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Setup Spinner listener
        CTCTrxTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                if(CTCTrxTypeSpinner.selectedItem==getString(R.string.expense)){

                    CTCImageView.setImageDrawable(resources.getDrawable(R.drawable.expense_icon))
                    CTCImageView.background = resources.getDrawable(R.drawable.fui_idp_button_background_email)

                }else{
                    CTCImageView.setImageDrawable(resources.getDrawable(R.drawable.income_icon))
                    CTCImageView.background = resources.getDrawable(R.drawable.fui_idp_button_background_phone)

                }

            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        }


        CTCCreateSubmit.setOnClickListener(){

            val uniqueID = UUID.randomUUID().toString()

            // Get SharedPreference data
            presenter = Presenter(this)
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

        }


        //// TextWatcher Validation

        CTCCategoryNameInput.error = getString(R.string.promptToEnter)

        CTCCategoryNameInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("","")

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val rex = getString(R.string.regExNoCharacterOnly).toRegex()

                if (CTCCategoryNameInput.length()>getString(R.string.maxAccNameInputField).toInt()){
                    CTCCategoryNameInput.error= getString(R.string.accNameInputErrorMaxLength)
                    CTCCreateSubmit.isEnabled = false
                }else if(!CTCCategoryNameInput.text.toString().matches(rex)){
                    CTCCategoryNameInput.error= getString(R.string.accNameInoutErrorInvalid)
                    CTCCreateSubmit.isEnabled = false

                }else{
                    CTCCategoryNameInput.error=null
                }

            }

            override fun afterTextChanged(s: Editable?) {

                if(CTCCategoryNameInput.error==null){
                    CTCCreateSubmit.isEnabled = true
                }

            }

        })

        CTCCreateSubmit.isEnabled=false

    }


    override fun createTrxCategorySuccess(mainContext: Context) {

        Toast.makeText(mainContext,mainContext.getString(R.string.createTrxCategorySuccess), Toast.LENGTH_LONG).show()
        (mainContext as Activity).onBackPressed()
    }

    override fun createTrxCategoryFail(mainContext: Context, errorMessage: String) {
        Toast.makeText(mainContext,mainContext.getString(R.string.createTrxCategoryFail)+errorMessage,Toast.LENGTH_LONG).show()
    }
}
