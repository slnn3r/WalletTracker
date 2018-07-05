package com.example.slnn3r.wallettrackermvp.Utility

import com.example.slnn3r.wallettrackermvp.Model.TransactionCategory

class DummyDataCategoryListItem{

    fun getListItem(): ArrayList<TransactionCategory> {



        val spinnerItem = ArrayList<TransactionCategory>()


        spinnerItem.add(TransactionCategory("1","Food and Drink","Expense","Default"))
        spinnerItem.add(TransactionCategory("1","Entertainment","Expense","Default"))
        spinnerItem.add(TransactionCategory("1","Transportion","Expense","Default"))
        spinnerItem.add(TransactionCategory("1","Dance Dance","Expense","Default"))
        spinnerItem.add(TransactionCategory("1","Yammmmm Seng","Expense","Default"))
        spinnerItem.add(TransactionCategory("1","Game","Expense","Default"))
        spinnerItem.add(TransactionCategory("1","Internet Service","Expense","Default"))
        spinnerItem.add(TransactionCategory("1","Computer Gadget","Expense","Default"))
        spinnerItem.add(TransactionCategory("1","Insurance","Expense","Default"))
        spinnerItem.add(TransactionCategory("1","House","Expense","Default"))




        return spinnerItem
    }


}