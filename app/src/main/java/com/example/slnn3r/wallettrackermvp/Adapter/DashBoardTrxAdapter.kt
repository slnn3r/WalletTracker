package com.example.slnn3r.wallettrackermvp.Adapter


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.slnn3r.wallettrackermvp.R
import com.example.slnn3r.wallettrackermvp.View.Activity.MenuActivity
import kotlinx.android.synthetic.main.transaction_list_row.view.*
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.Transaction
import com.squareup.picasso.Picasso


class DashBoardTrxAdapter(val homeFeed: ArrayList<Transaction>): RecyclerView.Adapter<DashBoardViewHolder>(){


    // numberOfItems
    override  fun getItemCount(): Int{
        return homeFeed.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashBoardViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.transaction_list_row, parent, false)


        return DashBoardViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: DashBoardViewHolder, position: Int) {

        val video = homeFeed.get(position)

        if(video.TransactionDate.equals(holder.view.context.getString(R.string.noResult))){


            Picasso.get().load(R.drawable.not_found).into(holder.view.DBTrxImageView)
            holder.view.DBAccNameTextView.text = video.TransactionDate
            holder.view.DBBalTextView.text = holder.view.context.getString(R.string.noDataInDatabase)
            holder.view.DBTrxCategoryTextView.text = holder.view.context.getString(R.string.tabToAddTrx)

        }else{

            holder.view.DBAccNameTextView.text = video.TransactionDate + " (" +video.TransactionTime+")"
            holder.view.DBBalTextView.text = video.TransactionCategory.TransactionCategoryName
            holder.view.DBTrxCategoryTextView.text = "$ " + video.TransactionAmount

            if(video.TransactionCategory.TransactionCategoryType.equals(holder.view.context.getString(R.string.expense))){
                Picasso.get().load(R.drawable.expense_icon).into(holder.view.DBTrxImageView)
            }else{
                Picasso.get().load(R.drawable.income_icon).into(holder.view.DBTrxImageView)

            }

        }



    }

}



class DashBoardViewHolder(val view: View): RecyclerView.ViewHolder(view){

    init{
        view.setOnClickListener{

            val context = view.context
            val noResult = view.context.getString(R.string.noResult)

            if(view.DBAccNameTextView.text.equals(noResult)){

                Toast.makeText(context, noResult, Toast.LENGTH_SHORT).show()

            }else{

                val navController = view.findNavController()
                navController.navigate(R.id.action_dashBoardFragment_to_detailsTrxFragment)

                (context as MenuActivity).setupNavigationMode()

            }
        }
    }

}