package com.example.slnn3r.wallettrackermvp.Utility

class SelectionTrxTypeSpinnerItem{

    fun getSpinnerItem(): ArrayList<String> {

        val spinnerItem = ArrayList<String>()
        spinnerItem.add("Expense")
        spinnerItem.add("Income")

        return spinnerItem
    }


}