package com.example.slnn3r.wallettrackermvp.Utility

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.support.v7.app.AlertDialog
import com.example.slnn3r.wallettrackermvp.R

class AlertDialog {

    fun confirmationDialog(mainContext: Context, title: String, message: String, icon: Drawable, dialogOnClickListener: DialogInterface.OnClickListener): Dialog {

        val confirmationDialog = AlertDialog.Builder(mainContext)
        confirmationDialog.setTitle(title)
                .setIcon(icon)
                .setMessage(message)
                .setPositiveButton(mainContext.getString(R.string.dialogPositive), dialogOnClickListener)
                .setNegativeButton(mainContext.getString(R.string.dialogNegative)
                ) { dialogBox, _ ->
                    dialogBox.dismiss()
                }
        return confirmationDialog.create()
    }
}