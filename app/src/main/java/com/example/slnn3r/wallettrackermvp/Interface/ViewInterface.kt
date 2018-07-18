package com.example.slnn3r.wallettrackermvp.Interface

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentActivity
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.Transaction
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.TransactionCategory
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.WalletAccount

interface ViewInterface{

    interface MainView{

        fun navigateToLoginScreen()
        fun navigateToMenuScreen(userName: String?)
        fun checkLoginFail(errorMessage: String)

    }

    interface LoginView{

        fun loginSuccess(mainContext: Context?, successLoginMessage:String)
        fun loginFail(mainContext: Context?, errorMessage:String)

        fun displayLoginLoading(mainContext:Context): ProgressDialog
        fun dismissLoginLoading(loginLoading:ProgressDialog)


    }

    interface MenuView{

        fun logoutSuccess(mainContext: Context, successLogoutMessage:String)
        fun logoutFail(mainContext: Context, errorMessage:String)

    }

    interface DashBoardView{

        fun populateWalletAccountSpinner(mainContext: Context, walletAccountList: ArrayList<WalletAccount>)
        fun populateWalletAccountSpinnerFail(mainContext: Context, errorMessage: String)


        fun firstTimeSetup(mainContext: Context)
        fun firstTimeSetupSuccess(mainContext: Context, walletAccount: WalletAccount)
        fun firstTimeSetupFail(mainContext: Context, errorMessage: String)


        fun populateTransactionRecycleView(mainContext: Context, transactionList: ArrayList<Transaction>)
        fun populateTransactionRecycleViewFail(mainContext: Context, errorMessage: String)


    }


    interface WalletAccountView{

        fun populateWalletAccountRecycleView(mainContext: Context, walletAccountList: ArrayList<WalletAccount>)
        fun populateWalletAccountRecycleViewFail(mainContext: Context, errorMessage: String)

        fun createButtonStatus(mainContext: Context, walletAccountCount:Int)
        fun createButtonStatusFail(mainContext: Context, errorMessage: String)

    }

    interface CreateWalletAccountView{

        fun createWalletAccountSuccess(mainContext: Context)
        fun createWalletAccountFail(mainContext: Context, errorMessage:String)


    }

    interface DetailsWalletAccountView{

        fun updateWalletAccountSuccess(mainContext: Context)
        fun updateWalletAccountFail(mainContext: Context, errorMessage:String)

        fun deleteWalletAccountSuccess(mainContext: Context)
        fun deleteWalletAccountFail(mainContext: Context, errorMessage:String)


    }


    interface TrxCategoryView{

        fun populateTrxCategoryRecycleView(mainContext: Context, trxCategoryList: ArrayList<TransactionCategory>)
        fun populateTrxCategoryRecycleViewFail(mainContext: Context, errorMessage: String)

    }


    interface CreateTrxCategoryView{

        fun createTrxCategorySuccess(mainContext: Context)
        fun createTrxCategoryFail(mainContext: Context, errorMessage:String)

    }

    interface DetailsTrxCategoryView{

        fun updateTrxCategorySuccess(mainContext: Context)
        fun updateTrxCategoryFail(mainContext: Context, errorMessage:String)

        fun deleteTrxCategorySuccess(mainContext: Context)
        fun deleteTrxCategoryFail(mainContext: Context, errorMessage:String)

    }

    interface NewTrxView{

        fun populateNewTrxCategorySpinner(mainContext: Context, trxCategoryList: ArrayList<TransactionCategory>)
        fun populateNewTrxCategorySpinnerFail(mainContext: Context, errorMessage: String)

        fun populateSelectedAccountSpinner(mainContext: Context, walletAccountList: ArrayList<WalletAccount>)
        fun populateSelectedAccountSpinnerFail(mainContext: Context, errorMessage: String)

        fun createNewTrxSuccess(mainContext: Context)
        fun createNewTrxFail(mainContext: Context, errorMessage: String)

    }


}