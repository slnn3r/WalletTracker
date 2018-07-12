package com.example.slnn3r.wallettrackermvp.Model.RealmClass

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass


@RealmClass
open class TransactionCategoryRealm(): RealmObject(){


    @PrimaryKey
    var TransactionCategoryID:String? = null
    var TransactionCategoryName:String? =null
    var TransactionCategoryType:String? = null
    var TransactionCategoryStatus:String? = null
    var UserUID: String?=null


}