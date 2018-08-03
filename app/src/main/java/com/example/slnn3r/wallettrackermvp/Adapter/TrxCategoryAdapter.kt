package com.example.slnn3r.wallettrackermvp.Adapter

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.TransactionCategory
import com.example.slnn3r.wallettrackermvp.R
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.trx_category_list_row.view.*

class TrxCategoryAdapter(private val transactionCategoryList: ArrayList<TransactionCategory>): RecyclerView.Adapter<TrxCategoryViewHolder>(){

    // numberOfItems
    override  fun getItemCount(): Int{
        return transactionCategoryList.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrxCategoryViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.trx_category_list_row, parent, false)

        return TrxCategoryViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: TrxCategoryViewHolder, position: Int) {

        /*
        onBindHolder called several times as recycler news needs a view unless new one. So each time you set visilibity in child views, other views states are also changes.
        Whenever you scroll up and down, these views are getting re-drawed with wrong visibility options. SO USE setIsRecyclable(false)
         */
        holder.setIsRecyclable(false)

        val transactionCategoryData = transactionCategoryList[position]

        holder.view.VTCAccNameTextView.text = transactionCategoryData.TransactionCategoryName

        if(transactionCategoryData.TransactionCategoryType == holder.view.context.getString(R.string.expense)){
            holder.view.VTCImageView.background = holder.view.VTCImageView.context.resources.getDrawable(R.drawable.fui_idp_button_background_email)

            // Picasso get LAGGY and affect Navigation Drawer Animation when does not crop it, as it will load full size image
            Picasso.get().load(R.drawable.expense_icon).resize(400,400).centerCrop().into(holder.view.VTCImageView)
            //holder.view.DBTrxImageView.setImageDrawable(holder.view.DBTrxImageView.context.resources.getDrawable(R.drawable.expense_icon))

        }else{

            // Picasso get LAGGY and affect Navigation Drawer Animation when does not crop it, as it will load full size image
            Picasso.get().load(R.drawable.income_icon).resize(400,400).centerCrop().into(holder.view.VTCImageView)
            //holder.view.DBTrxImageView.setImageDrawable(holder.view.DBTrxImageView.context.resources.getDrawable(R.drawable.income_icon))
        }

        holder.passData = transactionCategoryData
    }
}


class TrxCategoryViewHolder(val view: View, var passData: TransactionCategory?= null): RecyclerView.ViewHolder(view){

    init{
        view.setOnClickListener{

            if(passData!=null){

                val gson = Gson()
                val walletAccountData = TransactionCategory(passData!!.TransactionCategoryID,passData!!.TransactionCategoryName,passData!!.TransactionCategoryType, passData!!.TransactionCategoryStatus, passData!!.UserUID)
                val json = gson.toJson(walletAccountData)

                val navController = view.findNavController()

                val bundle = Bundle()
                bundle.putString(view.context.getString(R.string.trxCategoryPassArgKey), json)
                navController.navigate(R.id.action_viewTrxCategoryFragment_to_detailsTrxCategoryFragment, bundle)
            }
        }
    }
}