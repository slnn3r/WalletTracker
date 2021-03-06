package com.example.slnn3r.wallettrackermvp.Interface

import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentActivity

interface PresenterInterface{

    interface Presenter {

        fun checkLogin(mainContext:Context)
        fun checkLoginResult(mainContext:Context, loginResult: Boolean)

        fun loginGoogleRequest(mainContext:Context)
        fun loginGoogleExecute(mainContext: Context?, requestCode: Int, resultCode: Int, data: Intent)

        fun displayLoginFragment(mainContext:Context, fragment: FragmentActivity ,intent: Intent, REQUEST_CODE_SIGN_IN:Int)
        fun loginGoogleStatus(mainContext: Context?, loginStatus: Boolean)

        fun logoutGoogleExecute(mainContext: Context)
        fun logoutGoogleStatus(mainContext: Context, logourStatus:Boolean)

    }
    
}