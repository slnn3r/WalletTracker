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
import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation.findNavController
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.TransactionCategory
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.UserProfile
import com.example.slnn3r.wallettrackermvp.Model.SharedPreferenceAccess
import com.example.slnn3r.wallettrackermvp.R
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Callable


class Presenter: PresenterInterface.Presenter{


    private lateinit var mainView: ViewInterface.MainView
    private lateinit var loginView: ViewInterface.LoginView
    private lateinit var menuView: ViewInterface.MenuView

    private lateinit var dashBoardView: ViewInterface.DashBoardView

    private lateinit var walletAccountView: ViewInterface.WalletAccountView
    private lateinit var createWalletAccountView: ViewInterface.CreateWalletAccountView
    private lateinit var detailsWalletAccountView: ViewInterface.DetailsWalletAccountView

    private lateinit var trxCategoryView: ViewInterface.TrxCategoryView
    private lateinit var createTrxCategoryView: ViewInterface.CreateTrxCategoryView
    private lateinit var detailsTrxCategoryView: ViewInterface.DetailsTrxCategoryView

    private lateinit var newTrxView: ViewInterface.NewTrxView

    private val firebaseModel: ModelInterface.FirebaseAccess = FirebaseAccess()
    private val realmModel: ModelInterface.RealmAccess = RealmAccess()
    private val sharedPreferenceModel: ModelInterface.SharedPreference = SharedPreferenceAccess()

    private var mGoogleApiClient: GoogleApiClient? = null


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

    constructor(trxCategoryView: ViewInterface.TrxCategoryView){
        this.trxCategoryView=trxCategoryView
    }

    constructor(createTrxCategoryView: ViewInterface.CreateTrxCategoryView){
        this.createTrxCategoryView=createTrxCategoryView
    }

    constructor(detailsTrxCategoryView: ViewInterface.DetailsTrxCategoryView){
        this.detailsTrxCategoryView=detailsTrxCategoryView
    }

    constructor(newTrxView: ViewInterface.NewTrxView){
        this.newTrxView=newTrxView
    }


    // SharedPreference Retreive
    override fun getUserData(mainContext: Context): UserProfile {
        return sharedPreferenceModel.getUserData(mainContext)
    }

    // Get Data Only
    override fun getAccountData(mainContext: Context, userID: String): ArrayList<WalletAccount> {
        return realmModel.getAccountDataRealm(mainContext,userID)
    }

    override fun getCategoryData(mainContext: Context, userID: String): ArrayList<TransactionCategory> {
        return realmModel.getCategoryDataRealm(mainContext,userID)
    }


    // Wallet Account Input Validation
    override fun walletAccountNameValidation(mainContext: Context, input: String, accountNameList: ArrayList<WalletAccount>, updateID:String?): String? {

        var errorMessage:String?

        val rex = mainContext.getString(R.string.regExNoCharacterOnly).toRegex()

        if (input.length>mainContext.getString(R.string.maxAccNameInputField).toInt()){
            errorMessage= mainContext.getString(R.string.accNameInputErrorMaxLength)

        }else if(!input.matches(rex)){
            errorMessage= mainContext.getString(R.string.accNameInputErrorInvalid)

        }else if(accountNameList.size>0) {

            var detectMatched=0

            if(updateID==null){
                accountNameList.forEach{
                    data->
                    if(data.WalletAccountName.equals(input,ignoreCase = true)){
                        detectMatched+=1
                    }
                }
            }else{
                accountNameList.forEach{
                    data->
                    if(data.WalletAccountName.equals(input,ignoreCase = true)&&data.WalletAccountID!=updateID){
                        detectMatched+=1
                    }
                }
            }



            if(detectMatched>0){
                errorMessage= mainContext.getString(R.string.accNameUsedError)
            }else{
                errorMessage=null
            }

        }else if(accountNameList.size==0){ //when retrieve nothing database error

            errorMessage= mainContext.getString(R.string.accNameRetreiveError)

        }else{
            errorMessage=null
        }

        return errorMessage
    }


