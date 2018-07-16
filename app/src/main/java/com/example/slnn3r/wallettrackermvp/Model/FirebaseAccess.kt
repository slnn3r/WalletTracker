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


    // Main Activity
    override fun checkLoginFirebase():String? {

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth!!.currentUser

        return if(currentUser!=null){
            currentUser.displayName

        }else{
            ""
        }

    }


    // Login Activity





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
                .enableAutoManage(fragment) {



                    presenter= Presenter(MenuActivity())
                    presenter.logoutGoogleStatus(mainContext,false, errorMessage)
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