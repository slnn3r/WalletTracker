package com.example.slnn3r.wallettrackermvp.Interface

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentActivity
import com.example.slnn3r.wallettrackermvp.Model.UserProfile

interface ViewInterface{

    interface MainView{

        fun navigateToLoginScreen(mainContext: Context)
        fun navigateToMenuScreen(mainContext:Context)

    }

    interface LoginView{

        fun loginSuccess(mainContext: Context?, successLoginMessage:String)
        fun loginFail(mainContext: Context?, errorMessage:String)

        fun displayLoginAccount(mainContext:Context, fragment:FragmentActivity ,intent: Intent, REQUEST_CODE_SIGN_IN:Int)

        fun displayLoginLoading(mainContext:Context): ProgressDialog
        fun dismissLoginLoading(loginLoading:ProgressDialog)


    }

    interface MenuView{

        fun logoutSuccess(mainContext: Context, successLogoutMessage:String)
        fun logoutFail(mainContext: Context, errorMessage:String)

    }

    interface DashBoardView{

        fun populateWalletAccountSpinner()
    }

}