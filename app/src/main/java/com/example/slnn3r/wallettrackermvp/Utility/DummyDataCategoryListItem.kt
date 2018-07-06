package com.example.slnn3r.wallettrackermvp.Utility

import com.example.slnn3r.wallettrackermvp.Model.TransactionCategory

class DummyDataCategoryListItem{

    fun getListItem(): ArrayList<TransactionCategory> {



        val spinnerItem = ArrayList<TransactionCategory>()


        spinnerItem.add(TransactionCategory("1","Food and Drink","Expense","Default","01"))
        spinnerItem.add(TransactionCategory("1","Entertainment","Expense","Default","01"))
        spinnerItem.add(TransactionCategory("1","Transportion","Expense","Default","01"))
        spinnerItem.add(TransactionCategory("1","Dance Dance","Income","Default","01"))
        spinnerItem.add(TransactionCategory("1","Salary","Income","Default","01"))
        spinnerItem.add(TransactionCategory("1","Game","Expense","Default","01"))
        spinnerItem.add(TransactionCategory("1","Internet Service","Expense","Default","01"))
        spinnerItem.add(TransactionCategory("1","Computer Gadget","Expense","Default","01"))
        spinnerItem.add(TransactionCategory("1","Insurance","Expense","Default","01"))
        spinnerItem.add(TransactionCategory("1","House","Expense","Default","01"))




        return spinnerItem
    }


}