package com.example.slnn3r.wallettrackermvp.Model.ObjectClass

class Transaction(val TransactionID:String,
                  val TransactionDate:String,
                  val TransactionTime:String,
                  val TransactionAmount:Double,
                  val TransactionRemark: String,
                  val TransactionCategory: TransactionCategory, // to display Info TrxType info in dashboard easily
                  val WalletAccount:String)
