package com.example.slnn3r.wallettrackermvp.Model

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.navigation.Navigation.findNavController
import com.example.slnn3r.wallettrackermvp.Interface.ModelInterface
import com.example.slnn3r.wallettrackermvp.Interface.PresenterInterface
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.Transaction
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.TransactionCategory
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.WalletAccount
import com.example.slnn3r.wallettrackermvp.Model.RealmClass.TransactionRealm
import com.example.slnn3r.wallettrackermvp.Model.RealmClass.WalletAccountRealm
import com.example.slnn3r.wallettrackermvp.Presenter.Presenter
import com.example.slnn3r.wallettrackermvp.R
import com.example.slnn3r.wallettrackermvp.View.Fragment.DashBoardFragment
import com.example.slnn3r.wallettrackermvp.View.Fragment.WalletAccount.CreateWalletAccountFragment
import com.example.slnn3r.wallettrackermvp.View.Fragment.WalletAccount.DetailsWalletAccountFragment
import com.example.slnn3r.wallettrackermvp.View.Fragment.WalletAccount.ViewWalletAccountFragment
import io.realm.Realm
import io.realm.RealmConfiguration
import java.util.*


class RealmAccess: ModelInterface.RealmAccess{



    private lateinit var presenter: PresenterInterface.Presenter


    // DashBoard Fragment
    override fun checkWalletAccountRealm(mainContext: Context, userID: String) {


        var view = (mainContext as Activity).findViewById(R.id.navMenu) as View
        var currentDestination = findNavController(view).currentDestination.id

        if(currentDestination==R.id.dashBoardFragment){

            presenter= Presenter(DashBoardFragment())

        }else if(currentDestination==R.id.viewWalletAccountFragment){

            presenter= Presenter(ViewWalletAccountFragment())

        }


        //
        var realm: Realm? = null

        var WalletAccountData=ArrayList<WalletAccount>()


        try {
            Realm.init(mainContext)

            val config = RealmConfiguration.Builder()
                    .name("walletaccount.realm")
                    .build()

            realm = Realm.getInstance(config)


            realm!!.executeTransaction {

                val getWalletAccount = realm.where(WalletAccountRealm::class.java).findAll()


                getWalletAccount.forEach{
                    dataList->

                    if(userID==dataList.UserID){
                        WalletAccountData.add(
                                WalletAccount(
                                        dataList.WalletAccountID!!,
                                        dataList.WalletAccountName!!,
                                        dataList.WalletAccountInitialBalance!!,
                                        dataList.UserID!!,
                                        dataList.WalletAccountStatus!!
                                )
                        )
                    }


                }


            }

            realm?.close()

            presenter.checkWalletAccountResult(mainContext,WalletAccountData, "Success")


        }catch(e:Exception) {

            realm?.close()

            var WalletAccountData=ArrayList<WalletAccount>()
            presenter.checkWalletAccountResult(mainContext,WalletAccountData,"Fail: "+e.toString())

        }finally {
            realm?.close()
        }

        //


    }

    override fun firstTimeRealmSetup(mainContext: Context, userID: String) {

        presenter= Presenter(DashBoardFragment())

        val uniqueID = UUID.randomUUID().toString()

        //
        var realm: Realm? = null
        try {
            Realm.init(mainContext)

            val config = RealmConfiguration.Builder()
                    .name("walletaccount.realm")
                    .build()

            realm = Realm.getInstance(config)


            realm!!.executeTransaction {

                val creating = realm.createObject(WalletAccountRealm::class.java, uniqueID)

                creating.WalletAccountName= "Personal"
                creating.WalletAccountInitialBalance=0.0
                creating.UserID=userID
                creating.WalletAccountStatus="Default"



            }


            realm?.close()

            presenter.firstTimeSetupStatus(mainContext,WalletAccount(uniqueID,"Personal",0.0,userID,"Default"), "Success")


        }catch(e:Exception) {

            realm?.close()

            presenter.firstTimeSetupStatus(mainContext,WalletAccount("","",0.0,"",""), "Fail: "+e.toString())



        }finally {
            realm?.close()
        }

        //


    }


    override fun checkTransactionRealm(mainContext: Context, accountID: String) {


        presenter= Presenter(DashBoardFragment())

        //
        var realm: Realm? = null

        var TransactionData=ArrayList<Transaction>()


        try {
            Realm.init(mainContext)

            val config = RealmConfiguration.Builder()
                    .name("transaction.realm")
                    .build()

            realm = Realm.getInstance(config)


            realm!!.executeTransaction {

                val getTransaction = realm.where(TransactionRealm::class.java).findAll() //naming wrong


                var TransactionCategoryGSON= TransactionCategory("","","","","")

                getTransaction.forEach{
                    dataList->

                    if(dataList.WalletAccountID==accountID) {



                        TransactionData.add(
                                Transaction(
                                        dataList.TransactionID!!,
                                        dataList.TransactionDate!!,
                                        dataList.TransactionTime!!,
                                        dataList.TransactionAmount!!,
                                        dataList.TransactionRemark!!,
                                        TransactionCategoryGSON,
                                        dataList.WalletAccountID!!
                                )
                        )

                    }

                }

                if(TransactionData.size<1){
                    TransactionData.add(
                            Transaction(
                                    "No Search Found",
                                    "No Search Found",
                                    "No Search Found",
                                    0.0,
                                    "No Search Found",
                                    TransactionCategoryGSON,
                                    "No Search Found"
                            )
                    )
                }

            }

            realm?.close()

            presenter.checkTransactionResult(mainContext, TransactionData,"Success")


        }catch(e:Exception) {

            realm?.close()

            var TransactionData=ArrayList<Transaction>()

            presenter.checkTransactionResult(mainContext, TransactionData,"Fail: "+e.toString())



        }finally {
            realm?.close()
        }

        //



    }



