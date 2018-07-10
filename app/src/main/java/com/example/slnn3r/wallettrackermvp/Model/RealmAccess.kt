package com.example.slnn3r.wallettrackermvp.Model

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast
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
import com.example.slnn3r.wallettrackermvp.View.Activity.MenuActivity
import com.example.slnn3r.wallettrackermvp.View.Fragment.DashBoardFragment
import com.example.slnn3r.wallettrackermvp.View.Fragment.WalletAccount.CreateWalletAccountFragment
import com.example.slnn3r.wallettrackermvp.View.Fragment.WalletAccount.ViewWalletAccountFragment
import io.realm.Realm
import io.realm.RealmConfiguration
import java.util.*
import java.util.UUID.randomUUID



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



        Realm.init(mainContext)

        val config = RealmConfiguration.Builder()
                .name("walletaccount.realm")
                .build()

        val realm = Realm.getInstance(config)

        realm.beginTransaction()

        val getWalletAccount = realm.where(WalletAccountRealm::class.java).findAll()

        var WalletAccountData=ArrayList<WalletAccount>()

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

        realm.commitTransaction()

        presenter.checkWalletAccountResult(mainContext,WalletAccountData)

    }

    override fun firstTimeRealmSetup(mainContext: Context, userID: String) {

        presenter= Presenter(DashBoardFragment())

        val uniqueID = UUID.randomUUID().toString()


        Realm.init(mainContext)

        val config = RealmConfiguration.Builder()
                .name("walletaccount.realm")
                .build()

        val realm = Realm.getInstance(config)


        realm.beginTransaction()

        val creating = realm.createObject(WalletAccountRealm::class.java, uniqueID)

        creating.WalletAccountName= "Personal"
        creating.WalletAccountInitialBalance=0.0
        creating.UserID=userID
        creating.WalletAccountStatus="Default"

        realm.commitTransaction()


        presenter.firstTimeSetupStatus(mainContext,WalletAccount(uniqueID,"Personal",0.0,userID,"Default"))

    }


    override fun checkTransactionRealm(mainContext: Context, accountID: String) {


        presenter= Presenter(DashBoardFragment())


        Realm.init(mainContext)

        val config = RealmConfiguration.Builder()
                .name("transaction.realm")
                .build()

        val realm = Realm.getInstance(config)

        realm.beginTransaction()

        val getWalletAccount = realm.where(TransactionRealm::class.java).findAll() //naming wrong

        var TransactionData=ArrayList<Transaction>()

        var TransactionCategoryGSON= TransactionCategory("","","","","")

        getWalletAccount.forEach{
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

        realm.commitTransaction()

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

        presenter.checkTransactionResult(mainContext, TransactionData)

    }


    override fun createWalletAccountRealm(mainContext: Context, walletAccountInput: WalletAccount) {

        ////

        presenter= Presenter(CreateWalletAccountFragment())


        Realm.init(mainContext)

        val config = RealmConfiguration.Builder()
                .name("walletaccount.realm")
                .build()

        val realm = Realm.getInstance(config)


        realm.beginTransaction()

        val creating = realm.createObject(WalletAccountRealm::class.java, walletAccountInput.WalletAccountID)

        creating.WalletAccountName= walletAccountInput.WalletAccountName
        creating.WalletAccountInitialBalance= walletAccountInput.WalletAccountInitialBalance
        creating.UserID= walletAccountInput.userUID
        creating.WalletAccountStatus= walletAccountInput.WalletAccountStatus

        realm.commitTransaction()

        presenter.createWalletAccountStatus(mainContext, "Success")

    }


    override fun checkWalletAccountCountRealm(mainContext: Context) {

        presenter= Presenter(ViewWalletAccountFragment())


        Realm.init(mainContext)

        val config = RealmConfiguration.Builder()
                .name("walletaccount.realm")
                .build()

        val realm = Realm.getInstance(config)

        realm.beginTransaction()

        val getWalletAccount = realm.where(WalletAccountRealm::class.java).findAll()



        realm.commitTransaction()

        var count:Int=0

        getWalletAccount.forEach{
            data ->
            count+=1
        }


        presenter.checkWalletAccountCountResult(mainContext,getWalletAccount.size)

    }

}