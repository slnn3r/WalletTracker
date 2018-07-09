package com.example.slnn3r.wallettrackermvp.Model.RealmClass


import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.TransactionCategory
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass


@RealmClass
open class TransactionRealm(): RealmObject(){



    @PrimaryKey
    var TransactionID:String? = null
    var TransactionDate:String? =null
    var TransactionTime:String? = null
    var TransactionAmount:Double = 0.0
    var TransactionRemark: String?=null

    var TransactionCategory: String?= null

    var WalletAccountID:String?=null

}