package com.example.slnn3r.wallettrackermvp.Presenter

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.support.v4.app.FragmentActivity
import com.example.slnn3r.wallettrackermvp.Interface.PresenterInterface
import com.example.slnn3r.wallettrackermvp.Interface.ViewInterface
import com.example.slnn3r.wallettrackermvp.Interface.ModelInterface
import com.example.slnn3r.wallettrackermvp.Model.FirebaseAccess
import android.net.NetworkInfo
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.WalletAccount
import com.example.slnn3r.wallettrackermvp.Model.RealmAccess
import com.example.slnn3r.wallettrackermvp.View.Fragment.DashBoardFragment


class Presenter: PresenterInterface.Presenter{


    private lateinit var mainView: ViewInterface.MainView
    private lateinit var loginView: ViewInterface.LoginView
    private lateinit var menuView: ViewInterface.MenuView

    private lateinit var dashBoardView: ViewInterface.DashBoardView


    private val firebaseModel: ModelInterface.FirebaseAccess = FirebaseAccess()
    private val realmModel: ModelInterface.RealmAccess = RealmAccess()


    constructor(mainView: ViewInterface.MainView){
        this.mainView = mainView
    }

    constructor(loginView: ViewInterface.LoginView){
        this.loginView = loginView
    }

    constructor(menuView: ViewInterface.MenuView){
        this.menuView = menuView
    }

    constructor(dashBoardView: ViewInterface.DashBoardView){
        this.dashBoardView=dashBoardView
    }

    // Main Activity
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


    // Login activity
    override fun loginGoogleRequest(mainContext: Context) {
        firebaseModel.loginGoogleFirebaseRequest(mainContext)
    }

    override fun loginGoogleExecute(mainContext: Context?, requestCode: Int, resultCode: Int, data: Intent){

        if(resultCode!=0){
            firebaseModel.loginGoogleFirebaseExecute(mainContext, requestCode,resultCode,data,loginView.displayLoginLoading(mainContext!!))
        }else{
            var errorMessage= "Login Cancelled"

            val cm = mainContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

            if(!isConnected){
                errorMessage="Login Cancelled - No Internet Connection"
            }

            loginView.loginFail(mainContext, errorMessage)

        }

    }

    override fun loginGoogleStatus(mainContext: Context?, loginStatus: Boolean, statusMessage: String, loginloading: ProgressDialog?) {

        if(loginStatus){
            loginView.dismissLoginLoading(loginloading!!)
            loginView.loginSuccess(mainContext, statusMessage)


        }else{
            loginView.dismissLoginLoading(loginloading!!)
            loginView.loginFail(mainContext, statusMessage)

        }

    }

    override fun displayLoginFragment(mainContext: Context, fragment:FragmentActivity,intent: Intent, REQUEST_CODE_SIGN_IN: Int) {
        loginView.displayLoginAccount(mainContext, fragment,intent,REQUEST_CODE_SIGN_IN)
    }


    // Menu Activity
    override fun logoutGoogleExecute(mainContext: Context) {
        firebaseModel.logOutGoogleFirebase(mainContext)
    }

    override fun logoutGoogleStatus(mainContext: Context, logoutStatus: Boolean, statusMessage: String) {

        if(logoutStatus){
            menuView.logoutSuccess(mainContext,statusMessage)
        }else{
            menuView.logoutFail(mainContext,statusMessage)

        }
    }


    // DashBoard Fragment

    // In Progress
    override fun checkWalletAccount(mainContext: Context, userID: String) {
        realmModel.checkWalletAccountRealm(mainContext,userID)
    }

    override fun checkWalletAccountResult(mainContext: Context, walletAccountList: ArrayList<WalletAccount>) {

        if(walletAccountList.size<1){
            dashBoardView.firstTimeSetup(mainContext)
        }else{
            dashBoardView.populateWalletAccountSpinner(mainContext,walletAccountList)
        }

    }

    override fun firstTimeDatabaseSetup(mainContext: Context, userID: String) {

        realmModel.firstTimeRealmSetup(mainContext,userID)

    }

    override fun firstTimeSetupStatus(mainContext: Context, walletAccount: WalletAccount) {

        dashBoardView.firstTimeComplete(mainContext,walletAccount)
    }




}