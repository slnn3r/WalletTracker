package com.example.slnn3r.wallettrackermvp.Utility

import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.TransactionCategory

class DefaultDataCategoryListItem{

    fun getListItem(): ArrayList<TransactionCategory> {



        val spinnerItem = ArrayList<TransactionCategory>()


        spinnerItem.add(TransactionCategory("", "Food and Drink", "Expense", "Default", ""))
        spinnerItem.add(TransactionCategory("", "Entertainment", "Expense", "Default", ""))
        spinnerItem.add(TransactionCategory("", "Transport", "Expense", "Default", ""))
        spinnerItem.add(TransactionCategory("", "Bills", "Expense", "Default", ""))
        spinnerItem.add(TransactionCategory("", "Other", "Expense", "Default", ""))
        spinnerItem.add(TransactionCategory("", "Salary", "Income", "Default", ""))


        return spinnerItem
    }


}