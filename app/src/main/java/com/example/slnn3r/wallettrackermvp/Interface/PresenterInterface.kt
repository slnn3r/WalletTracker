package com.example.slnn3r.wallettrackermvp.Interface

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentActivity
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.Transaction
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.WalletAccount

interface PresenterInterface{

    interface Presenter {

        // Main Activity
        fun checkLogin(mainContext:Context)
        fun checkLoginResult(mainContext:Context, loginResult: Boolean)

        // Login activity
        fun loginGoogleRequest(mainContext:Context)
        fun loginGoogleExecute(mainContext: Context?, requestCode: Int, resultCode: Int, data: Intent)

        fun displayLoginFragment(mainContext:Context, fragment: FragmentActivity ,intent: Intent, REQUEST_CODE_SIGN_IN:Int)
        fun loginGoogleStatus(mainContext: Context?, loginStatus: Boolean, statusMessage:String, loginLoading:ProgressDialog?)

        // Menu Activity
        fun logoutGoogleExecute(mainContext: Context)
        fun logoutGoogleStatus(mainContext: Context, logourStatus:Boolean, statusMessage:String)

        // DashBoard Fragment
        fun checkWalletAccount(mainContext: Context, userID:String)
        fun checkWalletAccountResult(mainContext: Context, walletAccountList: ArrayList<WalletAccount>)

        fun firstTimeDatabaseSetup(mainContext: Context, userID:String)
        fun firstTimeSetupStatus(mainContext: Context, walletAccount: WalletAccount)

        fun checkTransaction(mainContext: Context, accountID: String)
        fun checkTransactionResult(mainContext: Context, transactionList: ArrayList<Transaction>)


    }
    
}