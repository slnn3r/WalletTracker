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
import kotlinx.android.synthetic.main.fragment_view_trx_category.view.*
import kotlinx.android.synthetic.main.wallet_account_list_row.view.*

class TrxCategoryAdapter(private val homeFeed: ArrayList<TransactionCategory>): RecyclerView.Adapter<TrxCategoryViewHolder>(){


    // numberOfItems
    override  fun getItemCount(): Int{
        return homeFeed.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrxCategoryViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.trx_category_list_row, parent, false)


        return TrxCategoryViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: TrxCategoryViewHolder, position: Int) {

        val spinner = holder.view.VTCTrxTypeSpinner
        val video = homeFeed[position]

        holder.view.VWAAccNameTextView.text = video.TransactionCategoryName

        holder.passData = video


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