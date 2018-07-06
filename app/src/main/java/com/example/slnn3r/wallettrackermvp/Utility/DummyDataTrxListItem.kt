package com.example.slnn3r.wallettrackermvp.Utility

import com.example.slnn3r.wallettrackermvp.Model.Transaction

class DummyDataTrxListItem{

    fun getListItem(): ArrayList<Transaction> {

        val spinnerItem = ArrayList<Transaction>()


        spinnerItem.add(Transaction("1","12/12/2018","10:30PM",5.0,"Good","Food and Drink","01"))
        spinnerItem.add(Transaction("1","12/12/2018","10:10PM",10.0,"Good","Food and Drink","01"))
        spinnerItem.add(Transaction("1","12/12/2018","9:30PM",20.0,"Good","Entertainment","01"))
        spinnerItem.add(Transaction("1","12/12/2018","8:30PM",3.0,"Good","Food and Drink","01"))
        spinnerItem.add(Transaction("1","12/12/2018","8:30PM",10.0,"Good","Food and Drink","01"))
        spinnerItem.add(Transaction("1","12/12/2018","7:30PM",20.0,"Good","Food and Drink","01"))
        spinnerItem.add(Transaction("1","12/12/2018","6:30PM",20.0,"Good","Food and Drink","01"))
        spinnerItem.add(Transaction("1","12/12/2018","11:30PM",10.0,"Good","Food and Drink","01"))
        spinnerItem.add(Transaction("1","12/12/2018","10:30AM",100.0,"Good","Transportation","01"))
        spinnerItem.add(Transaction("1","12/12/2018","10:30AM",500.0,"Good","Entertainment","01"))

        return spinnerItem
    }


}