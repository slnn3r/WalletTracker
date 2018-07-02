package com.example.slnn3r.wallettrackermvp.Interface

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent

interface ModelInterface {

    interface FirebaseAccess{

        // Main Activity
        fun checkLoginFirebase(mainContext: Context)

        // Login Activity
        fun loginGoogleFirebaseRequest(mainContext:Context)
        fun loginGoogleFirebaseExecute(mainContext: Context?, requestCode: Int, resultCode: Int, data: Intent, loginLoading: ProgressDialog)

        // Menu Activity
        fun logOutGoogleFirebase(mainContext: Context)

    }

    interface RealmAccess{

        

    }

}