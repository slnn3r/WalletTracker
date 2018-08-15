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
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.Navigation.findNavController
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.TransactionCategory
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.UserProfile
import com.example.slnn3r.wallettrackermvp.Model.SharedPreferenceAccess
import com.example.slnn3r.wallettrackermvp.R
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Presenter: PresenterInterface.Presenter{
    /*override fun retrieveData(mainContext: Context, userID: String) {


        firebaseModel.retrieveDataFirebase(mainContext, userID)

        loginView.syncDataSuccess(mainContext)

    }*/
    override fun retrieveData(mainContext: Context, userID: String) {

        retrieveDataObservable(mainContext, userID)
        .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Unit> {

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(value: Unit) {
                        Log.e("load","Done")

                        loginView.syncDataSuccess(mainContext)

                    }

                    override fun onError(e: Throwable) {
                        loginView.syncDataFail(mainContext, e.toString())
                    }

                    override fun onComplete() {
                    }
                })
    }

    private fun retrieveDataObservable(mainContext: Context, userID: String): Observable<Unit>{
        return Observable.defer { Observable.just(firebaseModel.retrieveDataFirebase(mainContext, userID))}
    }


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
    private lateinit var detailsTrxView: ViewInterface.DetailsTrxView

    private lateinit var trxHistorySpecificView: ViewInterface.TrxHistorySpecificView
    private lateinit var trxHistoryRangeView: ViewInterface.TrxHistoryRangeView

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

    constructor(detailsTrxView: ViewInterface.DetailsTrxView){
        this.detailsTrxView=detailsTrxView
    }

    constructor(trxHistorySpecificView: ViewInterface.TrxHistorySpecificView){
        this.trxHistorySpecificView=trxHistorySpecificView
    }

    constructor(trxHistoryRangeView: ViewInterface.TrxHistoryRangeView){
        this.trxHistoryRangeView=trxHistoryRangeView
    }



    // SharedPreference Retreive
    override fun getUserData(mainContext: Context): UserProfile {
        return sharedPreferenceModel.getUserData(mainContext)
    }

    override fun setSelectedAccount(mainContext: Context, selection: String) {
        sharedPreferenceModel.saveSelectedAccount(mainContext,selection)
    }

    override fun getSelectedAccount(mainContext: Context): String {
        return sharedPreferenceModel.getSelectedAccountData(mainContext)
    }

    override fun getTransactionData(mainContext: Context, userID: String): ArrayList<Transaction> {
        return realmModel.getTransactionDataRealm(mainContext,userID)
    }



    // Get Data Only
    override fun getAccountData(mainContext: Context, userID: String): ArrayList<WalletAccount> {
        return realmModel.getAccountDataRealm(mainContext,userID)
    }

    override fun getCategoryData(mainContext: Context, userID: String): ArrayList<TransactionCategory> {
        return realmModel.getCategoryDataRealm(mainContext,userID)
    }

    override fun getAccountDataByName(mainContext: Context, userID: String, accountName: String): WalletAccount {
        return realmModel.getAccountDataByNameRealm(mainContext, userID, accountName)
    }

    override fun getCategoryDataByName(mainContext: Context, userID: String, categoryName: String): TransactionCategory {
        return realmModel.getCategoryDataByNameRealm(mainContext, userID, categoryName)
    }




    // Wallet Account Input Validation
    override fun walletAccountNameValidation(mainContext: Context, input: String, accountNameList: ArrayList<WalletAccount>, updateID:String?): String? {

        val errorMessage:String?

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


            errorMessage = if(detectMatched>0){
                mainContext.getString(R.string.accNameUsedError)
            }else{
                null
            }

        }else if(accountNameList.size==0){ //when retrieve nothing database error

            errorMessage= mainContext.getString(R.string.accNameRetreiveError)

        }else{
            errorMessage=null
        }

        return errorMessage
    }


    override fun walletAccountBalanceValidation(mainContext: Context, input: String): String? {

        return if (input.isEmpty()){
            mainContext.getString(R.string.promptToEnter)
        }else{
            null
        }
    }


    // Transaction Category Input Validation
    override fun transactionCategoryNameValidation(mainContext: Context, input: String, categoryNameList: ArrayList<TransactionCategory>, updateID: String?): String? {

        val errorMessage:String?

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


            errorMessage = if(detectMatched>0){
                mainContext.getString(R.string.categoryNameUsedError)
            }else{
                null
            }

        }else if(categoryNameList.size==0){ //when retrieve nothing database error

            errorMessage= mainContext.getString(R.string.categoryNameRetreiveError)

        }else{
            errorMessage=null
        }

        return errorMessage
    }

    // NewTrx+DetailsTrx TrxInput Validation
    override fun transactionInputValidation(mainContext: Context, input: String): String? {

        return if (input.isEmpty()){
            mainContext.getString(R.string.promptToEnter)
        }else if(input.toDoubleOrNull()==null){
            mainContext.getString(R.string.noZeroInput)

        }else if(input.toDoubleOrNull()!=null){

            return if(input.toDouble()<=0.0){
                mainContext.getString(R.string.noZeroInput)
            }else{
                null
            }

        }else{
            null
        }

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

    private fun checkLoginObservable(): Observable<String>{
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
    override fun backupDataManually(mainContext: Context, userID: String) {

        backupDataManuallyObservable(mainContext,userID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Unit>
                {
                    override fun onSubscribe(d: Disposable)
                    {
                    }

                    override fun onNext(value: Unit)
                    {
                        // Store to Value Shared Preference HERE

                        menuView.backupDataSuccess(mainContext)
                    }

                    override fun onError(e: Throwable)
                    {
                        menuView.backupDataFail(mainContext,e.toString())
                    }

                    override fun onComplete()
                    {
                    }
                })
    }

    private fun backupDataManuallyObservable(mainContext: Context, userID:String): Observable<Unit>{
        return Observable.defer { Observable.just(firebaseModel.backupDataManuallyFirebase(mainContext,userID)) }
    }

    override fun backupDataPeriodically(mainContext: Context, userID: String) {

        backupDataPeriodicallyObservable(mainContext,userID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Unit>
                {
                    override fun onSubscribe(d: Disposable)
                    {
                    }

                    override fun onNext(value: Unit)
                    {
                        // Store to Value Shared Preference HERE

                        menuView.startPeriodicBackupSuccess(mainContext)
                    }

                    override fun onError(e: Throwable)
                    {
                        menuView.startPeriodicBackupFail(mainContext,e.toString())
                    }

                    override fun onComplete()
                    {
                    }
                })
    }

    private fun backupDataPeriodicallyObservable(mainContext: Context, userID:String): Observable<Unit>{
        return Observable.defer { Observable.just(firebaseModel.backupDataPeriodicallyFirebase(mainContext,userID)) }
    }


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

    // use by both Dashboard and WalletAccount Fragment + TrxHistoryFragment + newTrx+ DetailsTrx
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

                        }else if(currentDestination==R.id.newTrxFragment ){

                            newTrxView.populateSelectedAccountSpinner(mainContext,value)

                        }else if(currentDestination==R.id.detailsTrxFragment){
                            detailsTrxView.populateDetailTrxAccountSpinner(mainContext,value)

                        }else if(currentDestination==R.id.trxHistoryFragment){

                            val fragmentView = mainContext.findViewById(R.id.trxHistoryFragmentNavMenu) as View
                            val fragmentDestination = findNavController(fragmentView).currentDestination.id

                            if(fragmentDestination==R.id.trxHistorySpecificDateFragment){
                                trxHistorySpecificView.populateTrxHistorySpecificAccountSpinner(mainContext,value)

                            }else if(fragmentDestination==R.id.trxHistoryRangeDateFragment){
                                trxHistoryRangeView.populateTrxHistoryRangeAccountSpinner(mainContext,value)

                            }else if(fragmentDestination==R.id.detailsTrxFragmentTrxHistory){  //?!!
                                detailsTrxView.populateDetailTrxAccountSpinner(mainContext, value)
                            }

                        }
                    }

                    override fun onError(e: Throwable)
                    {

                        if(currentDestination==R.id.dashBoardFragment){
                            dashBoardView.populateWalletAccountSpinnerFail(mainContext,e.toString())

                        }else if(currentDestination==R.id.viewWalletAccountFragment){
                            walletAccountView.populateWalletAccountRecycleViewFail(mainContext,e.toString())

                        }else if(currentDestination==R.id.newTrxFragment){
                            newTrxView.populateSelectedAccountSpinnerFail(mainContext,e.toString())

                        }else if(currentDestination==R.id.detailsTrxFragment){
                            detailsTrxView.populateDetailTrxAccountSpinnerFail(mainContext,e.toString())

                        }else if(currentDestination==R.id.trxHistoryFragment){

                            val fragmentView = mainContext.findViewById(R.id.trxHistoryFragmentNavMenu) as View
                            val fragmentDestination = findNavController(fragmentView).currentDestination.id

                            if(fragmentDestination==R.id.trxHistorySpecificDateFragment){
                                trxHistorySpecificView.populateTrxHistorySpecificAccountSpinnerFail(mainContext,e.toString())

                            }else if(fragmentDestination==R.id.trxHistoryRangeDateFragment){
                                trxHistoryRangeView.populateTrxHistoryRangeAccountSpinnerFail(mainContext,e.toString())

                            }else if(fragmentDestination==R.id.detailsTrxFragmentTrxHistory){  //?!!
                                detailsTrxView.populateDetailTrxAccountSpinnerFail(mainContext, e.toString())
                            }

                        }
                    }

                    override fun onComplete()
                    {
                    }
                })
    }

    private fun checkWalletAccountObservable(mainContext: Context, userID: String): Observable<ArrayList<WalletAccount>>{
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

    private fun firstTimeDatabaseSetupObservable(mainContext: Context, userID: String): Observable<WalletAccount>{
        return Observable.defer { Observable.just(realmModel.firstTimeRealmSetup(mainContext,userID)) }
    }



    override fun checkTransaction(mainContext: Context, accountID: String, userID: String) {

        checkTransactionObservable(mainContext, accountID, userID)
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

    private fun checkTransactionObservable(mainContext: Context, accountID: String, userID: String): Observable<ArrayList<Transaction>>{
        return Observable.defer { Observable.just(realmModel.checkTransactionRealm(mainContext,accountID,userID)) }
    }


    override fun getAllIncome(mainContext: Context, userID: String, accountID: String) {

        val accountBalance = realmModel.getCurrentBalanceRealm(mainContext, userID, accountID)
        val allExpenses = realmModel.getAllExpense(mainContext,userID,accountID)

        getAllIncomeObservable(mainContext, userID, accountID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Double>
                {
                    override fun onSubscribe(d: Disposable)
                    {
                    }

                    override fun onNext(value: Double)
                    {
                        dashBoardView.populateCurrentBalance(mainContext, value+accountBalance-allExpenses)
                    }

                    override fun onError(e: Throwable)
                    {
                    }

                    override fun onComplete()
                    {
                    }
                })
    }

    private fun getAllIncomeObservable(mainContext: Context, userID: String, accountID: String): Observable<Double>{
        return Observable.defer { Observable.just(realmModel.getAllIncome(mainContext,userID,accountID)) }
    }


    override fun getThisMonthExpense(mainContext: Context, userID: String, accountID: String, thisMonth: String) {

        getThisMonthExpenseObservable(mainContext, userID, accountID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer< ArrayList<Transaction>>
                {
                    override fun onSubscribe(d: Disposable)
                    {
                    }

                    override fun onNext(value:  ArrayList<Transaction>)
                    {

                        var expenses=0.0

                        value.forEach { dataList ->

                            val sdf = SimpleDateFormat(mainContext.getString(R.string.dateFormat))
                            val d = sdf.parse(dataList.TransactionDate)
                            val cal = Calendar.getInstance()
                            cal.time = d
                            val month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())

                            if(month==thisMonth){
                                expenses+=dataList.TransactionAmount
                            }

                        }

                        dashBoardView.populateThisMonthExpense(mainContext, expenses)
                    }

                    override fun onError(e: Throwable)
                    {
                    }

                    override fun onComplete()
                    {
                    }
                })
    }

    private fun getThisMonthExpenseObservable(mainContext: Context, userID: String, accountID: String): Observable<ArrayList<Transaction>>{
        return Observable.defer { Observable.just(realmModel.getThisMonthExpenseRealm(mainContext,userID,accountID)) }
    }


    override fun getRecentExpenses(mainContext: Context, userID: String, accountID: String) {

        getRecentExpensesObservable(mainContext, userID, accountID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer< ArrayList<Transaction>>
                {
                    override fun onSubscribe(d: Disposable)
                    {
                    }

                    override fun onNext(value:  ArrayList<Transaction>)
                    {
                        val entries = ArrayList<Entry>()
                        val xAxisLabel = ArrayList<String>()

                        val sdf=SimpleDateFormat(mainContext.getString(R.string.dateFormat))

                        for (a in 0..30) {
                            val tempCalander = Calendar.getInstance()

                            tempCalander.add(Calendar.DAY_OF_MONTH, -a)

                            val thisDay = tempCalander.get(Calendar.DAY_OF_MONTH)
                            val thisMonth = tempCalander.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())

                            xAxisLabel.add(thisDay.toString() + thisMonth)

                            val compareDate = sdf.format(tempCalander.time)

                            var expense = 0.0

                            value.forEach { data ->

                                if (data.TransactionDate == compareDate && data.TransactionCategory.TransactionCategoryType == mainContext.getString(R.string.expense)) {
                                    expense += data.TransactionAmount
                                }
                            }

                            entries.add(BarEntry(a.toFloat(), expense.toFloat()))
                        }

                        dashBoardView.populateExpenseGraph(mainContext, entries,xAxisLabel)
                    }

                    override fun onError(e: Throwable)
                    {
                    }

                    override fun onComplete()
                    {
                    }
                })
    }

    private fun getRecentExpensesObservable(mainContext: Context, userID: String, accountID: String): Observable<ArrayList<Transaction>>{
        return Observable.defer { Observable.just(realmModel.getRecentExpenseRealm(mainContext,userID,accountID)) }
    }


    // ViewWalletAccount Fragment

    override fun checkWalletAccountCount(mainContext: Context, userID: String) {

        checkWalletAccountCountObservable(mainContext, userID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Int>
                {
                    override fun onSubscribe(d: Disposable)
                    {
                    }

                    override fun onNext(value: Int)
                    {
                        walletAccountView.createButtonStatus(mainContext, value)
                    }

                    override fun onError(e: Throwable)
                    {
                        walletAccountView.createButtonStatusFail(mainContext,e.toString())
                    }

                    override fun onComplete()
                    {
                    }
                })
    }

    private fun checkWalletAccountCountObservable(mainContext: Context, userID: String): Observable<Int>{
        return Observable.defer { Observable.just(realmModel.checkWalletAccountCountRealm(mainContext, userID)) }
    }


    // CreateWalletAccount Fragment

    override fun createWalletAccount(mainContext: Context, walletAccountInput: WalletAccount) {

        createWalletAccountObservable(mainContext, walletAccountInput)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Unit>
                {
                    override fun onSubscribe(d: Disposable)
                    {
                    }

                    override fun onNext(value: Unit)
                    {
                        createWalletAccountView.createWalletAccountSuccess(mainContext)
                    }

                    override fun onError(e: Throwable)
                    {
                        createWalletAccountView.createWalletAccountFail(mainContext,e.toString())
                    }

                    override fun onComplete()
                    {
                    }
                })
    }

    private fun createWalletAccountObservable(mainContext: Context, walletAccountInput: WalletAccount): Observable<Unit>{
        return Observable.defer { Observable.just(realmModel.createWalletAccountRealm(mainContext, walletAccountInput)) }
    }



    // DetailsWalletAccount Fragment

    override fun updateWalletAccount(mainContext: Context, walletAccountData: WalletAccount) {

        updateWalletAccountObservable(mainContext,walletAccountData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Unit>
                {
                    override fun onSubscribe(d: Disposable)
                    {
                    }

                    override fun onNext(value: Unit)
                    {
                        detailsWalletAccountView.updateWalletAccountSuccess(mainContext)
                    }

                    override fun onError(e: Throwable)
                    {
                        detailsWalletAccountView.updateWalletAccountFail(mainContext,e.toString())
                    }

                    override fun onComplete()
                    {
                    }
                })
    }

    private fun updateWalletAccountObservable(mainContext: Context, walletAccountData: WalletAccount): Observable<Unit>{
        return Observable.defer { Observable.just(realmModel.updateWalletAccountRealm(mainContext, walletAccountData)) }
    }


    override fun deleteWalletAccount(mainContext: Context, walletAccountID: String) {

        deleteWalletAccountObservable(mainContext, walletAccountID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Unit>
                {
                    override fun onSubscribe(d: Disposable)
                    {
                    }

                    override fun onNext(value: Unit)
                    {
                        detailsWalletAccountView.deleteWalletAccountSuccess(mainContext)
                    }

                    override fun onError(e: Throwable)
                    {
                        detailsWalletAccountView.deleteWalletAccountFail(mainContext,e.toString())
                    }

                    override fun onComplete()
                    {
                    }
                })
    }

    private fun deleteWalletAccountObservable(mainContext: Context, walletAccountID: String): Observable<Unit>{
        return Observable.defer { Observable.just(realmModel.deleteWalletAccountRealm(mainContext, walletAccountID)) }
    }



    // ViewTrxCategory Fragment
    override fun checkTransactionCategory(mainContext: Context, userID: String, filterSelection: String) {

        val view = (mainContext as Activity).findViewById(R.id.navMenu) as View
        val currentDestination = findNavController(view).currentDestination.id

        checkTransactionCategoryObservable(mainContext, userID, filterSelection)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ArrayList<TransactionCategory>>
                {
                    override fun onSubscribe(d: Disposable)
                    {
                    }

                    override fun onNext(value: ArrayList<TransactionCategory>)
                    {
                        if(currentDestination==R.id.viewTrxCategoryFragment){
                            trxCategoryView.populateTrxCategoryRecycleView(mainContext, value)

                        }else if(currentDestination==R.id.newTrxFragment){
                            newTrxView.populateNewTrxCategorySpinner(mainContext, value)

                        }else if(currentDestination==R.id.detailsTrxFragment){
                            detailsTrxView.populateDetailTrxCategorySpinner(mainContext, value)

                        }else if(currentDestination==R.id.trxHistoryFragment){

                            val fragmentView = mainContext.findViewById(R.id.trxHistoryFragmentNavMenu) as View
                            val fragmentDestination = findNavController(fragmentView).currentDestination.id

                            if(fragmentDestination==R.id.trxHistorySpecificDateFragment){
                                trxHistorySpecificView.populateTrxHistorySpecificCategorySpinner(mainContext,value)

                            }else if(fragmentDestination==R.id.trxHistoryRangeDateFragment){
                                trxHistoryRangeView.populateTrxHistoryRangeCategorySpinner(mainContext,value)

                            }else if(fragmentDestination==R.id.detailsTrxFragmentTrxHistory){  //?!!
                                detailsTrxView.populateDetailTrxCategorySpinner(mainContext, value)
                            }

                        }
                    }

                    override fun onError(e: Throwable)
                    {
                        if(currentDestination==R.id.viewTrxCategoryFragment){
                            trxCategoryView.populateTrxCategoryRecycleViewFail(mainContext,e.toString())

                        }else if(currentDestination==R.id.newTrxFragment){
                            newTrxView.populateNewTrxCategorySpinnerFail(mainContext,e.toString())

                        }else if(currentDestination==R.id.detailsTrxFragment){
                            detailsTrxView.populateDetailTrxAccountSpinnerFail(mainContext, e.toString())

                        }else if(currentDestination==R.id.trxHistoryFragment){

                            val fragmentView = mainContext.findViewById(R.id.trxHistoryFragmentNavMenu) as View
                            val fragmentDestination = findNavController(fragmentView).currentDestination.id

                            if(fragmentDestination==R.id.trxHistorySpecificDateFragment){
                                trxHistorySpecificView.populateTrxHistorySpecificCategorySpinnerFail(mainContext,e.toString())

                            }else if(fragmentDestination==R.id.trxHistoryRangeDateFragment){
                                trxHistoryRangeView.populateTrxHistoryRangeCategorySpinnerFail(mainContext,e.toString())

                            }else if(fragmentDestination==R.id.detailsTrxFragmentTrxHistory){  //?!!
                                detailsTrxView.populateDetailTrxCategorySpinnerFail(mainContext, e.toString())
                            }

                        }
                    }

                    override fun onComplete()
                    {
                    }
                })
    }

    private fun checkTransactionCategoryObservable(mainContext: Context, userID: String, filterSelection: String): Observable<ArrayList<TransactionCategory>>{
        return Observable.defer { Observable.just(realmModel.checkTransactionCategoryRealm(mainContext,userID,filterSelection)) }
    }


    // CreateTrxCategory Fragment
    override fun createTransactionCategory(mainContext: Context, trxCategoryInput: TransactionCategory) {

        createTransactionCategoryObservable(mainContext,trxCategoryInput)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Unit>
                {
                    override fun onSubscribe(d: Disposable)
                    {
                    }

                    override fun onNext(value: Unit)
                    {
                        createTrxCategoryView.createTrxCategorySuccess(mainContext)
                    }

                    override fun onError(e: Throwable)
                    {
                        createTrxCategoryView.createTrxCategoryFail(mainContext,e.toString())
                    }

                    override fun onComplete()
                    {
                    }
        })
    }

    private fun createTransactionCategoryObservable(mainContext: Context, trxCategoryInput: TransactionCategory): Observable<Unit>{
        return Observable.defer { Observable.just(realmModel.createTransactionCategoryRealm(mainContext,trxCategoryInput)) }
    }


    // DetailsTrxCategory Fragment
    override fun updateTransactionCategory(mainContext: Context, trxCategoryInput: TransactionCategory) {

        updateTransactionCategoryObservable(mainContext,trxCategoryInput)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Unit>
                {
                    override fun onSubscribe(d: Disposable)
                    {
                    }

                    override fun onNext(value: Unit)
                    {
                        detailsTrxCategoryView.updateTrxCategorySuccess(mainContext)
                    }

                    override fun onError(e: Throwable)
                    {
                        detailsTrxCategoryView.updateTrxCategoryFail(mainContext,e.toString())
                    }

                    override fun onComplete()
                    {
                    }
                })
    }

    private fun updateTransactionCategoryObservable(mainContext: Context, trxCategoryInput: TransactionCategory): Observable<Unit>{
        return Observable.defer { Observable.just(realmModel.updateTransactionCategoryRealm(mainContext, trxCategoryInput)) }
    }


    override fun deleteTransactionCategory(mainContext: Context, trxCategoryID: String) {

        deleteTransactionCategoryObservable(mainContext, trxCategoryID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Unit> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(value: Unit) {
                        detailsTrxCategoryView.deleteTrxCategorySuccess(mainContext)
                    }

                    override fun onError(e: Throwable) {
                        detailsTrxCategoryView.deleteTrxCategoryFail(mainContext, e.toString())
                    }

                    override fun onComplete() {
                    }
                })
    }

    private fun deleteTransactionCategoryObservable(mainContext: Context, trxCategoryID: String): Observable<Unit>{
        return Observable.defer { Observable.just(realmModel.deleteTransactionCategoryRealm(mainContext,trxCategoryID)) }
    }



    // NewTrx Fragment
    override fun createNewTrx(mainContext: Context, newTrxInput: Transaction) {

        createNewTrxObservable(mainContext, newTrxInput)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Unit>
                {
                    override fun onSubscribe(d: Disposable)
                    {
                    }

                    override fun onNext(value: Unit)
                    {
                        newTrxView.createNewTrxSuccess(mainContext)
                    }

                    override fun onError(e: Throwable)
                    {
                        newTrxView.createNewTrxFail(mainContext, e.toString())
                    }

                    override fun onComplete()
                    {
                    }
                })
    }

    private fun createNewTrxObservable(mainContext: Context, newTrxInput: Transaction): Observable<Unit>{
        return Observable.defer { Observable.just(realmModel.createNewTrxRealm(mainContext, newTrxInput)) }
    }



    // DetailsTrx Fragment

    override fun updateDetailsTrx(mainContext: Context, detailsTrxInput: Transaction) {

        updateDetailsTrxObservable(mainContext, detailsTrxInput)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Unit>
                {
                    override fun onSubscribe(d: Disposable)
                    {
                    }

                    override fun onNext(value: Unit)
                    {
                        detailsTrxView.updateDetailsTrxSuccess(mainContext)
                    }

                    override fun onError(e: Throwable)
                    {
                        detailsTrxView.updateDetailsTrxFail(mainContext, e.toString())
                    }

                    override fun onComplete()
                    {
                    }
                })
    }

    private fun updateDetailsTrxObservable(mainContext: Context, detailsTrxInput: Transaction): Observable<Unit>{
        return Observable.defer { Observable.just(realmModel.updateDetailsTrxRealm(mainContext, detailsTrxInput)) }
    }


    override fun deleteDetailsTrx(mainContext: Context, transactionID: String) {

        deleteDetailsTrxObservable(mainContext, transactionID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Unit>
                {
                    override fun onSubscribe(d: Disposable)
                    {
                    }

                    override fun onNext(value: Unit)
                    {
                        detailsTrxView.deleteDetailsTrxSuccess(mainContext)
                    }

                    override fun onError(e: Throwable)
                    {
                        detailsTrxView.deleteDetailsTrxFail(mainContext, e.toString())
                    }

                    override fun onComplete()
                    {
                    }
                })
    }

    private fun deleteDetailsTrxObservable(mainContext: Context, transactionID: String): Observable<Unit>{
        return Observable.defer { Observable.just(realmModel.deleteDetailsTrxRealm(mainContext, transactionID)) }
    }


    // TrxHistorySpecificDate Fragment
    override fun getTrxForSpecificDateFilter(mainContext: Context, userID: String, accountID: String, trxType: String, trxCategory: String, day: String, month: String, year: String) {

        getTrxForSpecificDateFilterObservable(mainContext, userID, accountID,trxType, trxCategory, day, month, year)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ArrayList<Transaction>>
                {
                    override fun onSubscribe(d: Disposable)
                    {
                        trxHistorySpecificView.disableBottomNavWhileLoading(mainContext)
                    }

                    override fun onNext(value: ArrayList<Transaction>)
                    {

                        //!!! Filtering urself HERE
                        val filteredData = ArrayList<Transaction>()

                        val noResult = mainContext.getString(R.string.noResult)

                        val nullData= Transaction(noResult,
                                noResult,
                                noResult,
                                0.0,
                                noResult,
                                TransactionCategory("","","","","")
                                ,WalletAccount("","",0.0,"","")
                        )


                        val sdf=SimpleDateFormat(mainContext.getString(R.string.dateFormat))

                        value.forEach {
                            data->

                            val mytime = data.TransactionDate

                            var myDate: Date? = null
                            myDate = sdf.parse(mytime)

                            val calendar = Calendar.getInstance()
                            calendar.time = myDate
                            val dataDay= calendar.get(Calendar.DAY_OF_MONTH).toString()

                            val dataMonth= calendar.get(Calendar.MONTH).toString()

                            var selectedMonth =""
                            if(month!=mainContext.getString(R.string.allMonth)){
                                val cal = Calendar.getInstance()
                                cal.time = SimpleDateFormat(mainContext.getString(R.string.monthFormat)).parse(month)
                                selectedMonth = (cal.get(Calendar.MONTH)).toString()
                            }

                            val dataYear= calendar.get(Calendar.YEAR).toString()


                            if(trxType==mainContext.getString(R.string.allType)) {

                                if (trxCategory == mainContext.getString(R.string.allCategory)) {

                                    ///////!!!!
                                    if (year == mainContext.getString(R.string.allYear)) {

                                        if (month == mainContext.getString(R.string.allMonth)) {

                                            if (day == mainContext.getString(R.string.allDay)) {
                                                filteredData.add(data)

                                            } else if (day != mainContext.getString(R.string.allDay) && day == dataDay){
                                                filteredData.add(data)
                                            }

                                        } else if (month != mainContext.getString(R.string.allMonth) && selectedMonth == dataMonth) {

                                            if (day == mainContext.getString(R.string.allDay)) {
                                                filteredData.add(data)

                                            } else if (day != mainContext.getString(R.string.allDay) && day == dataDay) {
                                                filteredData.add(data)

                                            }

                                        }

                                    } else if(year != mainContext.getString(R.string.allYear) && year==dataYear) {

                                        if (month == mainContext.getString(R.string.allMonth)) {

                                            if (day == mainContext.getString(R.string.allDay)) {
                                                filteredData.add(data)

                                            } else if (day != mainContext.getString(R.string.allDay) && day == dataDay){
                                                filteredData.add(data)
                                            }

                                        } else if (month != mainContext.getString(R.string.allMonth) && selectedMonth == dataMonth) {

                                            if (day == mainContext.getString(R.string.allDay)) {
                                                filteredData.add(data)

                                            } else if (day != mainContext.getString(R.string.allDay) && day == dataDay) {
                                                filteredData.add(data)

                                            }

                                        }

                                    }

                                }else if(trxCategory!=mainContext.getString(R.string.allCategory) && data.TransactionCategory.TransactionCategoryName==trxCategory){

                                    if (year == mainContext.getString(R.string.allYear)) {

                                        if (month == mainContext.getString(R.string.allMonth)) {

                                            if (day == mainContext.getString(R.string.allDay)) {
                                                filteredData.add(data)

                                            } else if (day != mainContext.getString(R.string.allDay) && day == dataDay){
                                                filteredData.add(data)
                                            }

                                        } else if (month != mainContext.getString(R.string.allMonth) && selectedMonth == dataMonth) {

                                            if (day == mainContext.getString(R.string.allDay)) {
                                                filteredData.add(data)

                                            } else if (day != mainContext.getString(R.string.allDay) && day == dataDay) {
                                                filteredData.add(data)

                                            }
                                        }

                                    } else if(year != mainContext.getString(R.string.allYear) && year==dataYear) {

                                        if (month == mainContext.getString(R.string.allMonth)) {

                                            if (day == mainContext.getString(R.string.allDay)) {
                                                filteredData.add(data)

                                            } else if (day != mainContext.getString(R.string.allDay) && day == dataDay){
                                                filteredData.add(data)
                                            }

                                        } else if (month != mainContext.getString(R.string.allMonth) && selectedMonth == dataMonth) {

                                            if (day == mainContext.getString(R.string.allDay)) {
                                                filteredData.add(data)

                                            } else if (day != mainContext.getString(R.string.allDay) && day == dataDay) {
                                                filteredData.add(data)

                                            }
                                        }
                                    }
                                }

                            }else if(trxType!=mainContext.getString(R.string.allType)){

                                if(trxCategory==mainContext.getString(R.string.allCategory) && data.TransactionCategory.TransactionCategoryType==trxType){
                                    ///////!!!!
                                    if (year == mainContext.getString(R.string.allYear)) {

                                        if (month == mainContext.getString(R.string.allMonth)) {

                                            if (day == mainContext.getString(R.string.allDay)) {
                                                filteredData.add(data)

                                            } else if (day != mainContext.getString(R.string.allDay) && day == dataDay){
                                                filteredData.add(data)
                                            }

                                        } else if (month != mainContext.getString(R.string.allMonth) && selectedMonth == dataMonth) {

                                            if (day == mainContext.getString(R.string.allDay)) {
                                                filteredData.add(data)

                                            } else if (day != mainContext.getString(R.string.allDay) && day == dataDay) {
                                                filteredData.add(data)

                                            }
                                        }

                                    } else if(year != mainContext.getString(R.string.allYear) && year==dataYear) {

                                        if (month == mainContext.getString(R.string.allMonth)) {

                                            if (day == mainContext.getString(R.string.allDay)) {
                                                filteredData.add(data)

                                            } else if (day != mainContext.getString(R.string.allDay) && day == dataDay){
                                                filteredData.add(data)
                                            }

                                        } else if (month != mainContext.getString(R.string.allMonth) && selectedMonth == dataMonth) {

                                            if (day == mainContext.getString(R.string.allDay)) {
                                                filteredData.add(data)

                                            } else if (day != mainContext.getString(R.string.allDay) && day == dataDay) {
                                                filteredData.add(data)

                                            }
                                        }
                                    }

                                }else if(trxCategory!=mainContext.getString(R.string.allCategory) && data.TransactionCategory.TransactionCategoryName==trxCategory && data.TransactionCategory.TransactionCategoryType==trxType){

                                    if (year == mainContext.getString(R.string.allYear)) {

                                        if (month == mainContext.getString(R.string.allMonth)) {

                                            if (day == mainContext.getString(R.string.allDay)) {
                                                filteredData.add(data)

                                            } else if (day != mainContext.getString(R.string.allDay) && day == dataDay){
                                                filteredData.add(data)
                                            }

                                        } else if (month != mainContext.getString(R.string.allMonth) && selectedMonth == dataMonth) {

                                            if (day == mainContext.getString(R.string.allDay)) {
                                                filteredData.add(data)

                                            } else if (day != mainContext.getString(R.string.allDay) && day == dataDay) {
                                                filteredData.add(data)

                                            }
                                        }

                                    } else if(year != mainContext.getString(R.string.allYear) && year==dataYear) {

                                        if (month == mainContext.getString(R.string.allMonth)) {

                                            if (day == mainContext.getString(R.string.allDay)) {
                                                filteredData.add(data)

                                            } else if (day != mainContext.getString(R.string.allDay) && day == dataDay){
                                                filteredData.add(data)
                                            }

                                        } else if (month != mainContext.getString(R.string.allMonth) && selectedMonth == dataMonth) {

                                            if (day == mainContext.getString(R.string.allDay)) {
                                                filteredData.add(data)

                                            } else if (day != mainContext.getString(R.string.allDay) && day == dataDay) {
                                                filteredData.add(data)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if(filteredData.size<1){
                            filteredData.add(nullData)
                        }

                        trxHistorySpecificView.populateTrxHistorySpecificRecycleView(mainContext, filteredData)
                    }

                    override fun onError(e: Throwable) {
                        trxHistorySpecificView.populateTrxHistorySpecificRecycleViewFail(mainContext, e.toString())
                    }

                    override fun onComplete() {
                        trxHistorySpecificView.enableBottomNavAfterLoading(mainContext)
                    }
                })
    }

    private fun getTrxForSpecificDateFilterObservable(mainContext: Context, userID: String, accountID: String, trxType: String, trxCategory: String, day: String, month: String, year: String): Observable<ArrayList<Transaction>>{
        return Observable.defer{ Observable.just(realmModel.getTrxForSpecificDateFilterRealm(mainContext, userID,accountID)) }
    }


    // TrxHistoryRangeDate Fragment
    override fun getTrxForRangeDateFilter(mainContext: Context, userID: String, accountID: String, trxType: String, trxCategory: String, startDate: String, endDate: String) {

        getTrxForRangeDateFilterObservable(mainContext, userID, accountID,trxType, trxCategory, startDate, endDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ArrayList<Transaction>> {
                    override fun onSubscribe(d: Disposable) {
                        trxHistoryRangeView.disableBottomNavWhileLoading(mainContext)
                    }

                    override fun onNext(value: ArrayList<Transaction>) {

                        val filteredData = ArrayList<Transaction>()

                        val noResult = mainContext.getString(R.string.noResult)

                        val nullData = Transaction(noResult,
                                noResult,
                                noResult,
                                0.0,
                                noResult,
                                TransactionCategory("", "", "", "", "")
                                , WalletAccount("", "", 0.0, "", "")
                        )

                        value.forEach {
                            data->

                            if (trxType == mainContext.getString(R.string.allType)) {

                                if (trxCategory == mainContext.getString(R.string.allCategory)) {

                                    if(data.TransactionDate in startDate..endDate){
                                        filteredData.add(data)
                                    }

                                } else if (trxCategory != mainContext.getString(R.string.allCategory) && data.TransactionCategory.TransactionCategoryName == trxCategory) {

                                    if(data.TransactionDate in startDate..endDate){
                                        filteredData.add(data)
                                    }
                                }

                            } else if (trxType != mainContext.getString(R.string.allType)) {

                                if (trxCategory == mainContext.getString(R.string.allCategory) && data.TransactionCategory.TransactionCategoryType == trxType) {

                                    if(data.TransactionDate in startDate..endDate){
                                        filteredData.add(data)
                                    }

                                } else if (trxCategory != mainContext.getString(R.string.allCategory) && data.TransactionCategory.TransactionCategoryName == trxCategory && data.TransactionCategory.TransactionCategoryType == trxType) {

                                    if(data.TransactionDate in startDate..endDate){
                                        filteredData.add(data)
                                    }
                                }
                            }
                        }

                        if(filteredData.size<1){
                            filteredData.add(nullData)
                        }

                        trxHistoryRangeView.populateTrxHistoryRangeRecycleView(mainContext, filteredData)
                    }

                    override fun onError(e: Throwable) {
                        trxHistoryRangeView.populateTrxHistoryRangeRecycleViewFail(mainContext, e.toString())
                    }

                    override fun onComplete() {
                        trxHistoryRangeView.enableBottomNavAfterLoading(mainContext)
                    }
                })
    }

    private fun getTrxForRangeDateFilterObservable(mainContext: Context, userID: String, accountID: String, trxType: String, trxCategory: String, startDate: String, endDate: String): Observable<ArrayList<Transaction>>{
        return Observable.defer{ Observable.just(realmModel.getTrxForRangeDateFilterRealm(mainContext, userID,accountID)) }
    }
}