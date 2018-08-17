package com.example.slnn3r.wallettrackermvp.Interface

import android.content.Context
import android.content.Intent
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.Transaction
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.TransactionCategory
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.UserProfile
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.WalletAccount

interface PresenterInterface {

    interface Presenter {

        // SharedPreference
        fun getUserData(mainContext: Context): UserProfile

        fun setSelectedAccount(mainContext: Context, selection: String)
        fun getSelectedAccount(mainContext: Context): String

        // GetDataOnly
        fun getAccountData(mainContext: Context, userID: String): ArrayList<WalletAccount>

        fun getCategoryData(mainContext: Context, userID: String): ArrayList<TransactionCategory>

        fun getTransactionData(mainContext: Context, userID: String): ArrayList<Transaction>

        fun getAccountDataByName(mainContext: Context, userID: String, accountName: String): WalletAccount
        fun getCategoryDataByName(mainContext: Context, userID: String, categoryName: String): TransactionCategory


        // Wallet Account Input Validation
        fun walletAccountNameValidation(mainContext: Context, input: String, accountNameList: ArrayList<WalletAccount>, updateID: String?): String?

        fun walletAccountBalanceValidation(mainContext: Context, input: String): String?

        // Transaction Category Input Validation
        fun transactionCategoryNameValidation(mainContext: Context, input: String, categoryNameList: ArrayList<TransactionCategory>, updateID: String?): String?

        // NewTrx+DetailsTrx TrxInput Validation
        fun transactionInputValidation(mainContext: Context, input: String): String?


        // Main Activity
        fun checkLogin()

        // Login activity
        fun loginGoogleExecute(mainContext: Context?, requestCode: Int, resultCode: Int, data: Intent)

        fun retrieveData(mainContext: Context, userID: String)

        // Menu Activity
        fun backupDataManually(mainContext: Context, userID: String)

        fun backupDataPeriodically(mainContext: Context, userID: String)

        fun logoutGoogleExecute(mainContext: Context)
        fun logoutGoogleStatus(mainContext: Context, logoutStatus: Boolean, statusMessage: String)


        // DashBoard Fragment
        fun checkWalletAccount(mainContext: Context, userID: String) // (used by ViewWalletAccount Fragment as well)

        fun firstTimeDatabaseSetup(mainContext: Context, userID: String)
        fun checkTransaction(mainContext: Context, accountID: String, userID: String)

        fun getAllIncome(mainContext: Context, userID: String, accountID: String)
        fun getThisMonthExpense(mainContext: Context, userID: String, accountID: String, thisMonth: String)

        fun getRecentExpenses(mainContext: Context, userID: String, accountID: String)

        // NewTrx Fragment
        fun createNewTrx(mainContext: Context, newTrxInput: Transaction)

        // DetailsTrx Fragment
        fun updateDetailsTrx(mainContext: Context, detailsTrxInput: Transaction)

        fun deleteDetailsTrx(mainContext: Context, transactionID: String)


        // ViewWalletAccount Fragment
        fun checkWalletAccountCount(mainContext: Context, userID: String)

        // CreateWalletAccount Fragment
        fun createWalletAccount(mainContext: Context, walletAccountInput: WalletAccount)

        // DetailsWalletAccount Fragment
        fun updateWalletAccount(mainContext: Context, walletAccountData: WalletAccount)

        fun deleteWalletAccount(mainContext: Context, walletAccountID: String)


        // ViewTrxCategory Fragment
        fun checkTransactionCategory(mainContext: Context, userID: String, filterSelection: String) // Used by Both NewTrx+DetailsTrx Fragment

        // CreateTrxCategory Fragment
        fun createTransactionCategory(mainContext: Context, trxCategoryInput: TransactionCategory)

        // DetailsTrxCategory Fragment
        fun updateTransactionCategory(mainContext: Context, trxCategoryInput: TransactionCategory)

        fun deleteTransactionCategory(mainContext: Context, trxCategoryID: String)


        // TrxHistorySpecificDate Fragment
        fun getTrxForSpecificDateFilter(mainContext: Context, userID: String, accountID: String, trxType: String, trxCategory: String, day: String, month: String, year: String)


        // TrxHistoryRangeDate Fragment
        fun getTrxForRangeDateFilter(mainContext: Context, userID: String, accountID: String, trxType: String, trxCategory: String, startDate: String, endDate: String)
    }

}