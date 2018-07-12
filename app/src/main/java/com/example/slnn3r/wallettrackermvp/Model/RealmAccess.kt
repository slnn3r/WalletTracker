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
import com.example.slnn3r.wallettrackermvp.Model.RealmClass.TransactionCategoryRealm
import com.example.slnn3r.wallettrackermvp.Model.RealmClass.TransactionRealm
import com.example.slnn3r.wallettrackermvp.Model.RealmClass.WalletAccountRealm
import com.example.slnn3r.wallettrackermvp.Presenter.Presenter
import com.example.slnn3r.wallettrackermvp.R
import com.example.slnn3r.wallettrackermvp.Utility.DefaultDataCategoryListItem
import com.example.slnn3r.wallettrackermvp.View.Fragment.DashBoardFragment
import com.example.slnn3r.wallettrackermvp.View.Fragment.TrxCategory.ViewTrxCategoryFragment
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


        val view = (mainContext as Activity).findViewById(R.id.navMenu) as View
        val currentDestination = findNavController(view).currentDestination.id

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
                    .name(mainContext.getString(R.string.walletAccountRealm))
                    .build()

            realm = Realm.getInstance(config)


            realm!!.executeTransaction {

                val getWalletAccount = realm.where(WalletAccountRealm::class.java).equalTo("UserUID",userID).findAll()

                getWalletAccount.forEach{
                    dataList->

                        WalletAccountData.add(
                                WalletAccount(
                                        dataList.WalletAccountID!!,
                                        dataList.WalletAccountName!!,
                                        dataList.WalletAccountInitialBalance,
                                        dataList.UserUID!!,
                                        dataList.WalletAccountStatus!!
                                )
                        )
                }

            }

            realm.close()

            presenter.checkWalletAccountResult(mainContext,WalletAccountData, mainContext.getString(R.string.statusSuccess))


        }catch(e:Exception) {

            realm?.close()

            val WalletAccountData=ArrayList<WalletAccount>()
            presenter.checkWalletAccountResult(mainContext,WalletAccountData,mainContext.getString(R.string.statusFail)+e.toString())

        }finally {
            realm?.close()
        }

        //


    }

    override fun firstTimeRealmSetup(mainContext: Context, userID: String) {  // Default data Setup, WalletAccount + TransactionCategory

        presenter= Presenter(DashBoardFragment())

        val uniqueID = UUID.randomUUID().toString()

        val defaultAccountName= mainContext.getString(R.string.defaultAccountName)
        val defaultAccountBalance= mainContext.getString(R.string.defaultAccountBalance).toDouble()
        val defaultAccountStatus= mainContext.getString(R.string.statusDefault)


        //
        var realm: Realm? = null
        try {
            Realm.init(mainContext)

            val config = RealmConfiguration.Builder()
                    .name(mainContext.getString(R.string.walletAccountRealm))
                    .build()

            realm = Realm.getInstance(config)


            realm!!.executeTransaction {

                val creating = realm!!.createObject(WalletAccountRealm::class.java, uniqueID)

                creating.WalletAccountName= defaultAccountName
                creating.WalletAccountInitialBalance= defaultAccountBalance
                creating.UserUID=userID
                creating.WalletAccountStatus=defaultAccountStatus

            }


            realm.close()



            ////
            val config2 = RealmConfiguration.Builder()
                    .name(mainContext.getString(R.string.transactionCategoryRealm))
                    .build()

            realm = Realm.getInstance(config2)


            realm!!.executeTransaction {

                for(item in DefaultDataCategoryListItem().getListItem()){

                    val creating = realm.createObject(TransactionCategoryRealm::class.java, UUID.randomUUID().toString())

                    creating.TransactionCategoryName= item.TransactionCategoryName
                    creating.TransactionCategoryType= item.TransactionCategoryType
                    creating.TransactionCategoryStatus= item.TransactionCategoryStatus
                    creating.UserUID= userID

                }

            }

            ////
            realm.close()


            presenter.firstTimeSetupStatus(mainContext,WalletAccount(uniqueID,defaultAccountName,defaultAccountBalance,userID,defaultAccountStatus), mainContext.getString(R.string.statusSuccess))


        }catch(e:Exception) {

            realm?.close()

            presenter.firstTimeSetupStatus(mainContext,WalletAccount("","",0.0,"",""), mainContext.getString(R.string.statusFail)+e.toString())



        }finally {
            realm?.close()
        }

        //


    }


    override fun checkTransactionRealm(mainContext: Context, accountID: String) {


        presenter= Presenter(DashBoardFragment())

        //
        var realm: Realm? = null

        val TransactionData=ArrayList<Transaction>()


        try {
            Realm.init(mainContext)

            val config = RealmConfiguration.Builder()
                    .name(mainContext.getString(R.string.transactionRealm))
                    .build()

            realm = Realm.getInstance(config)


            realm!!.executeTransaction {

                val getTransaction = realm.where(TransactionRealm::class.java).equalTo("WalletAccountID",accountID).findAll() //naming wrong

                val TransactionCategoryGSON= TransactionCategory("","","","","")

                getTransaction.forEach{
                    dataList->

                        TransactionData.add(
                                Transaction(
                                        dataList.TransactionID!!,
                                        dataList.TransactionDate!!,
                                        dataList.TransactionTime!!,
                                        dataList.TransactionAmount,
                                        dataList.TransactionRemark!!,
                                        TransactionCategoryGSON,
                                        dataList.WalletAccountID!!
                                )
                        )
                }

                val noResult = mainContext.getString(R.string.noResult)

                if(TransactionData.size<1){
                    TransactionData.add(
                            Transaction(
                                    noResult,
                                    noResult,
                                    noResult,
                                    0.0,
                                    noResult,
                                    TransactionCategoryGSON,
                                    noResult
                            )
                    )
                }

            }

            realm.close()

            presenter.checkTransactionResult(mainContext, TransactionData,mainContext.getString(R.string.statusSuccess))


        }catch(e:Exception) {

            realm?.close()

            val TransactionData =ArrayList<Transaction>()

            presenter.checkTransactionResult(mainContext, TransactionData ,mainContext.getString(R.string.statusFail)+e.toString())



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
                    .name(mainContext.getString(R.string.walletAccountRealm))
                    .build()

            realm = Realm.getInstance(config)


            realm!!.executeTransaction {

                val creating = realm.createObject(WalletAccountRealm::class.java, walletAccountInput.WalletAccountID)

                creating.WalletAccountName= walletAccountInput.WalletAccountName
                creating.WalletAccountInitialBalance= walletAccountInput.WalletAccountInitialBalance
                creating.UserUID= walletAccountInput.UserUID
                creating.WalletAccountStatus= walletAccountInput.WalletAccountStatus


            }

            realm.close()
            presenter.createWalletAccountStatus(mainContext, mainContext.getString(R.string.statusSuccess))



        }catch(e:Exception) {

            realm?.close()
            presenter.createWalletAccountStatus(mainContext, mainContext.getString(R.string.statusFail)+e.toString())


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
        var count=0

        try {

            Realm.init(mainContext)

            val config = RealmConfiguration.Builder()
                    .name(mainContext.getString(R.string.walletAccountRealm))
                    .build()

            realm = Realm.getInstance(config)



            realm!!.executeTransaction {

                val getWalletAccount = realm.where(WalletAccountRealm::class.java).equalTo("UserUID",userID).findAll()

                getWalletAccount.forEach{
                    data ->

                        count+=1

                }

            }

            realm.close()
            presenter.checkWalletAccountCountResult(mainContext,count,mainContext.getString(R.string.statusSuccess))



        }catch(e:Exception) {

            realm?.close()
            presenter.checkWalletAccountCountResult(mainContext,0,mainContext.getString(R.string.statusFail)+e.toString())


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
                    .name(mainContext.getString(R.string.walletAccountRealm))
                    .build()

            realm = Realm.getInstance(config)


            realm!!.executeTransaction {


                val getWalletAccount = realm.where(WalletAccountRealm::class.java).equalTo("WalletAccountID",walletAccountData.WalletAccountID).findAll()

                getWalletAccount.forEach{
                    dataList->

                        dataList.WalletAccountName = walletAccountData.WalletAccountName
                        dataList.WalletAccountInitialBalance = walletAccountData.WalletAccountInitialBalance

                }
            }

            realm.close()
            presenter.updateWalletAccountStatus(mainContext,mainContext.getString(R.string.statusSuccess))



        }catch(e:Exception) {

            realm?.close()
            presenter.updateWalletAccountStatus(mainContext,mainContext.getString(R.string.statusFail)+e.toString())



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
                    .name(mainContext.getString(R.string.walletAccountRealm))
                    .build()

            realm = Realm.getInstance(config)

            realm!!.executeTransaction {

                val getWalletAccount = realm.where(WalletAccountRealm::class.java).equalTo("WalletAccountID",walletAccountID).findAll() //naming wrong


                getWalletAccount.forEach{
                    dataList->

                        dataList.deleteFromRealm()
                }

            }

            realm.close()
            presenter.deleteWalletAccountStatus(mainContext,mainContext.getString(R.string.statusSuccess))



        }catch(e:Exception) {

            realm?.close()
            presenter.deleteWalletAccountStatus(mainContext,mainContext.getString(R.string.statusFail)+e.toString())



        }finally {
            realm?.close()
        }

        //
    }


    // ViewTrxCategory Fragment
    override fun checkTransactionCategoryRealm(mainContext: Context, userID: String) {

        presenter= Presenter(ViewTrxCategoryFragment())

        //
        var realm: Realm? = null

        val TransactionCategoryData=ArrayList<TransactionCategory>()

        try {
            Realm.init(mainContext)

            val config = RealmConfiguration.Builder()
                    .name(mainContext.getString(R.string.transactionCategoryRealm))
                    .build()

            realm = Realm.getInstance(config)


            realm!!.executeTransaction {

                val getTransactionCategory = realm.where(TransactionCategoryRealm::class.java).equalTo("UserUID",userID).findAll() //naming wrong


                val TransactionCategoryGSON= TransactionCategory("","","","","")

                getTransactionCategory.forEach{
                    dataList->

                        TransactionCategoryData.add(
                                TransactionCategory(
                                        dataList.TransactionCategoryID!!,
                                        dataList.TransactionCategoryName!!,
                                        dataList.TransactionCategoryType!!,
                                        dataList.TransactionCategoryStatus!!,
                                        userID

                                )
                        )
                }

                val noResult = mainContext.getString(R.string.noResult)

                if(TransactionCategoryData.size<1){
                    TransactionCategoryData.add(
                            TransactionCategory(
                                    noResult,
                                    noResult,
                                    noResult,
                                    noResult,
                                    noResult
                            )
                    )
                }

            }

            realm.close()

            presenter.checkTransactionCategoryResult(mainContext, TransactionCategoryData,mainContext.getString(R.string.statusSuccess))


        }catch(e:Exception) {

            realm?.close()

            val TransactionCategoryData=ArrayList<TransactionCategory>()

            presenter.checkTransactionCategoryResult(mainContext, TransactionCategoryData ,mainContext.getString(R.string.statusFail)+e.toString())



        }finally {
            realm?.close()
        }

        //


    }


}