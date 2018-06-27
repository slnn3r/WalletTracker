package com.example.slnn3r.wallettrackermvp.Interface

import android.content.Context
import android.content.Intent

interface ModelInterface {

    interface FirebaseAccess{


        fun checkLoginFirebase(mainContext: Context)

        fun loginGoogleFirebaseRequest(mainContext:Context)
        fun loginGoogleFirebaseExecute(mainContext: Context?, requestCode: Int, resultCode: Int, data: Intent)

        fun logOutGoogleFirebase(mainContext: Context)

    }

}