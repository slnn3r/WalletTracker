package com.example.slnn3r.wallettrackermvp.Utility

class DummyDataCategorySpinner{

    fun getSpinnerItem(): ArrayList<String> {

        val spinnerItem = ArrayList<String>()
        spinnerItem.add("Food and Drink")
        spinnerItem.add("Transportation")
        spinnerItem.add("Entertainment")

        return spinnerItem
    }


}