package com.example.slnn3r.wallettrackermvp.Adapter

import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.slnn3r.wallettrackermvp.Model.UserProfile
import com.example.slnn3r.wallettrackermvp.R
import kotlinx.android.synthetic.main.content_menu.*
import kotlinx.android.synthetic.main.transaction_list_row.view.*

class dashBoardTransactionAdapter(val homeFeed: UserProfile): RecyclerView.Adapter<CustomViewHolder>(){


    // numberOfItems
    override  fun getItemCount(): Int{
        return homeFeed.userName.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {

        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.transaction_list_row, parent, false)


        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        val video = homeFeed.userName.get(position)

        holder?.view?.adapterDateView?.text = video.toString();
        holder?.view?.adapterCategoryView?.text = video.toString();
        holder?.view?.adapterAmountView?.text = video.toString();


    }

}

class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view){

    init{
        view.setOnClickListener{
            println("click")



        }
    }

}