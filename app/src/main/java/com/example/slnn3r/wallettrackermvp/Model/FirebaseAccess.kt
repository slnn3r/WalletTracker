package com.example.slnn3r.wallettrackermvp.Model

import android.app.Activity
import com.example.slnn3r.wallettrackermvp.Interface.ModelInterface
import com.example.slnn3r.wallettrackermvp.Interface.PresenterInterface
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth


class FirebaseAccess: ModelInterface.FirebaseAccess{

    private var mAuth: FirebaseAuth? = null

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