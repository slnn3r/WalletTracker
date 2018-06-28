package com.example.slnn3r.wallettrackermvp.Model

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.example.slnn3r.wallettrackermvp.Interface.ModelInterface
import com.example.slnn3r.wallettrackermvp.Interface.PresenterInterface

import com.example.slnn3r.wallettrackermvp.Presenter.Presenter

import com.example.slnn3r.wallettrackermvp.View.MainActivity;
import com.example.slnn3r.wallettrackermvp.View.LoginActivity;
import com.example.slnn3r.wallettrackermvp.View.MenuActivity


import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

import android.os.Bundle

import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status


class FirebaseAccess: ModelInterface.FirebaseAccess{



    private lateinit var presenter: PresenterInterface.Presenter

    private var mAuth: FirebaseAuth? = null
    private val REQUEST_CODE_SIGN_IN= 1
    private var activity:Activity? = null
    private var mGoogleApiClient: GoogleApiClient? = null

    private val key="866059954529-u7ki0u0si1veh1ul9slartd6ejnq0js7.apps.googleusercontent.com";


    override fun checkLoginFirebase(mainContext: Context) {

        presenter= Presenter(MainActivity())
        mAuth = FirebaseAuth.getInstance()

        val currentUser = mAuth!!.currentUser

        if(currentUser!=null){
            presenter.checkLoginResult(mainContext, loginResult = true)

        }else{
            presenter.checkLoginResult(mainContext, loginResult = false )

        }

    }


    override fun loginGoogleFirebaseRequest(mainContext: Context) {

        activity = mainContext as Activity

        val fragment = activity as FragmentActivity

        mAuth = FirebaseAuth.getInstance()

        var errorMessage:String = "Google API Client Connection Failure"

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(key)
                .requestEmail()
                .build()

        mGoogleApiClient = GoogleApiClient.Builder(mainContext)
                .enableAutoManage(fragment, GoogleApiClient.OnConnectionFailedListener{
                    presenter= Presenter(LoginActivity())
                    presenter.loginGoogleStatus(mainContext,false, errorMessage)
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        val intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)

        mGoogleApiClient?.stopAutoManage(fragment)
        mGoogleApiClient?.disconnect()

        presenter= Presenter(LoginActivity())
        presenter.displayLoginFragment(mainContext, fragment, intent, REQUEST_CODE_SIGN_IN)

    }

    override fun loginGoogleFirebaseExecute(mainContext: Context?, requestCode: Int, resultCode: Int, data: Intent) {

        var errorMessage:String = "Firebase Auth With Google Failure"

        if (requestCode == REQUEST_CODE_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                // successful -> authenticate with Firebase
                val account = result.signInAccount
                firebaseAuthWithGoogle(account!!, mainContext)
            } else {
                // failed -> update UI
                presenter.loginGoogleStatus(mainContext,false, errorMessage)
            }
        }

    }


    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount, mainContext: Context?) {

        presenter= Presenter(LoginActivity())

        var successLoginMessage = "Successfully Login"
        var errorMessage:String = "Sign In With Credential Failure - No Internet Connection"


        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(activity!!) { task ->
                    if (task.isSuccessful) {
                        // Sign in success
                        val user = mAuth!!.currentUser
                        presenter.loginGoogleStatus(mainContext,true,successLoginMessage)

                    } else {
                        // Sign in fails
                        presenter.loginGoogleStatus(mainContext,false, errorMessage)

                    }
                }
    }


    override fun logOutGoogleFirebase(mainContext: Context) {

        presenter= Presenter(MenuActivity())

        activity = mainContext as Activity
        val fragment = activity as FragmentActivity

        var errorMessage:String = "Google API Client Connection Failure"


        mAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(key)
                .requestEmail()
                .build()

        mGoogleApiClient = GoogleApiClient.Builder(mainContext)
                .enableAutoManage(fragment, GoogleApiClient.OnConnectionFailedListener{
                    presenter= Presenter(MenuActivity())
                    presenter.logoutGoogleStatus(mainContext,false, errorMessage)
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        logOutGoogleFireBaseExecute(mainContext);

    }


    fun logOutGoogleFireBaseExecute(mainContext: Context) {

        var successLoginMessage = "Successfully Logout"
        var errorMessage:String = "Google SignInApi SignOut Failure"

        mGoogleApiClient?.connect()
        mGoogleApiClient?.registerConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {

            override fun onConnectionSuspended(p0: Int) {
                errorMessage="GoogleAPIClient Connection Suspended"
                presenter.logoutGoogleStatus(mainContext, false,errorMessage)
                mGoogleApiClient?.stopAutoManage(mainContext as FragmentActivity)
                mGoogleApiClient?.disconnect()
            }

            override fun onConnected(bundle: Bundle?) {

                FirebaseAuth.getInstance().signOut()
                if (mGoogleApiClient!!.isConnected()) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(object : ResultCallback<Status> {
                        override fun onResult(status: Status) {
                            if (status.isSuccess()) {

                                presenter.logoutGoogleStatus(mainContext, true,successLoginMessage)

                                mGoogleApiClient?.stopAutoManage(mainContext as FragmentActivity)
                                mGoogleApiClient?.disconnect()

                            }else{
                                presenter.logoutGoogleStatus(mainContext, false,errorMessage)

                                mGoogleApiClient?.stopAutoManage(mainContext as FragmentActivity)
                                mGoogleApiClient?.disconnect()
                            }
                        }
                    })
                }
            }


        })
    }


}