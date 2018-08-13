package com.example.slnn3r.wallettrackermvp.Model

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Handler
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import com.example.slnn3r.wallettrackermvp.Interface.ModelInterface
import com.example.slnn3r.wallettrackermvp.JobService.SyncDataJobService
import com.example.slnn3r.wallettrackermvp.Model.FirebaseClass.TransactionCategoryFirebase
import com.example.slnn3r.wallettrackermvp.Model.FirebaseClass.TransactionFirebase
import com.example.slnn3r.wallettrackermvp.Model.FirebaseClass.WalletAccountFirebase
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.Transaction
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.TransactionCategory
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.WalletAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

import android.net.ConnectivityManager
import java.util.concurrent.TimeUnit


class FirebaseAccess: ModelInterface.FirebaseAccess{


    private var mAuth: FirebaseAuth? = null
    private var database:FirebaseDatabase? = FirebaseDatabase.getInstance()

    // Main Activity
    override fun checkLoginFirebase():String? {

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth!!.currentUser

        return if(currentUser!=null){
            currentUser.displayName

        }else{
            ""
        }
    }


    // Login Activity



    // Menu Activity

    fun syncDataPeriodicFirebase(mainContext: Context, userID: String){

        val bundle = PersistableBundle()
        bundle.putString("user", userID)


        val component= ComponentName(mainContext, SyncDataJobService::class.java)
        val info = JobInfo.Builder(123, component)
                .setPeriodic(TimeUnit.MINUTES.toMillis(1))
                .setExtras(bundle)
                .setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build()
        val scheduler: JobScheduler = mainContext.applicationContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val resultCode = scheduler.schedule(info)

        if(resultCode== JobScheduler.RESULT_SUCCESS){
            Log.e("","Job Schedule")
        }else{
            Log.e("","Job Schedule Failed")

        }
    }


    override fun syncDataManuallyFirebase(mainContext: Context, userID: String) {

        val bundle = PersistableBundle()
        bundle.putString("user", userID)


        val component= ComponentName(mainContext, SyncDataJobService::class.java)
        val info = JobInfo.Builder(123, component)
                //.setPeriodic(TimeUnit.MINUTES.toMillis(1))
                .setExtras(bundle)
                .setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build()
        val scheduler: JobScheduler = mainContext.applicationContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val resultCode = scheduler.schedule(info)

        if(resultCode== JobScheduler.RESULT_SUCCESS){
            Log.e("","Job Schedule")
        }else{
            Log.e("","Job Schedule Failed")

        }


    }



    // DashBoard Fragment



}