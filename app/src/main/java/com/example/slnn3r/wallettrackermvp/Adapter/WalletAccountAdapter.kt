package com.example.slnn3r.wallettrackermvp.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.slnn3r.wallettrackermvp.Model.UserProfile
import com.example.slnn3r.wallettrackermvp.R
import kotlinx.android.synthetic.main.transaction_list_row.view.*


class WalletAccountAdapter(val homeFeed: UserProfile): RecyclerView.Adapter<WalletAccountViewHolder>(){


    // numberOfItems
    override  fun getItemCount(): Int{
        return homeFeed.userName.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletAccountViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.wallet_account_list_row, parent, false)


        return WalletAccountViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: WalletAccountViewHolder, position: Int) {

        val video = homeFeed.userName.get(position)

        holder.view.VWAAccNameTextView.text = video.toString()
        holder.view.VWABalTextView.text = video.toString()


    }

}

class WalletAccountViewHolder(val view: View): RecyclerView.ViewHolder(view){

    init{
        view.setOnClickListener{

            val navController = view.findNavController()
            navController.navigate(R.id.action_viewWalletAccountFragment_to_detailsWalletAccountFragment)


        }
    }

}