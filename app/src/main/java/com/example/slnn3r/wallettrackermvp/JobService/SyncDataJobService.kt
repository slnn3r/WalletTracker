package com.example.slnn3r.wallettrackermvp.JobService

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.slnn3r.wallettrackermvp.Interface.PresenterInterface
import com.example.slnn3r.wallettrackermvp.Model.FirebaseClass.TransactionCategoryFirebase
import com.example.slnn3r.wallettrackermvp.Model.FirebaseClass.TransactionFirebase
import com.example.slnn3r.wallettrackermvp.Model.FirebaseClass.WalletAccountFirebase
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.Transaction
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.TransactionCategory
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.WalletAccount
import com.example.slnn3r.wallettrackermvp.Presenter.Presenter
import com.example.slnn3r.wallettrackermvp.R
import com.example.slnn3r.wallettrackermvp.View.Fragment.DashBoardFragment
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*


class SyncDataJobService: JobService() {

    private var jobCancelled:Boolean = false
    private val database = FirebaseDatabase.getInstance()
    private lateinit var presenter: PresenterInterface.Presenter

    override fun onStartJob(params: JobParameters?): Boolean {

        Log.e("Job Scheduler Service","JobStarted")
        doBackgroundWork(params!!)
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {

        Log.e("Job Scheduler Service","Job Cancel")
        jobCancelled=true
        return true
    }


    private fun updateTransactionCategory(categoryList:ArrayList<TransactionCategory>){

        categoryList.forEach {
            data->
            database.reference.child(applicationContext.getString(R.string.TransactionCategoryFirebase)).push().setValue(data)
        }

        Log.e("Job Scheduler Service","TransactionCategory Push DONE")
    }

    private fun updateWalletAccount(accountList:ArrayList<WalletAccount>){
        accountList.forEach {
            data->
            database.reference.child(applicationContext.getString(R.string.WalletAccountFirebase)).push().setValue(data)
        }

        Log.e("Job Scheduler Service","WalletAccount Push DONE")
    }

    private fun updateTransaction(transactionList:ArrayList<Transaction>){
        transactionList.forEach {
            data->
            database.reference.child(applicationContext.getString(R.string.TransactionFirebase)).push().setValue(data)
        }

        Log.e("Job Scheduler Service","Transaction Push DONE")
    }


    //(No Error Handling for Firebase)
    private fun doBackgroundWork( params:JobParameters){

        val userID = params.extras.getString(applicationContext.getString(R.string.userIDServiceKey))

        presenter = Presenter(DashBoardFragment())

        val accountList = presenter.getAccountData(applicationContext, userID)
        val categoryList = presenter.getCategoryData(applicationContext, userID)
        val transactionList = presenter.getTransactionData(applicationContext,userID)

        database.reference.child(applicationContext.getString(R.string.TransactionCategoryFirebase)).addListenerForSingleValueEvent(object : ValueEventListener {
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


        database.reference.child(applicationContext.getString(R.string.WalletAccountFirebase)).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {

                    val message = dataSnapshot.getValue(WalletAccountFirebase::class.java)

                    if(message!!.UserUID==userID){
                        dataSnapshot.ref.setValue(null)
                    }
                }

                updateWalletAccount(accountList)

                val editor = applicationContext.getSharedPreferences(userID, AppCompatActivity.MODE_PRIVATE)!!.edit()


                val df = SimpleDateFormat(applicationContext.getString(R.string.dateFormat))
                val date12Format = SimpleDateFormat(applicationContext.getString(R.string.timeFormat12))
                val date24Format = SimpleDateFormat(applicationContext.getString(R.string.timeFormat24))

                val date = Calendar.getInstance().time

                val formattedDate = df.format(date).toString()
                val formattedTime = date12Format.format(date).toString()

               // val convertedTime = date12Format.format(date24Format.parse(cal.))

                editor.putString(userID, applicationContext.getString(R.string.formatDisplayDateTime,formattedDate,formattedTime))
                editor.apply()
                editor.commit()

                Handler().postDelayed({

                    Toast.makeText(applicationContext, applicationContext.getString(R.string.syncServiceSuccess), Toast.LENGTH_LONG).show()

                }, 200)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG: ", databaseError.message)
            }
        })


        database.reference.child(applicationContext.getString(R.string.TransactionFirebase)).addListenerForSingleValueEvent(object : ValueEventListener {
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
}