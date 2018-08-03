package com.example.slnn3r.wallettrackermvp.Model.RealmClass


import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass


@RealmClass
open class TransactionRealm(): RealmObject(){

    @PrimaryKey
    var transactionID:String? = null
    var transactionDate:String? =null
    var transactionTime:String? = null
    var transactionAmount:Double = 0.0
    var transactionRemark: String?=null
    var transactionCategory: String?= null
    var walletAccount:String?=null
}