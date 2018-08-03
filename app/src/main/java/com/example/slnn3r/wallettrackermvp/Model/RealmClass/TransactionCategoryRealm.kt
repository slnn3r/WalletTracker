package com.example.slnn3r.wallettrackermvp.Model.RealmClass

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass


@RealmClass
open class TransactionCategoryRealm(): RealmObject(){

    @PrimaryKey
    var transactionCategoryID:String? = null
    var transactionCategoryName:String? =null
    var transactionCategoryType:String? = null
    var transactionCategoryStatus:String? = null
    var userUID: String?=null
}