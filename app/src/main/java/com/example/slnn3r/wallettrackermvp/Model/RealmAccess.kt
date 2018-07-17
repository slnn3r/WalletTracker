package com.example.slnn3r.wallettrackermvp.Model

import android.content.Context
import com.example.slnn3r.wallettrackermvp.Interface.ModelInterface
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.Transaction
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.TransactionCategory
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.WalletAccount
import com.example.slnn3r.wallettrackermvp.Model.RealmClass.TransactionCategoryRealm
import com.example.slnn3r.wallettrackermvp.Model.RealmClass.TransactionRealm
import com.example.slnn3r.wallettrackermvp.Model.RealmClass.WalletAccountRealm
import com.example.slnn3r.wallettrackermvp.R
import com.example.slnn3r.wallettrackermvp.Utility.DefaultDataCategoryListItem
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import java.util.*
import kotlin.collections.ArrayList


class RealmAccess: ModelInterface.RealmAccess{

    // Get Data Only
    override fun getAccountDataRealm(mainContext: Context, userID: String): ArrayList<WalletAccount> {

        //
        var realm: Realm? = null

        val walletAccountData=ArrayList<WalletAccount>()


        try {
            Realm.init(mainContext)

            val config = RealmConfiguration.Builder()
                    .name(mainContext.getString(R.string.walletAccountRealm))
                    .build()

            realm = Realm.getInstance(config)


            realm!!.executeTransaction {

                val getWalletAccount = realm.where(WalletAccountRealm::class.java).equalTo(mainContext.getString(R.string.UserUID),userID).findAll()

                getWalletAccount.forEach{
                    dataList->

                    walletAccountData.add(
                            WalletAccount(
                                    dataList.walletAccountID!!,
                                    dataList.walletAccountName!!,
                                    dataList.walletAccountInitialBalance,
                                    dataList.userUID!!,
                                    dataList.walletAccountStatus!!
                            )
                    )
                }

            }

            realm.close()

           return walletAccountData


        }catch(e:Exception) {

            realm?.close()

            return ArrayList()

        }finally {
            realm?.close()
        }


    }

    override fun getCategoryDataRealm(mainContext: Context, userID: String): ArrayList<TransactionCategory> {

        //
        var realm: Realm? = null

        val transactionCategoryData=ArrayList<TransactionCategory>()

        try {
            Realm.init(mainContext)

            val config = RealmConfiguration.Builder()
                    .name(mainContext.getString(R.string.transactionCategoryRealm))
                    .build()

            realm = Realm.getInstance(config)


            realm!!.executeTransaction {


                val getTransactionCategory: RealmResults<TransactionCategoryRealm>? = realm.where(TransactionCategoryRealm::class.java).equalTo(mainContext.getString(R.string.UserUID),userID).findAll()

                getTransactionCategory!!.forEach{
                    dataList->

                    transactionCategoryData.add(
                            TransactionCategory(
                                    dataList.transactionCategoryID!!,
                                    dataList.transactionCategoryName!!,
                                    dataList.transactionCategoryType!!,
                                    dataList.transactionCategoryStatus!!,
                                    userID

                            )
                    )
                }



            }

            realm.close()

            return transactionCategoryData

        }catch(e:Exception) {

            realm?.close()

            return ArrayList()



        }finally {
            realm?.close()
        }

        //

    }



    // DashBoard Fragment
    override fun checkWalletAccountRealm(mainContext: Context, userID: String): ArrayList<WalletAccount> {

        //
        var realm: Realm? = null

        val walletAccountData=ArrayList<WalletAccount>()

            Realm.init(mainContext)

            val config = RealmConfiguration.Builder()
                    .name(mainContext.getString(R.string.walletAccountRealm))
                    .build()

            realm = Realm.getInstance(config)


            realm!!.executeTransaction {

                val getWalletAccount = realm.where(WalletAccountRealm::class.java).equalTo(mainContext.getString(R.string.UserUID),userID).findAll()

                getWalletAccount.forEach{
                    dataList->

                    walletAccountData.add(
                                WalletAccount(
                                        dataList.walletAccountID!!,
                                        dataList.walletAccountName!!,
                                        dataList.walletAccountInitialBalance,
                                        dataList.userUID!!,
                                        dataList.walletAccountStatus!!
                                )
                        )
                }

            }

            realm.close()

        //
        return walletAccountData

    }

