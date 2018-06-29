package com.example.slnn3r.wallettrackermvp.Presenter

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.support.v4.app.FragmentActivity
import com.example.slnn3r.wallettrackermvp.Interface.PresenterInterface
import com.example.slnn3r.wallettrackermvp.Interface.ViewInterface
import com.example.slnn3r.wallettrackermvp.Interface.ModelInterface
import com.example.slnn3r.wallettrackermvp.Model.FirebaseAccess;
import android.net.NetworkInfo
import android.util.Log
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

    // RX JAVA
    fun rxLogin(mainContext: Context?, requestCode: Int, resultCode: Int, data: Intent, loginloading: ProgressDialog): Observable<Unit>{

        return Observable.defer(object : Callable<ObservableSource<out Unit>>
        {
            @Throws(Exception::class)
            override fun call(): Observable<Unit>?
            {
                return Observable.just(firebaseModel.loginGoogleFirebaseExecute(mainContext, requestCode,resultCode,data,loginloading))
            }
        })

    }

    override fun loginGoogleExecute(mainContext: Context?, requestCode: Int, resultCode: Int, data: Intent){

        if(resultCode!=0){

            //RX JAVA
            rxLogin(mainContext, requestCode, resultCode,data,loginView.displayLoginLoading(mainContext!!))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<Unit>
                    {
                        override fun onSubscribe(d: Disposable)
                        {
                            //Suppposingly Call displayLoginLoading() Here
                        }

                        override fun onNext(value: Unit)
                        {
                            //Suppposingly Call dismissLoginLoading() Here
                            // Check (RXJAVA ISSUE) comment at FireBaseAccess.kt
                        }


                        override fun onError(e: Throwable)
                        {
                              Log.e("GG", "onError: ${e.toString()}", e)
                        }

                        override fun onComplete()
                        {
                            Log.e("GG", "COMPLEte: ")

                        }
                    })


            //loginView.displayLoginLoading()
            //firebaseModel.loginGoogleFirebaseExecute(mainContext, requestCode,resultCode,data)
        }else{
            var errorMessage:String= "Login Cancelled"

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


}