    // CreateWalletAccount Fragment

    override fun createWalletAccountRealm(mainContext: Context, walletAccountInput: WalletAccount) {

        ////
        presenter= Presenter(CreateWalletAccountFragment())

        //
        var realm: Realm? = null


        try {
            Realm.init(mainContext)

            val config = RealmConfiguration.Builder()
                    .name("walletaccount.realm")
                    .build()

            realm = Realm.getInstance(config)


            realm!!.executeTransaction {

                val creating = realm.createObject(WalletAccountRealm::class.java, walletAccountInput.WalletAccountID)

                creating.WalletAccountName= walletAccountInput.WalletAccountName
                creating.WalletAccountInitialBalance= walletAccountInput.WalletAccountInitialBalance
                creating.UserID= walletAccountInput.userUID
                creating.WalletAccountStatus= walletAccountInput.WalletAccountStatus


            }

            realm?.close()
            presenter.createWalletAccountStatus(mainContext, "Success")



        }catch(e:Exception) {

            realm?.close()
            presenter.createWalletAccountStatus(mainContext, "Fail: "+e.toString())


        }finally {
            realm?.close()
        }

        //


    }


    // ViewWalletAccount Fragment

    override fun checkWalletAccountCountRealm(mainContext: Context, userID: String) {

        presenter= Presenter(ViewWalletAccountFragment())


        //
        var realm: Realm? = null
        var count:Int=0

        try {

            Realm.init(mainContext)

            val config = RealmConfiguration.Builder()
                    .name("walletaccount.realm")
                    .build()

            realm = Realm.getInstance(config)



            realm!!.executeTransaction {

                val getWalletAccount = realm.where(WalletAccountRealm::class.java).findAll()


                getWalletAccount.forEach{
                    data ->

                    if(data.UserID==userID){
                        count+=1
                    }

                }

            }


            realm?.close()
            presenter.checkWalletAccountCountResult(mainContext,count,"Success")



        }catch(e:Exception) {

            realm?.close()
            presenter.checkWalletAccountCountResult(mainContext,0,"Fail"+e.toString())


        }finally {
            realm?.close()
        }

        //



    }



    // DetailsWalletAccount Fragment
    override fun updateWalletAccountRealm(mainContext: Context, walletAccountData: WalletAccount) {

        presenter= Presenter(DetailsWalletAccountFragment())

        //
        var realm: Realm? = null


        try {

            Realm.init(mainContext)

            val config = RealmConfiguration.Builder()
                    .name("walletaccount.realm")
                    .build()

            realm = Realm.getInstance(config)


            realm!!.executeTransaction {


                val getWalletAccount = realm.where(WalletAccountRealm::class.java).findAll() //naming wrong


                getWalletAccount.forEach{
                    dataList->

                    if(dataList.WalletAccountID==walletAccountData.WalletAccountID) {

                        dataList.WalletAccountName = walletAccountData.WalletAccountName
                        dataList.WalletAccountInitialBalance = walletAccountData.WalletAccountInitialBalance

                    }


                }

            }

            realm?.close()
            presenter.updateWalletAccountStatus(mainContext,"Success")



        }catch(e:Exception) {

            realm?.close()
            presenter.updateWalletAccountStatus(mainContext,"Fail: "+e.toString())



        }finally {
            realm?.close()
        }

        //

        

    }

    override fun deleteWalletAccountRealm(mainContext: Context, walletAccountID: String) {

        presenter= Presenter(DetailsWalletAccountFragment())


        //
        var realm: Realm? = null


        try {

            Realm.init(mainContext)

            val config = RealmConfiguration.Builder()
                    .name("walletaccount.realm")
                    .build()

            val realm = Realm.getInstance(config)

            realm!!.executeTransaction {

                val getWalletAccount = realm.where(WalletAccountRealm::class.java).findAll() //naming wrong


                getWalletAccount.forEach{
                    dataList->

                    if(dataList.WalletAccountID==walletAccountID) {

                        dataList.deleteFromRealm()

                    }

                }


            }

            realm?.close()
            presenter.deleteWalletAccountStatus(mainContext,"Success")



        }catch(e:Exception) {

            realm?.close()
            presenter.deleteWalletAccountStatus(mainContext,"Fail: "+e.toString())



        }finally {
            realm?.close()
        }

        //
        
        


        



    }

}