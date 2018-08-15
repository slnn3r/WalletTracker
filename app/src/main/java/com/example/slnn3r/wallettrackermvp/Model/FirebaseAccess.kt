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
import com.example.slnn3r.wallettrackermvp.Model.RealmClass.TransactionCategoryRealm
import com.example.slnn3r.wallettrackermvp.Model.RealmClass.TransactionRealm
import com.example.slnn3r.wallettrackermvp.Model.RealmClass.WalletAccountRealm
import com.example.slnn3r.wallettrackermvp.R
import com.google.gson.Gson
import io.realm.Realm
import io.realm.RealmConfiguration
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class FirebaseAccess: ModelInterface.FirebaseAccess{

    private lateinit var mAuth: FirebaseAuth
    private lateinit var database:FirebaseDatabase

    // Main Activity
    override fun checkLoginFirebase():String? {

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser

        return if(currentUser!=null){
            currentUser.displayName

        }else{
            ""
        }
    }


    // Login Activity
    override fun retrieveDataFirebase(mainContext: Context, userID: String) {

        database = FirebaseDatabase.getInstance()

        val transactionCategoryList= ArrayList<TransactionCategoryFirebase>()
        val transactionList= ArrayList<TransactionFirebase>()
        val walletAccountList= ArrayList<WalletAccountFirebase>()

        database.reference.child("TransactionCategory").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {

                    val message = dataSnapshot.getValue(TransactionCategoryFirebase::class.java)

                    if(message!!.UserUID==userID){
                        transactionCategoryList.add(dataSnapshot.getValue(TransactionCategoryFirebase::class.java)!!)
                    }
                }

                Log.e("TC: ",transactionCategoryList.size.toString())

                syncTransactionCategoryRealm(mainContext, userID, transactionCategoryList)
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
                        walletAccountList.add(dataSnapshot.getValue(WalletAccountFirebase::class.java)!!)
                    }
                }

                Log.e("WA: ",walletAccountList.size.toString())
                syncWalletAccountRealm(mainContext, userID, walletAccountList)
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
                        transactionList.add(dataSnapshot.getValue(TransactionFirebase::class.java)!!)
                    }
                }

                Log.e("T: ",transactionList.size.toString())
                syncTransaction(mainContext, userID, transactionList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG: ", databaseError.message)
            }
        })


    }

    private fun syncWalletAccountRealm(mainContext: Context, userID:String, walletAccountList:ArrayList<WalletAccountFirebase>){

        var realm: Realm? = null
        Realm.init(mainContext)

        val config = RealmConfiguration.Builder()
                .name(mainContext.getString(R.string.walletAccountRealm))
                .build()

        realm = Realm.getInstance(config)

        realm!!.executeTransaction {

            val getWalletAccount = realm.where(WalletAccountRealm::class.java).equalTo("userUID",userID).findAll()

            getWalletAccount.forEach{
                dataList->

                    dataList.deleteFromRealm()
            }

            if(walletAccountList.size!=0) {
                walletAccountList.forEach {
                    data ->

                    val creating = realm.createObject(WalletAccountRealm::class.java, data.WalletAccountID)
                    creating.walletAccountName = data.WalletAccountName
                    creating.walletAccountInitialBalance = data.WalletAccountInitialBalance
                    creating.walletAccountStatus = data.WalletAccountStatus
                    creating.userUID = data.UserUID
                }
            }


        }

        realm.close()
        Log.e("WA: ","DONE CREATE")

    }


    private fun syncTransactionCategoryRealm(mainContext: Context, userID:String, transactionCategoryList:ArrayList<TransactionCategoryFirebase>){

        var realm: Realm? = null
        Realm.init(mainContext)

        val config = RealmConfiguration.Builder()
                .name(mainContext.getString(R.string.transactionCategoryRealm))
                .build()

        realm = Realm.getInstance(config)

        realm!!.executeTransaction {

            val getTransactionCategory = realm.where(TransactionCategoryRealm::class.java).equalTo("userUID",userID).findAll()

            getTransactionCategory.forEach{
                dataList->

                    dataList.deleteFromRealm()
            }

            if(transactionCategoryList.size!=0){
                transactionCategoryList.forEach {
                    data ->

                    val creating = realm.createObject(TransactionCategoryRealm::class.java, data.TransactionCategoryID)
                    creating.transactionCategoryName = data.TransactionCategoryName
                    creating.transactionCategoryStatus = data.TransactionCategoryStatus
                    creating.transactionCategoryType = data.TransactionCategoryType
                    creating.userUID = data.UserUID
                }
            }


        }

        realm.close()
        Log.e("TC: ","DONE CREATE")

    }


    private fun syncTransaction(mainContext: Context, userID:String, transactionList:ArrayList<TransactionFirebase>){
        var realm: Realm? = null
        Realm.init(mainContext)

        val config = RealmConfiguration.Builder()
                .name(mainContext.getString(R.string.transactionRealm))
                .build()

        realm = Realm.getInstance(config)

        realm!!.executeTransaction {

            val getTrx = realm.where(TransactionRealm::class.java).findAll()
            val gson = Gson()

            getTrx.forEach{
                dataList->

                val transactionCategoryData = gson.fromJson<TransactionCategoryFirebase>(dataList.transactionCategory, TransactionCategoryFirebase::class.java)

                if(transactionCategoryData.UserUID==userID){
                    dataList.deleteFromRealm()
                }
            }

            if(transactionList.size!=0) {
                transactionList.forEach {
                    data->

                    val convertedCategory = gson.toJson(data.TransactionCategory)
                    val convertedAccount = gson.toJson(data.WalletAccount)

                    val creating = realm.createObject(TransactionRealm::class.java, data.TransactionID)
                    creating.transactionDate = data.TransactionDate
                    creating.transactionTime = data.TransactionTime
                    creating.transactionAmount = data.TransactionAmount
                    creating.transactionRemark = data.TransactionRemark
                    creating.transactionCategory = convertedCategory
                    creating.walletAccount = convertedAccount

                }
            }


        }

        realm.close()
        Log.e("T: ","DONE CREATE")

    }



    // Menu Activity

    override fun backupDataPeriodicallyFirebase(mainContext: Context, userID: String){

        val bundle = PersistableBundle()
        bundle.putString("user", userID)


        val component= ComponentName(mainContext, SyncDataJobService::class.java)
        val info = JobInfo.Builder(123, component)
                .setPeriodic(TimeUnit.MINUTES.toMillis(1))
                .setExtras(bundle)
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


    override fun backupDataManuallyFirebase(mainContext: Context, userID: String) {

        val bundle = PersistableBundle()
        bundle.putString("user", userID)


        val component= ComponentName(mainContext, SyncDataJobService::class.java)
        val info = JobInfo.Builder(123, component)
                //.setPeriodic(TimeUnit.MINUTES.toMillis(1))
                .setExtras(bundle)
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



}