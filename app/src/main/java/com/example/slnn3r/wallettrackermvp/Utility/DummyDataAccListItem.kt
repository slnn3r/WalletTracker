package com.example.slnn3r.wallettrackermvp.Utility

import com.example.slnn3r.wallettrackermvp.Model.WalletAccount

class DummyDataAccListItem{

    fun getListItem(): ArrayList<WalletAccount> {



        val spinnerItem = ArrayList<WalletAccount>()


        spinnerItem.add(WalletAccount("1","Personal",1000))
        spinnerItem.add(WalletAccount("1","Business Service",2000))
        spinnerItem.add(WalletAccount("1","Education",5000))




        return spinnerItem
    }


}