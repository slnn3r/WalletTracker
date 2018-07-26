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
import com.google.gson.Gson
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import io.realm.Sort
import java.text.SimpleDateFormat
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


    override fun getAccountDataByNameRealm(mainContext: Context, userID: String, accountName: String): WalletAccount {

//
        var realm: Realm? = null

        var walletAccountData = WalletAccount("","",0.0,"","")


        try {
            Realm.init(mainContext)

            val config = RealmConfiguration.Builder()
                    .name(mainContext.getString(R.string.walletAccountRealm))
                    .build()

            realm = Realm.getInstance(config)


            realm!!.executeTransaction {

                val getWalletAccount = realm.where(WalletAccountRealm::class.java)
                        .equalTo(mainContext.getString(R.string.UserUID),userID)
                        .equalTo(mainContext.getString(R.string.WalletAccountName), accountName)
                        .findAll()

                getWalletAccount.forEach{
                    dataList->

                    walletAccountData= WalletAccount(
                                    dataList.walletAccountID!!,
                                    dataList.walletAccountName!!,
                                    dataList.walletAccountInitialBalance,
                                    dataList.userUID!!,
                                    dataList.walletAccountStatus!!
                            )
                }

            }

            realm.close()

            return walletAccountData


        }catch(e:Exception) {

            realm?.close()

            return walletAccountData

        }finally {
            realm?.close()
        }
    }

    override fun getCategoryDataByNameRealm(mainContext: Context, userID: String, categoryName: String): TransactionCategory {

        var realm: Realm? = null

        var transactionCategoryData= TransactionCategory("","","","","")

        try {
            Realm.init(mainContext)

            val config = RealmConfiguration.Builder()
                    .name(mainContext.getString(R.string.transactionCategoryRealm))
                    .build()

            realm = Realm.getInstance(config)


            realm!!.executeTransaction {


                val getTransactionCategory: RealmResults<TransactionCategoryRealm>? = realm.where(TransactionCategoryRealm::class.java)
                        .equalTo(mainContext.getString(R.string.UserUID),userID)
                        .equalTo(mainContext.getString(R.string.TransactionCategoryName),categoryName)
                        .findAll()

                getTransactionCategory!!.forEach{
                    dataList->

                    transactionCategoryData =  TransactionCategory(
                                    dataList.transactionCategoryID!!,
                                    dataList.transactionCategoryName!!,
                                    dataList.transactionCategoryType!!,
                                    dataList.transactionCategoryStatus!!,
                                    userID

                            )

                }



            }

            realm.close()

            return transactionCategoryData

        }catch(e:Exception) {

            realm?.close()

            return transactionCategoryData



        }finally {
            realm?.close()
        }
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


    override fun checkTransactionRealm(mainContext: Context, accountID: String, userID: String): ArrayList<Transaction>{

        //
        var realm: Realm? = null

        val transactionData=ArrayList<Transaction>()


            Realm.init(mainContext)

            val config = RealmConfiguration.Builder()
                    .name(mainContext.getString(R.string.transactionRealm))
                    .build()

            realm = Realm.getInstance(config)


            realm!!.executeTransaction {

                val getTransaction = realm.where(TransactionRealm::class.java)
                        .sort(mainContext.getString(R.string.TransactionDate), Sort.DESCENDING,mainContext.getString(R.string.TransactionTime), Sort.DESCENDING)
                        .findAll()


                var count=0

                getTransaction.forEach{
                    dataList->

                    val gson = Gson()

                    val walletAccount = dataList.walletAccount
                    val transactionCategory = dataList.transactionCategory

                    val walletAccountData = gson.fromJson<WalletAccount>(walletAccount, WalletAccount::class.java)
                    val transactionCategoryData = gson.fromJson<TransactionCategory>(transactionCategory, TransactionCategory::class.java)



                    if(walletAccountData.WalletAccountID==accountID && walletAccountData.UserUID==userID && count<10){

                        // convert to 12hour for ez display purpose
                        val notConvertedTime = dataList.transactionTime!!
                        val date12Format = SimpleDateFormat(mainContext.getString(R.string.timeFormat12))
                        val date24Format = SimpleDateFormat(mainContext.getString(R.string.timeFormat24))
                        val convertedTime = date12Format.format(date24Format.parse(notConvertedTime))

                        transactionData.add(

                                Transaction(
                                        dataList.transactionID!!,
                                        dataList.transactionDate!!,
                                        convertedTime,
                                        dataList.transactionAmount,
                                        dataList.transactionRemark!!,
                                        transactionCategoryData,
                                        walletAccountData
                                )
                        )
                        count+=1

                    }


                }

                val noResult = mainContext.getString(R.string.noResult) //ONLY USED FOR DASHBOARD GET TRANSACTION LIST

                val transactionCategoryNull= TransactionCategory("","","","","")
                val walletAccountNull= WalletAccount("","",0.0,"","")


                if(transactionData.size<1){
                    transactionData.add(
                            Transaction(
                                    noResult,
                                    noResult,
                                    noResult,
                                    0.0,
                                    noResult,
                                    transactionCategoryNull,
                                    walletAccountNull
                            )
                    )
                }

            }

            realm.close()

        return transactionData


    }


    override fun getCurrentBalanceRealm(mainContext: Context, userID: String, accountID: String): Double {

        //
        var realm: Realm? = null

        var balance=0.0

        Realm.init(mainContext)

        val config = RealmConfiguration.Builder()
                .name(mainContext.getString(R.string.walletAccountRealm))
                .build()

        realm = Realm.getInstance(config)


        realm!!.executeTransaction {

            val getWalletAccount = realm.where(WalletAccountRealm::class.java)
                    .equalTo(mainContext.getString(R.string.UserUID),userID)
                    .equalTo(mainContext.getString(R.string.WalletAccountID),accountID)
                    .findAll()

            getWalletAccount.forEach{
                dataList->

                balance=dataList.walletAccountInitialBalance

            }

        }

        realm.close()

        //
        return balance

    }

    override fun getAllIncome(mainContext: Context, userID: String, accountID: String): Double {

        var income=0.0
        var realm: Realm? = null

        Realm.init(mainContext)

        val config = RealmConfiguration.Builder()
                .name(mainContext.getString(R.string.transactionRealm))
                .build()

        realm = Realm.getInstance(config)


        realm!!.executeTransaction {

            val getTransaction = realm.where(TransactionRealm::class.java)
                    .findAll()


            getTransaction.forEach { dataList ->

                val gson = Gson()
                val walletAccount = dataList.walletAccount
                val transactionCategory = dataList.transactionCategory
                val walletAccountData = gson.fromJson<WalletAccount>(walletAccount, WalletAccount::class.java)
                val transactionCategoryData = gson.fromJson<TransactionCategory>(transactionCategory, TransactionCategory::class.java)

                if(transactionCategoryData.TransactionCategoryType==mainContext.getString(R.string.income)&&walletAccountData.WalletAccountID==accountID){
                    income+=dataList.transactionAmount
                }

            }
        }

        return income

    }

    override fun getAllExpense(mainContext: Context, userID: String, accountID: String): Double {

        var expense=0.0
        var realm: Realm? = null

        Realm.init(mainContext)

        val config = RealmConfiguration.Builder()
                .name(mainContext.getString(R.string.transactionRealm))
                .build()

        realm = Realm.getInstance(config)


        realm!!.executeTransaction {

            val getTransaction = realm.where(TransactionRealm::class.java)
                    .findAll()


            getTransaction.forEach { dataList ->

                val gson = Gson()
                val walletAccount = dataList.walletAccount
                val transactionCategory = dataList.transactionCategory
                val walletAccountData = gson.fromJson<WalletAccount>(walletAccount, WalletAccount::class.java)
                val transactionCategoryData = gson.fromJson<TransactionCategory>(transactionCategory, TransactionCategory::class.java)

                if(transactionCategoryData.TransactionCategoryType==mainContext.getString(R.string.expense)&&walletAccountData.WalletAccountID==accountID){
                    expense+=dataList.transactionAmount
                }

            }
        }

        return expense

    }

    override fun getThisMonthExpenseRealm(mainContext: Context, userID: String, accountID: String): ArrayList<Transaction> {

        var realm: Realm? = null
        val transactionData=ArrayList<Transaction>()

        Realm.init(mainContext)

        val config = RealmConfiguration.Builder()
                .name(mainContext.getString(R.string.transactionRealm))
                .build()

        realm = Realm.getInstance(config)


        realm!!.executeTransaction {

            val getTransaction = realm.where(TransactionRealm::class.java)
                    .findAll()


            getTransaction.forEach { dataList ->

                val gson = Gson()
                val walletAccount = dataList.walletAccount
                val transactionCategory = dataList.transactionCategory
                val walletAccountData = gson.fromJson<WalletAccount>(walletAccount, WalletAccount::class.java)
                val transactionCategoryData = gson.fromJson<TransactionCategory>(transactionCategory, TransactionCategory::class.java)


                if(transactionCategoryData.TransactionCategoryType==mainContext.getString(R.string.expense)&&walletAccountData.WalletAccountID==accountID){

                    transactionData.add(
                            Transaction(
                                    dataList.transactionID!!,
                                    dataList.transactionDate!!,
                                    dataList.transactionTime!!,
                                    dataList.transactionAmount,
                                    dataList.transactionRemark!!,
                                    transactionCategoryData,
                                    walletAccountData
                            )
                    )

                }

            }
        }

        return transactionData

    }


    override fun getRecentExpenseRealm(mainContext: Context, userID: String, accountID: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

                getTransactionCategory = if(filterSelection == incomeType){
                    realm.where(TransactionCategoryRealm::class.java).equalTo(userIDRef,userID).equalTo(transactionCategoryTypeRef,incomeType).findAll()

                }else if(filterSelection == expenseType){
                    realm.where(TransactionCategoryRealm::class.java).equalTo(userIDRef,userID).equalTo(transactionCategoryTypeRef,expenseType).findAll()

                }else{
                    realm.where(TransactionCategoryRealm::class.java).equalTo(userIDRef,userID).findAll()

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


    override fun createNewTrxRealm(mainContext: Context, newTrxInput: Transaction) {

        //
        var realm: Realm? = null

        Realm.init(mainContext)

        val config = RealmConfiguration.Builder()
                .name(mainContext.getString(R.string.transactionRealm))
                .build()

        realm = Realm.getInstance(config)

        realm!!.executeTransaction {

            val creating = realm.createObject(TransactionRealm::class.java, newTrxInput.TransactionID)


            val gson = Gson()
            val convertedCategory = gson.toJson(newTrxInput.TransactionCategory)
            val convertedAccount = gson.toJson(newTrxInput.WalletAccount)


            creating.transactionDate= newTrxInput.TransactionDate
            creating.transactionTime= newTrxInput.TransactionTime
            creating.transactionAmount= newTrxInput.TransactionAmount
            creating.transactionRemark= newTrxInput.TransactionRemark
            creating.transactionCategory= convertedCategory
                    creating.walletAccount= convertedAccount

        }

        realm.close()

        //

    }


    override fun updateDetailsTrxRealm(mainContext: Context, detailsTrxInput: Transaction) {

        //
        var realm: Realm? = null

        Realm.init(mainContext)

        val config = RealmConfiguration.Builder()
                .name(mainContext.getString(R.string.transactionRealm))
                .build()

        realm = Realm.getInstance(config)


        realm!!.executeTransaction {


            val getTrx = realm.where(TransactionRealm::class.java).equalTo(mainContext.getString(R.string.TransactionID), detailsTrxInput.TransactionID).findAll()

            val gson = Gson()
            val convertedCategory = gson.toJson(detailsTrxInput.TransactionCategory)
            val convertedAccount = gson.toJson(detailsTrxInput.WalletAccount)


            getTrx.forEach{
                dataList->

                dataList.transactionDate = detailsTrxInput.TransactionDate
                dataList.transactionTime = detailsTrxInput.TransactionTime
                dataList.transactionAmount = detailsTrxInput.TransactionAmount
                dataList.transactionRemark = detailsTrxInput.TransactionRemark
                dataList.transactionCategory = convertedCategory
                dataList.walletAccount = convertedAccount

            }
        }

        realm.close()

    }

    override fun deleteDetailsTrxRealm(mainContext: Context, transactionID: String) {

        //
        var realm: Realm? = null

        Realm.init(mainContext)

        val config = RealmConfiguration.Builder()
                .name(mainContext.getString(R.string.transactionRealm))
                .build()

        realm = Realm.getInstance(config)

        realm!!.executeTransaction {

            val getTrx = realm.where(TransactionRealm::class.java).equalTo(mainContext.getString(R.string.TransactionID),transactionID).findAll()


            getTrx.forEach{
                dataList->

                dataList.deleteFromRealm()
            }

        }

        realm.close()

        //

    }


    // TrxHistorySpecificDate Fragment
    override fun getTrxForSpecificDateFilterRealm(mainContext: Context, userID: String, accountID: String): ArrayList<Transaction> {

        //
        var realm: Realm? = null

        val transactionData=ArrayList<Transaction>()


        Realm.init(mainContext)

        val config = RealmConfiguration.Builder()
                .name(mainContext.getString(R.string.transactionRealm))
                .build()

        realm = Realm.getInstance(config)


        realm!!.executeTransaction {

            val getTransaction = realm.where(TransactionRealm::class.java)
                    .sort(mainContext.getString(R.string.TransactionDate), Sort.DESCENDING,mainContext.getString(R.string.TransactionTime), Sort.DESCENDING)
                    .findAll()



            getTransaction.forEach{
                dataList->

                val gson = Gson()

                val walletAccount = dataList.walletAccount
                val transactionCategory = dataList.transactionCategory

                val walletAccountData = gson.fromJson<WalletAccount>(walletAccount, WalletAccount::class.java)
                val transactionCategoryData = gson.fromJson<TransactionCategory>(transactionCategory, TransactionCategory::class.java)



                if(walletAccountData.WalletAccountID==accountID && walletAccountData.UserUID==userID){

                    // convert to 12hour for ez display purpose
                    val notConvertedTime = dataList.transactionTime!!
                    val date12Format = SimpleDateFormat(mainContext.getString(R.string.timeFormat12))
                    val date24Format = SimpleDateFormat(mainContext.getString(R.string.timeFormat24))
                    val convertedTime = date12Format.format(date24Format.parse(notConvertedTime))

                    transactionData.add(

                            Transaction(
                                    dataList.transactionID!!,
                                    dataList.transactionDate!!,
                                    convertedTime,
                                    dataList.transactionAmount,
                                    dataList.transactionRemark!!,
                                    transactionCategoryData,
                                    walletAccountData
                            )
                    )

                }


            }

        }

        realm.close()

        return transactionData

    }



    // TrxHistoryRangeDate Fragment
    override fun getTrxForRangeDateFilterRealm(mainContext: Context, userID: String, accountID: String): ArrayList<Transaction> {

        //
        var realm: Realm? = null

        val transactionData=ArrayList<Transaction>()


        Realm.init(mainContext)

        val config = RealmConfiguration.Builder()
                .name(mainContext.getString(R.string.transactionRealm))
                .build()

        realm = Realm.getInstance(config)


        realm!!.executeTransaction {

            val getTransaction = realm.where(TransactionRealm::class.java)
                    .sort(mainContext.getString(R.string.TransactionDate), Sort.DESCENDING,mainContext.getString(R.string.TransactionTime), Sort.DESCENDING)
                    .findAll()



            getTransaction.forEach{
                dataList->

                val gson = Gson()

                val walletAccount = dataList.walletAccount
                val transactionCategory = dataList.transactionCategory

                val walletAccountData = gson.fromJson<WalletAccount>(walletAccount, WalletAccount::class.java)
                val transactionCategoryData = gson.fromJson<TransactionCategory>(transactionCategory, TransactionCategory::class.java)



                if(walletAccountData.WalletAccountID==accountID && walletAccountData.UserUID==userID){

                    // convert to 12hour for ez display purpose
                    val notConvertedTime = dataList.transactionTime!!
                    val date12Format = SimpleDateFormat(mainContext.getString(R.string.timeFormat12))
                    val date24Format = SimpleDateFormat(mainContext.getString(R.string.timeFormat24))
                    val convertedTime = date12Format.format(date24Format.parse(notConvertedTime))

                    transactionData.add(

                            Transaction(
                                    dataList.transactionID!!,
                                    dataList.transactionDate!!,
                                    convertedTime,
                                    dataList.transactionAmount,
                                    dataList.transactionRemark!!,
                                    transactionCategoryData,
                                    walletAccountData
                            )
                    )

                }


            }

        }

        realm.close()

        return transactionData

    }


}