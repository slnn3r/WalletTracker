package com.example.slnn3r.wallettrackermvp.Adapter

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.Transaction
import com.example.slnn3r.wallettrackermvp.R
import com.example.slnn3r.wallettrackermvp.View.Activity.MenuActivity
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.transaction_list_row.view.*

var loadGraph = false
var loadBal = false
var loadExp = false

class DashBoardTrxAdapter(private val transactionList: ArrayList<Transaction>) : RecyclerView.Adapter<DashBoardViewHolder>() {

    // numberOfItems
    override fun getItemCount(): Int {
        return transactionList.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashBoardViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.transaction_list_row, parent, false)

        return DashBoardViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: DashBoardViewHolder, position: Int) {

        /*
        onBindHolder called several times as recycler news needs a view unless new one. So each time you set visilibity in child views, other views states are also changes.
        Whenever you scroll up and down, these views are getting re-drawed with wrong visibility options. SO USE setIsRecyclable(false)
         */
        holder.setIsRecyclable(false)

        val transactionData = transactionList[position]

        if (transactionData.TransactionDate == holder.view.context.getString(R.string.noResult)) {

            Picasso.get().load(R.drawable.not_found).into(holder.view.DBTrxImageView)
            holder.view.DBAccNameTextView.text = transactionData.TransactionDate
            holder.view.DBBalTextView.text = holder.view.context.getString(R.string.noDataInDatabase)
            holder.view.DBTrxCategoryTextView.text = holder.view.context.getString(R.string.tabToAddTrx)

        } else {

            holder.view.DBAccNameTextView.text = holder.view.DBTrxImageView.context.getString(R.string.formatDisplayDateTime, transactionData.TransactionDate, transactionData.TransactionTime)
            holder.view.DBBalTextView.text = transactionData.TransactionCategory.TransactionCategoryName
            holder.view.DBTrxCategoryTextView.text = String.format(holder.view.DBTrxImageView.context.getString(R.string.formatDisplay2DecimalMoney), transactionData.TransactionAmount)

            if (transactionData.TransactionCategory.TransactionCategoryType == holder.view.context.getString(R.string.expense)) {
                holder.view.DBTrxImageView.background = holder.view.DBTrxImageView.context.resources.getDrawable(R.drawable.fui_idp_button_background_email)

                // Picasso get LAGGY and affect Navigation Drawer Animation when does not crop it, as it will load full size image
                Picasso.get().load(R.drawable.expense_icon).resize(200, 200).centerCrop().into(holder.view.DBTrxImageView)
                //holder.view.DBTrxImageView.setImageDrawable(holder.view.DBTrxImageView.context.resources.getDrawable(R.drawable.expense_icon))

            } else {

                // Picasso get LAGGY and affect Navigation Drawer Animation when does not crop it, as it will load full size image
                Picasso.get().load(R.drawable.income_icon).resize(200, 200).centerCrop().into(holder.view.DBTrxImageView)
                //holder.view.DBTrxImageView.setImageDrawable(holder.view.DBTrxImageView.context.resources.getDrawable(R.drawable.income_icon))
            }
        }

        holder.passData = transactionData
    }
}


class DashBoardViewHolder(val view: View, var passData: Transaction? = null) : RecyclerView.ViewHolder(view) {

    init {

        view.setOnClickListener {

            if(!loadGraph || !loadExp || !loadBal){
                return@setOnClickListener
            }

                val context = view.context
                val noResult = view.context.getString(R.string.noResult)

                if (view.DBAccNameTextView.text == noResult) {

                    Toast.makeText(context, noResult, Toast.LENGTH_SHORT).show()

                } else {

                    val gson = Gson()
                    val transactionData = Transaction(
                            passData!!.TransactionID,
                            passData!!.TransactionDate,
                            passData!!.TransactionTime,
                            passData!!.TransactionAmount,
                            passData!!.TransactionRemark,
                            passData!!.TransactionCategory,
                            passData!!.WalletAccount
                    )

                    val json = gson.toJson(transactionData)

                    val bundle = Bundle()
                    bundle.putString(view.context.getString(R.string.transactionPassArgKey), json)

                    val navController = view.findNavController()
                    navController.navigate(R.id.action_dashBoardFragment_to_detailsTrxFragment, bundle)

                    (context as MenuActivity).setupNavigationMode()
                }

        }
    }
}