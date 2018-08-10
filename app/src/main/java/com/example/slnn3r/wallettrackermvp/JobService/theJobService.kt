package com.example.slnn3r.wallettrackermvp.JobService

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import com.example.slnn3r.wallettrackermvp.Interface.PresenterInterface
import com.example.slnn3r.wallettrackermvp.Model.FirebaseClass.TransactionCategoryFirebase
import com.example.slnn3r.wallettrackermvp.Model.FirebaseClass.TransactionFirebase
import com.example.slnn3r.wallettrackermvp.Model.FirebaseClass.WalletAccountFirebase
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.Transaction
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.TransactionCategory
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.WalletAccount
import com.example.slnn3r.wallettrackermvp.Presenter.Presenter
import com.example.slnn3r.wallettrackermvp.View.Fragment.DashBoardFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener


class theJobService: JobService() {

    private var jobCancelled:Boolean = false
    private val database = FirebaseDatabase.getInstance()
    private lateinit var presenter: PresenterInterface.Presenter

    override fun onStartJob(params: JobParameters?): Boolean {

        Log.e("","JobStarted")

        doBackgroundWork(params!!)


        return true
    }

    private fun updateTransactionCategory(categoryList:ArrayList<TransactionCategory>){

        categoryList.forEach {
            data->
            database.reference.child("TransactionCategory").push().setValue(data)
        }

        Log.e("","TransactionCategory DONE")

    }

    private fun updateWalletAccount(accountList:ArrayList<WalletAccount>){
        accountList.forEach {
            data->
            database.reference.child("WalletAccount").push().setValue(data)
        }

        Log.e("","WalletAccount DONE")

    }

    private fun updateTransaction(transactionList:ArrayList<Transaction>){
        transactionList.forEach {
            data->
            database.reference.child("Transaction").push().setValue(data)
        }

        Log.e("","Transaction DONE")

    }

    private fun doBackgroundWork( params:JobParameters){

        val userID = params.extras.getString("user")

        presenter = Presenter(DashBoardFragment())

        val accountList = presenter.getAccountData(applicationContext!!, userID)
        val categoryList = presenter.getCategoryData(applicationContext!!, userID)
        val transactionList = presenter.getTransactionData(applicationContext!!,userID)

        database.reference.child("TransactionCategory").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {

                    val message = dataSnapshot.getValue(TransactionCategoryFirebase::class.java)

                    if(message!!.UserUID==userID){
                        dataSnapshot.ref.setValue(null)
                    }

                }

                updateTransactionCategory(categoryList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG: ", databaseError.message)
            }
        })


        database.reference.child("WalletAccount").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {

                    val message = dataSnapshot.getValue(WalletAccountFirebase::class.java)

                    if(message!!.UserUID==userID){
                        dataSnapshot.ref.setValue(null)
                    }

                }

                updateWalletAccount(accountList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG: ", databaseError.message)
            }
        })

        database.reference.child("Transaction").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {

                    val message = dataSnapshot.getValue(TransactionFirebase::class.java)

                    if(message!!.WalletAccount.UserUID==userID){
                        dataSnapshot.ref.setValue(null)
                    }

                }

                updateTransaction(transactionList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG: ", databaseError.message)
            }
        })



    }


    override fun onStopJob(params: JobParameters?): Boolean {

        Log.e("","Job Cancel")
        jobCancelled=true
        return true

    }



}