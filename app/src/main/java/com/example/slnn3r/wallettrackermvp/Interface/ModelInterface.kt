package com.example.slnn3r.wallettrackermvp.Interface

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.TransactionCategory
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.UserProfile
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

        // GetDataOnly
        fun getAccountDataRealm(mainContext: Context, userID: String): ArrayList<WalletAccount>
        fun getCategoryDataRealm(mainContext: Context, userID: String): ArrayList<TransactionCategory>


        // DashBoard Fragment
        fun checkWalletAccountRealm(mainContext: Context, userID: String) //(used by WalletAccount Fragment as well)

        fun firstTimeRealmSetup(mainContext: Context, userID:String)

        fun checkTransactionRealm(mainContext: Context, accountID: String)


        // ViewWalletAccount Fragment
        fun checkWalletAccountCountRealm(mainContext: Context, userID:String)

        // CreateWalletAccount Fragment
        fun createWalletAccountRealm(mainContext:Context, walletAccountInput: WalletAccount)

         // DetailsWalletAccount Fragment
        fun updateWalletAccountRealm(mainContext:Context, walletAccountData: WalletAccount)
        fun deleteWalletAccountRealm(mainContext:Context, walletAccountDataID: String)


        // ViewTrxCategory Fragment
        fun checkTransactionCategoryRealm(mainContext: Context, userID: String, filterSelection: String)

        // CreateTrxCategory Fragment
        fun createTransactionCategoryRealm(mainContext: Context, trxCategoryInput: TransactionCategory)

        // DetailsTrxCategory Fragment
        fun updateTransactionCategoryRealm(mainContext: Context, trxCategoryInput: TransactionCategory)
        fun deleteTransactionCategoryRealm(mainContext:Context, walletAccountDataID: String)



    }

    interface SharedPreference{

        fun getUserData(mainContext: Context): UserProfile

    }

}