    override fun walletAccountBalanceValidation(mainContext: Context, input: String): String? {

        var errorMessage: String?


        if (input.isEmpty()){
            errorMessage=mainContext.getString(R.string.promptToEnter)
        }else{
            errorMessage=null
        }

        return errorMessage

    }


    // Transaction Category Input Validation
    override fun transactionCategoryNameValidation(mainContext: Context, input: String, categoryNameList: ArrayList<TransactionCategory>, updateID: String?): String? {

        var errorMessage:String?

        val rex = mainContext.getString(R.string.regExNoCharacterOnly).toRegex()

        if (input.length>mainContext.getString(R.string.maxAccNameInputField).toInt()){
            errorMessage= mainContext.getString(R.string.categoryNameInputErrorMaxLength)
        }else if(!input.matches(rex)){
            errorMessage= mainContext.getString(R.string.categoryNameInputErrorInvalid)

        }else if(categoryNameList.size>0) {

            var detectMatched=0

            if(updateID==null){
                categoryNameList.forEach{
                    data->
                    if(data.TransactionCategoryName.equals(input,ignoreCase = true)){
                        detectMatched+=1
                    }
                }
            }else{
                categoryNameList.forEach{
                    data->
                    if(data.TransactionCategoryName.equals(input,ignoreCase = true) && updateID!=data.TransactionCategoryID){
                        detectMatched+=1
                    }
                }
            }



            if(detectMatched>0){
                errorMessage= mainContext.getString(R.string.categoryNameUsedError)
            }else{
                errorMessage=null
            }

        }else if(categoryNameList.size==0){ //when retrieve nothing database error

            errorMessage= mainContext.getString(R.string.categoryNameRetreiveError)

        }else{
            errorMessage=null
        }

        return errorMessage
    }



