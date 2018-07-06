package com.example.slnn3r.wallettrackermvp.Utility

import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.WalletAccount

class DummyDataAccListItem{

    fun getListItem(): ArrayList<WalletAccount> {



        val spinnerItem = ArrayList<WalletAccount>()


        spinnerItem.add(WalletAccount("1", "Personal", 1000.0, "01"))
        spinnerItem.add(WalletAccount("1", "Business Service", 2000.0, "01"))
        spinnerItem.add(WalletAccount("1", "Education", 5000.0, "01"))




        return spinnerItem
    }


}