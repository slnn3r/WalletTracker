package com.example.slnn3r.wallettrackermvp.Utility

import com.example.slnn3r.wallettrackermvp.Model.Transaction

class DummyDataTrxListItem{

    fun getListItem(): ArrayList<Transaction> {



        val spinnerItem = ArrayList<Transaction>()


        spinnerItem.add(Transaction("1","Food and Drink","12/12/2018","Good",5))
        spinnerItem.add(Transaction("1","Food and Drink","12/12/2018","Good",10))
        spinnerItem.add(Transaction("1","Entertainment","12/12/2018","Good",20))
        spinnerItem.add(Transaction("1","Food and Drink","12/12/2018","Good",3))
        spinnerItem.add(Transaction("1","Food and Drink","12/12/2018","Good",10))
        spinnerItem.add(Transaction("1","Food and Drink","12/12/2018","Good",20))
        spinnerItem.add(Transaction("1","Food and Drink","12/12/2018","Good",20))
        spinnerItem.add(Transaction("1","Food and Drink","12/12/2018","Good",10))
        spinnerItem.add(Transaction("1","Transportation","12/12/2018","Good",100))
        spinnerItem.add(Transaction("1","Entertainment","12/12/2018","Good",500))

        return spinnerItem
    }


}