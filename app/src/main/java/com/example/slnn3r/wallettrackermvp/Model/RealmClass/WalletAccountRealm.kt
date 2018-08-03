package com.example.slnn3r.wallettrackermvp.Model.RealmClass

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass


@RealmClass
open class WalletAccountRealm(): RealmObject(){

    @PrimaryKey
    var walletAccountID:String? = null
    var walletAccountName: String? = null
    var walletAccountInitialBalance:Double = 0.0
    var userUID:String? = null
    var walletAccountStatus: String?= null
}