package com.example.slnn3r.wallettrackermvp.Model.RealmClass

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass


@RealmClass
open class WalletAccountRealm(): RealmObject(){

    @PrimaryKey
    var WalletAccountID:String? = null

    var WalletAccountName: String? = null
    var WalletAccountInitialBalance:Double = 0.0
    var UserID:String? = null


}