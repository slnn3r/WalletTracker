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
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.Transaction
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.WalletAccount
import com.example.slnn3r.wallettrackermvp.Model.RealmAccess
import com.example.slnn3r.wallettrackermvp.View.Fragment.DashBoardFragment
import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import com.example.slnn3r.wallettrackermvp.R


class Presenter: PresenterInterface.Presenter{



    private lateinit var mainView: ViewInterface.MainView
    private lateinit var loginView: ViewInterface.LoginView
    private lateinit var menuView: ViewInterface.MenuView

    private lateinit var dashBoardView: ViewInterface.DashBoardView

    private lateinit var walletAccountView: ViewInterface.WalletAccountView
    private lateinit var createWalletAccountView: ViewInterface.CreateWalletAccountView
    private lateinit var detailsWalletAccountView: ViewInterface.DetailsWalletAccountView


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

    constructor(walletAccountView: ViewInterface.WalletAccountView){
        this.walletAccountView=walletAccountView
    }

    constructor(createWalletAccountView: ViewInterface.CreateWalletAccountView){
        this.createWalletAccountView=createWalletAccountView
    }

    constructor(detailsWalletAccountView: ViewInterface.DetailsWalletAccountView){
        this.detailsWalletAccountView=detailsWalletAccountView
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
            var errorMessage= mainContext!!.getString(R.string.loginCancel)

            val cm = mainContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

            if(!isConnected){
                errorMessage=mainContext.getString(R.string.loginCancelNoInternet)
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

    // use by both Dashboard and WalletAccount Fragment
    override fun checkWalletAccount(mainContext: Context, userID: String) {
        realmModel.checkWalletAccountRealm(mainContext,userID)
    }

    // use by both Dashboard and WalletAccount Fragment
    override fun checkWalletAccountResult(mainContext: Context, walletAccountList: java.util.ArrayList<WalletAccount>, status: String) {

        val view = (mainContext as Activity).findViewById(R.id.navMenu) as View
        val currentDestination = findNavController(view).currentDestination.id

        if(status==mainContext.getString(R.string.statusSuccess)){

            if(currentDestination==R.id.dashBoardFragment){

                if(walletAccountList.size<1){
                    dashBoardView.firstTimeSetup(mainContext)
                }else{
                    dashBoardView.populateWalletAccountSpinner(mainContext,walletAccountList)
                }

            }else if(currentDestination==R.id.viewWalletAccountFragment){

                walletAccountView.populateWalletAccountRecycleView(mainContext,walletAccountList)
            }

        }else{

            if(currentDestination==R.id.dashBoardFragment){

                dashBoardView.populateWalletAccountSpinnerFail(mainContext,status)

            }else if(currentDestination==R.id.viewWalletAccountFragment){

                walletAccountView.populateWalletAccountRecycleViewFail(mainContext,status)

            }

        }




    }

    override fun firstTimeDatabaseSetup(mainContext: Context, userID: String) {

        realmModel.firstTimeRealmSetup(mainContext,userID)

    }

    override fun firstTimeSetupStatus(mainContext: Context, walletAccount: WalletAccount, status: String) {

        if(status==mainContext.getString(R.string.statusSuccess)){
            dashBoardView.firstTimeSetupSuccess(mainContext,walletAccount)
        }else{
            dashBoardView.firstTimeSetupFail(mainContext,status)
        }




    }


    override fun checkTransaction(mainContext: Context, accountID: String) {
        realmModel.checkTransactionRealm(mainContext,accountID)
    }

    override fun checkTransactionResult(mainContext: Context, transactionList: ArrayList<Transaction>, status: String) {

        if(status==mainContext.getString(R.string.statusSuccess)){
            dashBoardView.populateTransactionRecycleView(mainContext, transactionList)

        }else{
            dashBoardView.populateTransactionRecycleViewFail(mainContext,status)
        }



    }


    // CreateWalletAccount Fragment

    override fun createWalletAccount(mainContext: Context, walletAccountInput: WalletAccount) {

        realmModel.createWalletAccountRealm(mainContext, walletAccountInput)
    }

    override fun createWalletAccountStatus(mainContext: Context, createStatus:String) {

        if(createStatus==mainContext.getString(R.string.statusSuccess)){
            createWalletAccountView.createWalletAccountSuccess(mainContext)
        }else{
            createWalletAccountView.createWalletAccountFail(mainContext,createStatus)
        }

    }



    // ViewWalletAccount Fragment

    override fun checkWalletAccountCount(mainContext: Context, userID: String) {

        realmModel.checkWalletAccountCountRealm(mainContext, userID)
    }

    override fun checkWalletAccountCountResult(mainContext: Context, walletAccountCount: Int, status: String) {

        if(status==mainContext.getString(R.string.statusSuccess)){
            walletAccountView.createButtonStatus(mainContext, walletAccountCount)

        }else{
            walletAccountView.createButtonStatusFail(mainContext,status)
        }

    }



    // DetailsWalletAccount Fragment

    override fun updateWalletAccount(mainContext: Context, walletAccountData: WalletAccount) {

        realmModel.updateWalletAccountRealm(mainContext, walletAccountData)

    }

    override fun updateWalletAccountStatus(mainContext: Context, updateStatus: String) {

        if(updateStatus==mainContext.getString(R.string.statusSuccess)){
            detailsWalletAccountView.updateWalletAccountSuccess(mainContext)

        }else{
            detailsWalletAccountView.updateWalletAccountFail(mainContext,updateStatus)
        }


    }

    override fun deleteWalletAccount(mainContext: Context, walletAccountID: String) {

        realmModel.deleteWalletAccountRealm(mainContext, walletAccountID)
    }

    override fun deleteWalletAccountStatus(mainContext: Context, deleteStatus: String) {

        if(deleteStatus==mainContext.getString(R.string.statusSuccess)){
            detailsWalletAccountView.deleteWalletAccountSuccess(mainContext)

        }else{
            detailsWalletAccountView.deleteWalletAccountFail(mainContext,deleteStatus)
        }

    }

}