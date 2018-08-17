package com.example.slnn3r.wallettrackermvp.Model.FirebaseClass


class TransactionFirebase(var TransactionID: String = "",
                          var TransactionDate: String = "",
                          var TransactionTime: String = "",
                          var TransactionAmount: Double = 0.0,
                          var TransactionRemark: String = "",
                          var TransactionCategory: TransactionCategoryFirebase = TransactionCategoryFirebase(),
                          var WalletAccount: WalletAccountFirebase = WalletAccountFirebase())