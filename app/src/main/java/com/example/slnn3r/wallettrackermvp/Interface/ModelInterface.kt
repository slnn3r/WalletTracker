package com.example.slnn3r.wallettrackermvp.Interface

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.WalletAccount

interface ModelInterface {

    interface FirebaseAccess{

        // Main Activity
        fun checkLoginFirebase(mainContext: Context)

        // Login Activity
        fun loginGoogleFirebaseRequest(mainContext:Context)
        fun loginGoogleFirebaseExecute(mainContext: Context?, requestCode: Int, resultCode: Int, data: Intent, loginLoading: ProgressDialog)

        // Menu Activity
        fun logOutGoogleFirebase(mainContext: Context)


        // DashBoard Fragment


    }

    interface RealmAccess{

        // DashBoard Fragment
        fun checkWalletAccountRealm(mainContext: Context, userID: String) //(used by WalletAccount Fragment as well)

        fun firstTimeRealmSetup(mainContext: Context, userID:String)

        fun checkTransactionRealm(mainContext: Context, accountID: String)


        // CreateWalletAccount Fragment
        fun createWalletAccountRealm(mainContext:Context, walletAccountInput: WalletAccount)

        // ViewWalletAccount Fragment
        fun checkWalletAccountCountRealm(mainContext: Context, userID:String)

        // DetailsWalletAccount Fragment
        fun updateWalletAccountRealm(mainContext:Context, walletAccountData: WalletAccount)
        fun deleteWalletAccountRealm(mainContext:Context, walletAccountDataID: String)


    }

}