package com.example.slnn3r.wallettrackermvp.Interface

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentActivity
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.Transaction
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.TransactionCategory
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.UserProfile
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.WalletAccount

interface PresenterInterface{

    interface Presenter {

        // SharedPreference
        fun getUserData(mainContext: Context): UserProfile

        // GetDataOnly
        fun getAccountData(mainContext: Context, userID: String): ArrayList<WalletAccount>
        fun getCategoryData(mainContext: Context, userID: String): ArrayList<TransactionCategory>

        // Wallet Account Input Validation
        fun walletAccountNameValidation(mainContext: Context, input: String, accountNameList: ArrayList<WalletAccount>, updateID:String?): String?
        fun walletAccountBalanceValidation(mainContext: Context, input: String): String?

        // Transaction Category Input Validation
        fun transactionCategoryNameValidation(mainContext: Context, input: String, categoryNameList: ArrayList<TransactionCategory>, updateID:String?): String?


        // Main Activity
        fun checkLogin()

        // Login activity
        fun loginGoogleExecute(mainContext: Context?, requestCode: Int, resultCode: Int, data: Intent)

        // Menu Activity
        fun logoutGoogleExecute(mainContext: Context)
        fun logoutGoogleStatus(mainContext: Context, logourStatus:Boolean, statusMessage:String)

        // DashBoard Fragment
        fun checkWalletAccount(mainContext: Context, userID:String) // (used by ViewWalletAccount Fragment as well)
        fun checkWalletAccountResult(mainContext: Context, walletAccountList: ArrayList<WalletAccount>, status:String) // (used by ViewWalletAccount Fragment as well)

        fun firstTimeDatabaseSetup(mainContext: Context, userID:String)
        fun firstTimeSetupStatus(mainContext: Context, walletAccount: WalletAccount, status:String)

        fun checkTransaction(mainContext: Context, accountID: String)
        fun checkTransactionResult(mainContext: Context, transactionList: ArrayList<Transaction>, status: String)


        // ViewWalletAccount Fragment
        fun checkWalletAccountCount(mainContext: Context, userID: String)
        fun checkWalletAccountCountResult(mainContext: Context, walletAccountCount:Int,status: String)

        // CreateWalletAccount Fragment
        fun createWalletAccount(mainContext:Context, walletAccountInput: WalletAccount)
        fun createWalletAccountStatus(mainContext:Context, createStatus:String)

        // DetailsWalletAccount Fragment
        fun updateWalletAccount(mainContext: Context, walletAccountData:WalletAccount)
        fun updateWalletAccountStatus(mainContext:Context, updateStatus:String)

        fun deleteWalletAccount(mainContext: Context, walletAccountID:String)
        fun deleteWalletAccountStatus(mainContext:Context, deleteStatus:String)


        // ViewTrxCategory Fragment
        fun checkTransactionCategory(mainContext: Context, userID: String, filterSelection: String) // Used by Both NewTrx+DetailsTrx Fragment
        fun checkTransactionCategoryResult(mainContext: Context, transactionCategoryList: ArrayList<TransactionCategory>, status: String) // Used by Both NewTrx+DetailsTrx Fragment

        // CreateTrxCategory Fragment
        fun createTransactionCategory(mainContext: Context, trxCategoryInput: TransactionCategory)
        fun createTransactionCategoryStatus(mainContext: Context, createStatus: String)

        // DetailsTrxCategory Fragment
        fun updateTransactionCategory(mainContext: Context, trxCategoryInput: TransactionCategory)
        fun updateTransactionCategoryStatus(mainContext: Context, updateStatus: String)

        fun deleteTransactionCategory(mainContext: Context, trxCategoryID:String)
        fun deleteTransactionCategoryStatus(mainContext: Context, deleteStatus: String)






    }
    
}