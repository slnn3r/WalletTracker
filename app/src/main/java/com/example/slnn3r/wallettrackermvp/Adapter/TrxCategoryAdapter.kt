package com.example.slnn3r.wallettrackermvp.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.TransactionCategory
import com.example.slnn3r.wallettrackermvp.R
import kotlinx.android.synthetic.main.wallet_account_list_row.view.*

class TrxCategoryAdapter(val homeFeed: ArrayList<TransactionCategory>): RecyclerView.Adapter<TrxCategoryViewHolder>(){


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

        val video = homeFeed.get(position)

        holder.view.VWAAccNameTextView.text = video.TransactionCategoryName


    }

}

class TrxCategoryViewHolder(val view: View): RecyclerView.ViewHolder(view){

    init{
        view.setOnClickListener{

            val navController = view.findNavController()
            navController.navigate(R.id.action_viewTrxCategoryFragment_to_detailsTrxCategoryFragment)


        }
    }

}