    override fun firstTimeRealmSetup(mainContext: Context, userID: String): WalletAccount {  // Default data Setup, WalletAccount + TransactionCategory

        val uniqueID = UUID.randomUUID().toString()

        val defaultAccountName= mainContext.getString(R.string.defaultAccountName)
        val defaultAccountBalance= mainContext.getString(R.string.defaultAccountBalance).toDouble()
        val defaultAccountStatus= mainContext.getString(R.string.statusDefault)


        //
        var realm: Realm? = null

            Realm.init(mainContext)

            val config = RealmConfiguration.Builder()
                    .name(mainContext.getString(R.string.walletAccountRealm))
                    .build()

            realm = Realm.getInstance(config)


            realm!!.executeTransaction {

                val creating = realm!!.createObject(WalletAccountRealm::class.java, uniqueID)

                creating.walletAccountName= defaultAccountName
                creating.walletAccountInitialBalance= defaultAccountBalance
                creating.userUID=userID
                creating.walletAccountStatus=defaultAccountStatus

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

                    creating.transactionCategoryName= item.TransactionCategoryName
                    creating.transactionCategoryType= item.TransactionCategoryType
                    creating.transactionCategoryStatus= item.TransactionCategoryStatus
                    creating.userUID= userID

                }

            }

            ////
            realm.close()


