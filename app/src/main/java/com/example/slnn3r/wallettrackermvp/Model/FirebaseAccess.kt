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


    // DashBoard Fragment



}