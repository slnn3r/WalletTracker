package com.example.slnn3r.wallettrackermvp.Interface

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentActivity
import com.example.slnn3r.wallettrackermvp.Model.UserProfile

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
        fun checkWalletAccount()
        fun checkWallterAccountResult()

        fun syncWalletAccount()
        fun syncWalletAccountResult()

    }
    
}