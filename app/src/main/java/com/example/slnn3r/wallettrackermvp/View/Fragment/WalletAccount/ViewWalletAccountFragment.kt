package com.example.slnn3r.wallettrackermvp.View.Fragment.WalletAccount


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.slnn3r.wallettrackermvp.Adapter.WalletAccountAdapter
import com.example.slnn3r.wallettrackermvp.Interface.PresenterInterface
import com.example.slnn3r.wallettrackermvp.Interface.ViewInterface
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.UserProfile
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.WalletAccount
import com.example.slnn3r.wallettrackermvp.Presenter.Presenter

import com.example.slnn3r.wallettrackermvp.R
import com.example.slnn3r.wallettrackermvp.Utility.DummyDataAccListItem
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_view_wallet_account.*
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import android.os.IBinder
import android.view.inputmethod.InputMethodManager


class ViewWalletAccountFragment : Fragment(), ViewInterface.WalletAccountView {


    private lateinit var presenter: PresenterInterface.Presenter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.viewWalletAccountFragmentTitle)

        return inflater.inflate(R.layout.fragment_view_wallet_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // To Hide KeyBoard
        val inputManager = view!!
                .getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        val binder = view!!.getWindowToken()
        inputManager.hideSoftInputFromWindow(binder,
                InputMethodManager.HIDE_NOT_ALWAYS)



        WVAGoToCWA.setOnClickListener(){

            val navController = view.findNavController()
            navController.navigate(R.id.action_viewWalletAccountFragment_to_createWalletAccountFragment)

        }



        // Get SharedPreference data
        presenter = Presenter(this)
        val userProfile = presenter.getUserData(context!!)
        val userID = userProfile.UserUID


        presenter.checkWalletAccount(context!!, userID)
        presenter.checkWalletAccountCount(context!!, userID)

    }


    override fun populateWalletAccountRecycleView(mainContext: Context, walletAccountList: ArrayList<WalletAccount>) {

        val VWARecyclerView = (mainContext as Activity).findViewById(R.id.VWARecyclerView) as RecyclerView

        VWARecyclerView.layoutManager = LinearLayoutManager(context)
        VWARecyclerView.adapter = WalletAccountAdapter(walletAccountList)

    }

    override fun populateWalletAccountRecycleViewFail(mainContext: Context, errorMessage: String) {

        Toast.makeText(mainContext,errorMessage,Toast.LENGTH_LONG).show()

    }


    override fun createButtonStatus(mainContext: Context, walletAccountCount:Int) {

        val createSubmitButton = (mainContext as Activity).findViewById(R.id.WVAGoToCWA) as Button


        if(walletAccountCount<5){
            createSubmitButton.isEnabled = true

        }else{
            createSubmitButton.isEnabled = false
            createSubmitButton.text = mainContext.getString(R.string.accExceedQuota)

        }


    }

    override fun createButtonStatusFail(mainContext: Context, errorMessage: String) {
        val createSubmitButton = (mainContext as Activity).findViewById(R.id.WVAGoToCWA) as Button

        createSubmitButton.isEnabled = false

        Toast.makeText(mainContext,errorMessage,Toast.LENGTH_LONG).show()

    }


}