        return WalletAccount(uniqueID,defaultAccountName,defaultAccountBalance,userID,defaultAccountStatus)
        //
    }


    override fun checkTransactionRealm(mainContext: Context, accountID: String): ArrayList<Transaction>{

        //
        var realm: Realm? = null

        val transactionData=ArrayList<Transaction>()


            Realm.init(mainContext)

            val config = RealmConfiguration.Builder()
                    .name(mainContext.getString(R.string.transactionRealm))
                    .build()

            realm = Realm.getInstance(config)


            realm!!.executeTransaction {

                val getTransaction = realm.where(TransactionRealm::class.java).equalTo(mainContext.getString(R.string.WalletAccountID),accountID).findAll()

                val transactionCategoryGSON= TransactionCategory("","","","","")

                getTransaction.forEach{
                    dataList->

                    transactionData.add(
                                Transaction(
                                        dataList.transactionID!!,
                                        dataList.transactionDate!!,
                                        dataList.transactionTime!!,
                                        dataList.transactionAmount,
                                        dataList.transactionRemark!!,
                                        transactionCategoryGSON,
                                        dataList.walletAccountID!!
                                )
                        )
                }

                val noResult = mainContext.getString(R.string.noResult) //ONLY USED FOR DASHBOARD GET TRANSACTION LIST

                if(transactionData.size<1){
                    transactionData.add(
                            Transaction(
                                    noResult,
                                    noResult,
                                    noResult,
                                    0.0,
                                    noResult,
                                    transactionCategoryGSON,
                                    noResult
                            )
                    )
                }

            }

            realm.close()

        return transactionData


    }



    // CreateWalletAccount Fragment

    override fun createWalletAccountRealm(mainContext: Context, walletAccountInput: WalletAccount) {

        //
        var realm: Realm? = null


            Realm.init(mainContext)

            val config = RealmConfiguration.Builder()
                    .name(mainContext.getString(R.string.walletAccountRealm))
                    .build()

            realm = Realm.getInstance(config)


            realm!!.executeTransaction {

                val creating = realm.createObject(WalletAccountRealm::class.java, walletAccountInput.WalletAccountID)

                creating.walletAccountName= walletAccountInput.WalletAccountName
                creating.walletAccountInitialBalance= walletAccountInput.WalletAccountInitialBalance
                creating.userUID= walletAccountInput.UserUID
                creating.walletAccountStatus= walletAccountInput.WalletAccountStatus


            }

            realm.close()

        //

    }


    // ViewWalletAccount Fragment

    override fun checkWalletAccountCountRealm(mainContext: Context, userID: String) : Int {

        //
        var realm: Realm? = null
        var count=0

            Realm.init(mainContext)

            val config = RealmConfiguration.Builder()
                    .name(mainContext.getString(R.string.walletAccountRealm))
                    .build()

            realm = Realm.getInstance(config)



            realm!!.executeTransaction {

                val getWalletAccount = realm.where(WalletAccountRealm::class.java).equalTo(mainContext.getString(R.string.UserUID),userID).findAll()

                getWalletAccount.forEach{
                    data ->

                        count+=1

                }

            }

            realm.close()

            return count

    }



    // DetailsWalletAccount Fragment
    override fun updateWalletAccountRealm(mainContext: Context, walletAccountData: WalletAccount) {

        //
        var realm: Realm? = null


            Realm.init(mainContext)

            val config = RealmConfiguration.Builder()
                    .name(mainContext.getString(R.string.walletAccountRealm))
                    .build()

            realm = Realm.getInstance(config)


            realm!!.executeTransaction {


                val getWalletAccount = realm.where(WalletAccountRealm::class.java).equalTo(mainContext.getString(R.string.WalletAccountID),walletAccountData.WalletAccountID).findAll()

                getWalletAccount.forEach{
                    dataList->

                        dataList.walletAccountName = walletAccountData.WalletAccountName
                        dataList.walletAccountInitialBalance = walletAccountData.WalletAccountInitialBalance

                }
            }

            realm.close()

    }

    override fun deleteWalletAccountRealm(mainContext: Context, walletAccountID: String) {

        //
        var realm: Realm? = null

            Realm.init(mainContext)

            val config = RealmConfiguration.Builder()
                    .name(mainContext.getString(R.string.walletAccountRealm))
                    .build()

            realm = Realm.getInstance(config)

            realm!!.executeTransaction {

                val getWalletAccount = realm.where(WalletAccountRealm::class.java).equalTo(mainContext.getString(R.string.WalletAccountID),walletAccountID).findAll()


                getWalletAccount.forEach{
                    dataList->

                        dataList.deleteFromRealm()
                }

            }

            realm.close()

        //
    }


    // ViewTrxCategory Fragment
    override fun checkTransactionCategoryRealm(mainContext: Context, userID: String, filterSelection: String):ArrayList<TransactionCategory> {

        val userIDRef = mainContext.getString(R.string.UserUID)
        val transactionCategoryTypeRef = mainContext.getString(R.string.TransactionCategoryType)

        //
        var realm: Realm? = null

        val transactionCategoryData=ArrayList<TransactionCategory>()

            Realm.init(mainContext)

            val config = RealmConfiguration.Builder()
                    .name(mainContext.getString(R.string.transactionCategoryRealm))
                    .build()

            realm = Realm.getInstance(config)


            realm!!.executeTransaction {


                var getTransactionCategory: RealmResults<TransactionCategoryRealm>? = null

                val incomeType = mainContext.getString(R.string.income)
                val expenseType = mainContext.getString(R.string.expense)

                if(filterSelection == incomeType){
                    getTransactionCategory= realm.where(TransactionCategoryRealm::class.java).equalTo(userIDRef,userID).equalTo(transactionCategoryTypeRef,incomeType).findAll()

                }else if(filterSelection == expenseType){
                    getTransactionCategory= realm.where(TransactionCategoryRealm::class.java).equalTo(userIDRef,userID).equalTo(transactionCategoryTypeRef,expenseType).findAll()

                }else{
                    getTransactionCategory= realm.where(TransactionCategoryRealm::class.java).equalTo(userIDRef,userID).findAll()

                }



                getTransactionCategory.forEach{
                    dataList->

                    transactionCategoryData.add(
                                TransactionCategory(
                                        dataList.transactionCategoryID!!,
                                        dataList.transactionCategoryName!!,
                                        dataList.transactionCategoryType!!,
                                        dataList.transactionCategoryStatus!!,
                                        userID

                                )
                        )
                }
            }

            realm.close()

        //
        return transactionCategoryData

    }


    // CreateTrxCategory Fragment
    override fun createTransactionCategoryRealm(mainContext: Context, trxCategoryInput: TransactionCategory) {

        //
        var realm: Realm? = null

            Realm.init(mainContext)

            val config = RealmConfiguration.Builder()
                    .name(mainContext.getString(R.string.transactionCategoryRealm))
                    .build()

            realm = Realm.getInstance(config)


            realm!!.executeTransaction {

                val creating = realm.createObject(TransactionCategoryRealm::class.java, trxCategoryInput.TransactionCategoryID)

                creating.transactionCategoryName= trxCategoryInput.TransactionCategoryName
                creating.transactionCategoryType= trxCategoryInput.TransactionCategoryType
                creating.transactionCategoryStatus= trxCategoryInput.TransactionCategoryStatus
                creating.userUID= trxCategoryInput.UserUID


            }

            realm.close()

        //

    }


    // DetailsTrxCategory Fragment
    override fun updateTransactionCategoryRealm(mainContext: Context, trxCategoryInput: TransactionCategory) {

        //
        var realm: Realm? = null

            Realm.init(mainContext)

            val config = RealmConfiguration.Builder()
                    .name(mainContext.getString(R.string.transactionCategoryRealm))
                    .build()

            realm = Realm.getInstance(config)


            realm!!.executeTransaction {


                val getTrxCategory = realm.where(TransactionCategoryRealm::class.java).equalTo(mainContext.getString(R.string.TransactionCategoryID), trxCategoryInput.TransactionCategoryID).findAll()

                getTrxCategory.forEach{
                    dataList->

                    dataList.transactionCategoryName = trxCategoryInput.TransactionCategoryName
                    dataList.transactionCategoryType = trxCategoryInput.TransactionCategoryType

                }
            }

            realm.close()

    }

    override fun deleteTransactionCategoryRealm(mainContext: Context, transactionCategoryID: String) {

        //
        var realm: Realm? = null

            Realm.init(mainContext)

            val config = RealmConfiguration.Builder()
                    .name(mainContext.getString(R.string.transactionCategoryRealm))
                    .build()

            realm = Realm.getInstance(config)

            realm!!.executeTransaction {

                val getWalletAccount = realm.where(TransactionCategoryRealm::class.java).equalTo(mainContext.getString(R.string.TransactionCategoryID),transactionCategoryID).findAll()


                getWalletAccount.forEach{
                    dataList->

                    dataList.deleteFromRealm()
                }

            }

            realm.close()

        //
    }


}