    // Main Activity
    override fun checkLogin() {

        checkLoginObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<String> {

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(loginResult: String) {

                if(!loginResult.isEmpty()){
                    mainView.navigateToMenuScreen(loginResult)
                }else{
                    mainView.navigateToLoginScreen()
                }
            }

            override fun onError(e: Throwable) {
                mainView.checkLoginFail(e.toString())
            }

            override fun onComplete() {
            }
        })
    }

    private fun checkLoginObservable(): Observable<String>
    {
        return Observable.defer { Observable.just(firebaseModel.checkLoginFirebase())}
    }



    // Login activity
    override fun loginGoogleExecute(mainContext: Context?, requestCode: Int, resultCode: Int, data: Intent){

        if(resultCode!=0){

            if (requestCode == mainContext!!.getString(R.string.REQUEST_CODE_SIGN_IN).toInt()) {
                val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
                if (result.isSuccess) {
                    // successful -> authenticate with Firebase
                    val account = result.signInAccount
                    firebaseAuthWithGoogle(account!!, mainContext, loginView.displayLoginLoading(mainContext))
                } else {
                    // failed -> update UI
                    loginView.loginFail(mainContext,mainContext.getString(R.string.FAWGError))
                }
            }


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

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount, mainContext: Context?, loginLoading:ProgressDialog){

        val successLoginMessage = mainContext!!.getString(R.string.loginSuccess)
        val errorMessage = mainContext.getString(R.string.SIWCError)

        val mAuth = FirebaseAuth.getInstance()

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener( mainContext as Activity) { task ->    // RXJAVA ISSUE: Before Firebase Response to here, RXJava will thought the function have complete and call onNext
                    if (task.isSuccessful) {                    // So without wait for Firebase loading, the ProgressDialog will be dismiss immediately at the onNext
                        // Sign in success
                        val user = mAuth.currentUser


                        // User GSON convert object to JSON String to store to shared Preference
                        val gson = Gson()
                        val userProfile = UserProfile(user!!.uid, user.displayName.toString(), user.email.toString(), user.photoUrl.toString())
                        val json = gson.toJson(userProfile)

                        // Store to SharedPreference
                        sharedPreferenceModel.saveUserData(mainContext, json)


                        loginView.dismissLoginLoading(loginLoading)
                        loginView.loginSuccess(mainContext,successLoginMessage)


                    } else {
                        // Sign in fails
                        loginView.dismissLoginLoading(loginLoading)
                        loginView.loginFail(mainContext, errorMessage)

                    }
                }

    }




    // Menu Activity
    override fun logoutGoogleExecute(mainContext: Context) {

        val activity = mainContext as Activity
        val fragment = activity as FragmentActivity

        val errorMessage = mainContext.getString(R.string.GAPCCError)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mainContext.getString(R.string.GoogleSignInOptionKey))
                .requestEmail()
                .build()

        mGoogleApiClient = GoogleApiClient.Builder(mainContext)
                .enableAutoManage(fragment) {

                    menuView.logoutFail(mainContext,errorMessage)

                }
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        logOutGoogleFireBaseExecute(mainContext)

    }

    private fun logOutGoogleFireBaseExecute(mainContext: Context) {

        val successLoginMessage = mainContext.getString(R.string.logoutSuccess)
        var errorMessage = mainContext.getString(R.string.GSSError)

        mGoogleApiClient?.connect()
        mGoogleApiClient?.registerConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {

            override fun onConnectionSuspended(p0: Int) {
                errorMessage=mainContext.getString(R.string.GCSError)

                menuView.logoutFail(mainContext,errorMessage)


                mGoogleApiClient?.stopAutoManage(mainContext as FragmentActivity)
                mGoogleApiClient?.disconnect()
            }

            override fun onConnected(bundle: Bundle?) {

                FirebaseAuth.getInstance().signOut()
                if (mGoogleApiClient!!.isConnected) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback { status ->
                        if (status.isSuccess) {


                            // remove SharedPreference data
                            sharedPreferenceModel.removeUserData(mainContext)

                            menuView.logoutSuccess(mainContext,successLoginMessage)


                            mGoogleApiClient?.stopAutoManage(mainContext as FragmentActivity)
                            mGoogleApiClient?.disconnect()

                        }else{

                            menuView.logoutFail(mainContext,errorMessage)

                            mGoogleApiClient?.stopAutoManage(mainContext as FragmentActivity)
                            mGoogleApiClient?.disconnect()
                        }
                    }
                }
            }


        })
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

        val view = (mainContext as Activity).findViewById(R.id.navMenu) as View
        val currentDestination = findNavController(view).currentDestination.id


        checkWalletAccountObservable(mainContext, userID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ArrayList<WalletAccount>>
                {
                    override fun onSubscribe(d: Disposable)
                    {

                    }

                    override fun onNext(value: ArrayList<WalletAccount>)
                    {

                        if(currentDestination==R.id.dashBoardFragment){

                            if(value.size<1){
                                dashBoardView.firstTimeSetup(mainContext)
                            }else{
                                dashBoardView.populateWalletAccountSpinner(mainContext,value)
                            }

                        }else if(currentDestination==R.id.viewWalletAccountFragment){

                            walletAccountView.populateWalletAccountRecycleView(mainContext,value)

                        }

                    }

                    override fun onError(e: Throwable)
                    {

                        if(currentDestination==R.id.dashBoardFragment){

                            dashBoardView.populateWalletAccountSpinnerFail(mainContext,e.toString())

                        }else if(currentDestination==R.id.viewWalletAccountFragment){

                            walletAccountView.populateWalletAccountRecycleViewFail(mainContext,e.toString())

                        }

                    }

                    override fun onComplete()
                    {

                    }
                })
    }

    private fun checkWalletAccountObservable(mainContext: Context, userID: String): Observable<ArrayList<WalletAccount>>
    {
        return Observable.defer { Observable.just(realmModel.checkWalletAccountRealm(mainContext,userID)) }
    }



    override fun firstTimeDatabaseSetup(mainContext: Context, userID: String) {

        firstTimeDatabaseSetupObservable(mainContext,userID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<WalletAccount>
                {
                    override fun onSubscribe(d: Disposable)
                    {
                        // Log.d(TAG, "onSubscribe: $d")
                    }

                    override fun onNext(value: WalletAccount)
                    {
                        dashBoardView.firstTimeSetupSuccess(mainContext,value)


                    }

                    override fun onError(e: Throwable)
                    {
                        dashBoardView.firstTimeSetupFail(mainContext,e.toString())

                    }

                    override fun onComplete()
                    {
                    }
                })
    }

    private fun firstTimeDatabaseSetupObservable(mainContext: Context, userID: String): Observable<WalletAccount>
    {
        return Observable.defer { Observable.just(realmModel.firstTimeRealmSetup(mainContext,userID)) }
    }



    override fun checkTransaction(mainContext: Context, accountID: String) {

        checkTransactionObservable(mainContext, accountID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ArrayList<Transaction>>
                {
                    override fun onSubscribe(d: Disposable)
                    {
                    }

                    override fun onNext(value: ArrayList<Transaction>)
                    {
                        dashBoardView.populateTransactionRecycleView(mainContext, value)


                    }

                    override fun onError(e: Throwable)
                    {
                        dashBoardView.populateTransactionRecycleViewFail(mainContext,e.toString())

                    }

                    override fun onComplete()
                    {
                    }
                })

    }

    private fun checkTransactionObservable(mainContext: Context, accountID: String): Observable<ArrayList<Transaction>>
    {
        return Observable.defer { Observable.just(realmModel.checkTransactionRealm(mainContext,accountID)) }
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



    // ViewTrxCategory Fragment
    override fun checkTransactionCategory(mainContext: Context, userID: String, filterSelection: String) {
        realmModel.checkTransactionCategoryRealm(mainContext,userID,filterSelection)
    }

    override fun checkTransactionCategoryResult(mainContext: Context, transactionCategoryList: ArrayList<TransactionCategory>, status: String) {

        val view = (mainContext as Activity).findViewById(R.id.navMenu) as View
        val currentDestination = findNavController(view).currentDestination.id

        if(status==mainContext.getString(R.string.statusSuccess)){

            if(currentDestination==R.id.viewTrxCategoryFragment){
                trxCategoryView.populateTrxCategoryRecycleView(mainContext, transactionCategoryList)

            }else if(currentDestination==R.id.newTrxFragment){
                newTrxView.populateNewTrxCategorySpinner(mainContext, transactionCategoryList)
            }


        }else{

            if(currentDestination==R.id.viewTrxCategoryFragment){
                trxCategoryView.populateTrxCategoryRecycleViewFail(mainContext,status)

            }else if(currentDestination==R.id.newTrxFragment){
                newTrxView.populateNewTrxCategorySpinnerFail(mainContext,status)

            }
        }

    }


    // CreateTrxCategory Fragment
    override fun createTransactionCategory(mainContext: Context, trxCategoryInput: TransactionCategory) {
        realmModel.createTransactionCategoryRealm(mainContext,trxCategoryInput)
    }

    override fun createTransactionCategoryStatus(mainContext: Context, createStatus: String) {

        if(createStatus==mainContext.getString(R.string.statusSuccess)){
            createTrxCategoryView.createTrxCategorySuccess(mainContext)

        }else{
            createTrxCategoryView.createTrxCategoryFail(mainContext,createStatus)
        }

    }

    // DetailsTrxCategory Fragment
    override fun updateTransactionCategory(mainContext: Context, trxCategoryInput: TransactionCategory) {

        realmModel.updateTransactionCategoryRealm(mainContext, trxCategoryInput)

    }

    override fun updateTransactionCategoryStatus(mainContext: Context, updateStatus: String) {

        if(updateStatus==mainContext.getString(R.string.statusSuccess)){
            detailsTrxCategoryView.updateTrxCategorySuccess(mainContext)

        }else{
            detailsTrxCategoryView.updateTrxCategoryFail(mainContext,updateStatus)
        }

    }

    override fun deleteTransactionCategory(mainContext: Context, trxCategoryID: String) {

       realmModel.deleteTransactionCategoryRealm(mainContext,trxCategoryID)

    }

    override fun deleteTransactionCategoryStatus(mainContext: Context, deleteStatus: String) {

        if(deleteStatus==mainContext.getString(R.string.statusSuccess)){
            detailsTrxCategoryView.deleteTrxCategorySuccess(mainContext)

        }else{
            detailsTrxCategoryView.deleteTrxCategoryFail(mainContext,deleteStatus)
        }
    }

}