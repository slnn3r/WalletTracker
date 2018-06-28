package com.example.slnn3r.wallettrackermvp.Interface

import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentActivity

interface ViewInterface{

    interface MainView{

        fun navigateToLoginScreen(mainContext: Context)
        fun navigateToMenuScreen(mainContext:Context)

    }

    interface LoginView{

        fun loginSuccess(mainContext: Context?, successLoginMessage:String)
        fun loginFail(mainContext: Context?, errorMessage:String)

        fun displayLoginAccount(mainContext:Context, fragment:FragmentActivity ,intent: Intent, REQUEST_CODE_SIGN_IN:Int)

    }

    interface MenuView{

        fun logoutSuccess(mainContext: Context, successLogoutMessage:String)
        fun logoutFail(mainContext: Context, errorMessage:String)

    }

}