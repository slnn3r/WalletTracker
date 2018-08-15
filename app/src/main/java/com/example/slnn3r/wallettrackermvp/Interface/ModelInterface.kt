package com.example.slnn3r.wallettrackermvp.Interface

import android.content.Context
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.Transaction
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.TransactionCategory
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.UserProfile
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.WalletAccount

interface ModelInterface {

    interface FirebaseAccess{

        // Main Activity
        fun checkLoginFirebase(): String?

        // Login Activity
        fun retrieveDataFirebase(mainContext: Context, userID: String)

        // Menu Activity
        fun backupDataManuallyFirebase(mainContext: Context,userID: String)
        fun backupDataPeriodicallyFirebase(mainContext: Context,userID: String)
    }

    interface RealmAccess{

        // GetDataOnly
        fun getAccountDataRealm(mainContext: Context, userID: String): ArrayList<WalletAccount>
        fun getCategoryDataRealm(mainContext: Context, userID: String): ArrayList<TransactionCategory>

        fun getTransactionDataRealm(mainContext: Context, userID: String): ArrayList<Transaction>

        fun getAccountDataByNameRealm(mainContext: Context, userID: String, accountName: String): WalletAccount
        fun getCategoryDataByNameRealm(mainContext: Context, userID: String, categoryName: String): TransactionCategory

        // DashBoard Fragment
        fun checkWalletAccountRealm(mainContext: Context, userID: String): ArrayList<WalletAccount> //(used by WalletAccount Fragment as well)
        fun firstTimeRealmSetup(mainContext: Context, userID:String): WalletAccount
        fun checkTransactionRealm(mainContext: Context, accountID: String, userID:String): ArrayList<Transaction>

        fun getCurrentBalanceRealm(mainContext: Context, userID: String, accountID: String): Double
        fun getAllIncome(mainContext: Context, userID:String, accountID: String): Double
        fun getAllExpense(mainContext: Context, userID: String, accountID: String): Double

        fun getThisMonthExpenseRealm(mainContext: Context, userID: String, accountID: String):  ArrayList<Transaction>

        fun getRecentExpenseRealm(mainContext: Context, userID: String, accountID: String):  ArrayList<Transaction>

        // NewTrx Fragment
        fun createNewTrxRealm(mainContext: Context, newTrxInput:Transaction)

        // DetailsTrx Fragment
        fun updateDetailsTrxRealm(mainContext: Context, detailsTrxInput:Transaction)
        fun deleteDetailsTrxRealm(mainContext: Context, transactionID:String)


        // ViewWalletAccount Fragment
        fun checkWalletAccountCountRealm(mainContext: Context, userID:String): Int

        // CreateWalletAccount Fragment
        fun createWalletAccountRealm(mainContext:Context, walletAccountInput: WalletAccount)

         // DetailsWalletAccount Fragment
        fun updateWalletAccountRealm(mainContext:Context, walletAccountData: WalletAccount)
        fun deleteWalletAccountRealm(mainContext:Context, walletAccountDataID: String)


        // ViewTrxCategory Fragment
        fun checkTransactionCategoryRealm(mainContext: Context, userID: String, filterSelection: String):ArrayList<TransactionCategory>

        // CreateTrxCategory Fragment
        fun createTransactionCategoryRealm(mainContext: Context, trxCategoryInput: TransactionCategory)

        // DetailsTrxCategory Fragment
        fun updateTransactionCategoryRealm(mainContext: Context, trxCategoryInput: TransactionCategory)
        fun deleteTransactionCategoryRealm(mainContext:Context, walletAccountDataID: String)


        // TrxHistorySpecificDate Fragment
        fun getTrxForSpecificDateFilterRealm(mainContext: Context, userID: String, accountID: String):  ArrayList<Transaction>

        // TrxHistoryRangeDate Fragment
        fun getTrxForRangeDateFilterRealm(mainContext: Context, userID: String, accountID: String):  ArrayList<Transaction>
    }

    interface SharedPreference{

        fun getUserData(mainContext: Context): UserProfile
        fun saveUserData(mainContext: Context, userData:String)
        fun removeUserData(mainContext: Context)

        fun saveSelectedAccount(mainContext: Context, selection:String)
        fun getSelectedAccountData(mainContext: Context): String
    }

}