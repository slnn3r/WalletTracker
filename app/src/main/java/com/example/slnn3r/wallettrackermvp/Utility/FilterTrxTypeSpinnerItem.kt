package com.example.slnn3r.wallettrackermvp.Utility

class FilterTrxTypeSpinnerItem{

    fun getSpinnerItem(): ArrayList<String> {

        val spinnerItem = ArrayList<String>()
        spinnerItem.add("All Type")
        spinnerItem.add("Expense")
        spinnerItem.add("Income")

        return spinnerItem
    }


}