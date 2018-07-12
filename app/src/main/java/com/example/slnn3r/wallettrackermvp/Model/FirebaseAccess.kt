package com.example.slnn3r.wallettrackermvp.Model

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentActivity
import com.example.slnn3r.wallettrackermvp.Interface.ModelInterface
import com.example.slnn3r.wallettrackermvp.Interface.PresenterInterface

import com.example.slnn3r.wallettrackermvp.Presenter.Presenter

import com.example.slnn3r.wallettrackermvp.View.Activity.MainActivity
import com.example.slnn3r.wallettrackermvp.View.Activity.LoginActivity
import com.example.slnn3r.wallettrackermvp.View.Activity.MenuActivity

import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

import android.os.Bundle


import android.content.Context.MODE_PRIVATE
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.UserProfile
import com.example.slnn3r.wallettrackermvp.R
import com.google.gson.Gson


class FirebaseAccess: ModelInterface.FirebaseAccess{

    private lateinit var presenter: PresenterInterface.Presenter

    private var mAuth: FirebaseAuth? = null
    private val REQUEST_CODE_SIGN_IN= 1
    private var activity:Activity? = null
    private var mGoogleApiClient: GoogleApiClient? = null

    private val loginLoading: ProgressDialog? = null


    // Main Activity
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


    // Login Activity
    override fun loginGoogleFirebaseRequest(mainContext: Context) {

        activity = mainContext as Activity

        val fragment = activity as FragmentActivity

        mAuth = FirebaseAuth.getInstance()

        val errorMessage = mainContext.getString(R.string.GAPCCError)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mainContext.getString(R.string.GoogleSignInOptionKey))
                .requestEmail()
                .build()

        mGoogleApiClient = GoogleApiClient.Builder(mainContext)
                .enableAutoManage(fragment, GoogleApiClient.OnConnectionFailedListener{
                    presenter= Presenter(LoginActivity())
                    presenter.loginGoogleStatus(mainContext,false, errorMessage, loginLoading)
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        val intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)

        mGoogleApiClient?.stopAutoManage(fragment)
        mGoogleApiClient?.disconnect()

        presenter= Presenter(LoginActivity())
        presenter.displayLoginFragment(mainContext, fragment, intent, REQUEST_CODE_SIGN_IN)

    }

    override fun loginGoogleFirebaseExecute(mainContext: Context?, requestCode: Int, resultCode: Int, data: Intent, loginLoading:ProgressDialog){

        val errorMessage = mainContext!!.getString(R.string.FAWGError)

        if (requestCode == REQUEST_CODE_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                // successful -> authenticate with Firebase
                val account = result.signInAccount
                firebaseAuthWithGoogle(account!!, mainContext, loginLoading)
            } else {
                // failed -> update UI
                presenter.loginGoogleStatus(mainContext,false, errorMessage,loginLoading)
            }
        }

    }


    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount, mainContext: Context?,loginLoading:ProgressDialog){

        val successLoginMessage = mainContext!!.getString(R.string.loginSuccess)
        val errorMessage = mainContext.getString(R.string.SIWCError)


        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(activity!!) { task ->    // RXJAVA ISSUE: Before Firebase Response to here, RXJava will thought the function have complete and call onNext
                    if (task.isSuccessful) {                    // So without wait for Firebase loading, the ProgressDialog will be dismiss immediately at the onNext
                        // Sign in success
                        val user = mAuth!!.currentUser


                        // Store to SharedPreference
                        val editor = mainContext.getSharedPreferences(mainContext.getString(R.string.userProfileKey), MODE_PRIVATE)!!.edit()

                        // User GSON convert object to JSON String to store to shared Preference
                        val gson = Gson()
                        val userProfile = UserProfile(user!!.uid, user.displayName.toString(), user.email.toString(), user.photoUrl.toString())
                        val json = gson.toJson(userProfile)

                        editor.putString(mainContext.getString(R.string.userProfileKey), json)
                        editor.apply()
                        editor.commit()
                        // Store to SharedPreference


                        presenter.loginGoogleStatus(mainContext,true,successLoginMessage,loginLoading)


                    } else {
                        // Sign in fails
                        presenter.loginGoogleStatus(mainContext,false, errorMessage,loginLoading)

                    }
                }

    }



    // Menu Activity
    override fun logOutGoogleFirebase(mainContext: Context) {

        presenter= Presenter(MenuActivity())

        activity = mainContext as Activity
        val fragment = activity as FragmentActivity

        val errorMessage = mainContext.getString(R.string.GAPCCError)

        mAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mainContext.getString(R.string.GoogleSignInOptionKey))
                .requestEmail()
                .build()

        mGoogleApiClient = GoogleApiClient.Builder(mainContext)
                .enableAutoManage(fragment, GoogleApiClient.OnConnectionFailedListener{
                    presenter= Presenter(MenuActivity())
                    presenter.logoutGoogleStatus(mainContext,false, errorMessage)
                })
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
                presenter.logoutGoogleStatus(mainContext, false,errorMessage)
                mGoogleApiClient?.stopAutoManage(mainContext as FragmentActivity)
                mGoogleApiClient?.disconnect()
            }

            override fun onConnected(bundle: Bundle?) {

                FirebaseAuth.getInstance().signOut()
                if (mGoogleApiClient!!.isConnected) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback { status ->
                        if (status.isSuccess) {


                            // remove SharedPreference data
                            val editor = mainContext.getSharedPreferences(mainContext.getString(R.string.userProfileKey), MODE_PRIVATE).edit()
                            editor.remove(mainContext.getString(R.string.userProfileKey)).commit()
                            editor.remove(mainContext.getString(R.string.userProfileKey)).apply()

                            presenter.logoutGoogleStatus(mainContext, true,successLoginMessage)

                            mGoogleApiClient?.stopAutoManage(mainContext as FragmentActivity)
                            mGoogleApiClient?.disconnect()

                        }else{
                            presenter.logoutGoogleStatus(mainContext, false,errorMessage)

                            mGoogleApiClient?.stopAutoManage(mainContext as FragmentActivity)
                            mGoogleApiClient?.disconnect()
                        }
                    }
                }
            }


        })
    }


    // DashBoard Fragment



}