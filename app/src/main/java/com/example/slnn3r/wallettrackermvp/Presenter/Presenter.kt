package com.example.slnn3r.wallettrackermvp.Presenter

import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.support.v7.view.menu.MenuView
import android.view.View
import com.example.slnn3r.wallettrackermvp.Interface.PresenterInterface

import com.example.slnn3r.wallettrackermvp.Interface.ViewInterface
import com.example.slnn3r.wallettrackermvp.Interface.ModelInterface

import com.example.slnn3r.wallettrackermvp.Model.FirebaseAccess;


class Presenter: PresenterInterface.Presenter{


    private lateinit var mainView: ViewInterface.MainView
    private lateinit var loginView: ViewInterface.LoginView
    private lateinit var menuView: ViewInterface.MenuView



    private val firebaseModel: ModelInterface.FirebaseAccess = FirebaseAccess()

    constructor(mainView: ViewInterface.MainView){
        this.mainView = mainView
    }

    constructor(loginView: ViewInterface.LoginView){
        this.loginView = loginView
    }

    constructor(menuView: ViewInterface.MenuView){
        this.menuView = menuView
    }

    override fun checkLogin(mainContext: Context) {
        firebaseModel.checkLoginFirebase(mainContext)
    }

    override fun checkLoginResult(mainContext: Context, loginResult: Boolean) {

        if(loginResult){
            mainView.navigateToMenuScreen(mainContext)
        }else{
            mainView.navigateToLoginScreen(mainContext)
        }

    }


    override fun loginGoogleRequest(mainContext: Context) {
        firebaseModel.loginGoogleFirebaseRequest(mainContext)
    }

    override fun loginGoogleExecute(mainContext: Context?, requestCode: Int, resultCode: Int, data: Intent) {
        firebaseModel.loginGoogleFirebaseExecute(mainContext, requestCode,resultCode,data)
    }


    override fun loginGoogleStatus(mainContext: Context?, loginStatus: Boolean) {

        if(loginStatus){
            loginView.loginSuccess(mainContext)
        }else{
            loginView.loginFail(mainContext)

        }

    }

    override fun displayLoginFragment(mainContext: Context, fragment:FragmentActivity,intent: Intent, REQUEST_CODE_SIGN_IN: Int) {
        loginView.displayLoginOption(mainContext, fragment,intent,REQUEST_CODE_SIGN_IN)
    }



    override fun logoutGoogleExecute(mainContext: Context) {
        firebaseModel.logOutGoogleFirebase(mainContext)
    }

    override fun logoutGoogleStatus(mainContext: Context, logoutStatus: Boolean) {

        if(logoutStatus){
            menuView.logoutSuccess(mainContext)
        }else{
            menuView.logoutFail(mainContext)

        }
    }


}