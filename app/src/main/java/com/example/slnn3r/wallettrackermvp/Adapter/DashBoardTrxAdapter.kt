package com.example.slnn3r.wallettrackermvp.Adapter


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.slnn3r.wallettrackermvp.Model.UserProfile
import com.example.slnn3r.wallettrackermvp.R
import com.example.slnn3r.wallettrackermvp.View.Activity.MenuActivity
import kotlinx.android.synthetic.main.transaction_list_row.view.*
import kotlin.coroutines.experimental.coroutineContext
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import com.example.slnn3r.wallettrackermvp.Model.Transaction


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

        holder.view.DBAccNameTextView.text = video.TransactionDate + " (" +video.TransactionTime+")"
        holder.view.DBBalTextView.text = video.TransactionCategoryID
        holder.view.DBTrxCategoryTextView.text = "$ " + video.TransactionAmount

    }

}



class DashBoardViewHolder(val view: View): RecyclerView.ViewHolder(view){

    init{
        view.setOnClickListener{

            val navController = view.findNavController()
            navController.navigate(R.id.action_dashBoardFragment_to_detailsTrxFragment)

            val context = view.context
            (context as MenuActivity).setupNavigationMode()
        }
    }

}