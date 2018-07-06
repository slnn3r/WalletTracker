package com.example.slnn3r.wallettrackermvp.Model

import android.content.Context
import android.widget.Toast
import com.example.slnn3r.wallettrackermvp.Interface.ModelInterface
import com.example.slnn3r.wallettrackermvp.Interface.PresenterInterface
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.WalletAccount
import com.example.slnn3r.wallettrackermvp.Model.RealmClass.WalletAccountRealm
import com.example.slnn3r.wallettrackermvp.Presenter.Presenter
import com.example.slnn3r.wallettrackermvp.View.Activity.MenuActivity
import com.example.slnn3r.wallettrackermvp.View.Fragment.DashBoardFragment
import io.realm.Realm
import io.realm.RealmConfiguration

class RealmAccess: ModelInterface.RealmAccess{

    private lateinit var presenter: PresenterInterface.Presenter


    // DashBoard Fragment
    override fun checkWalletAccountRealm(mainContext: Context, userID: String) {

        presenter= Presenter(DashBoardFragment())


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
                WalletAccountData.add(
                        WalletAccount(
                                dataList.WalletAccountID!!,
                                dataList.WalletAccountName!!,
                                dataList.WalletAccountInitialBalance!!,
                                dataList.UserID!!
                        )
                )
        }

        realm.commitTransaction()

        presenter.checkWalletAccountResult(mainContext,WalletAccountData)

    }

    override fun firstTimeRealmSetup(mainContext: Context, userID: String) {

        presenter= Presenter(DashBoardFragment())


        Realm.init(mainContext)

        val config = RealmConfiguration.Builder()
                .name("walletaccount.realm")
                .build()

        val realm = Realm.getInstance(config)


        realm.beginTransaction()

        val creating = realm.createObject(WalletAccountRealm::class.java, userID+"1")

        creating.WalletAccountName= "Personal"
        creating.WalletAccountInitialBalance=0.0
        creating.UserID=userID

        realm.commitTransaction()




        presenter.firstTimeSetupStatus(mainContext,WalletAccount(userID+1,"Personal",0.0,userID))

    }

}