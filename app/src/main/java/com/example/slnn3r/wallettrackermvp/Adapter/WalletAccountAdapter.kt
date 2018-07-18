package com.example.slnn3r.wallettrackermvp.Adapter

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.WalletAccount
import com.example.slnn3r.wallettrackermvp.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.wallet_account_list_row.view.*


class WalletAccountAdapter(private val homeFeed: ArrayList<WalletAccount>): RecyclerView.Adapter<WalletAccountViewHolder>(){


    // numberOfItems
    override  fun getItemCount(): Int{
        return homeFeed.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletAccountViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.wallet_account_list_row, parent, false)

        return WalletAccountViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: WalletAccountViewHolder, position: Int) {

        val video = homeFeed[position]

        holder.view.VWAAccNameTextView.text = video.WalletAccountName

        holder.passData = video

    }

}

class WalletAccountViewHolder(val view: View, var passData: WalletAccount?= null): RecyclerView.ViewHolder(view){

    init{
        view.setOnClickListener{

            if(passData!=null){

                val gson = Gson()
                val walletAccountData = WalletAccount(passData!!.WalletAccountID,passData!!.WalletAccountName,passData!!.WalletAccountInitialBalance,passData!!.UserUID,passData!!.WalletAccountStatus)
                val json = gson.toJson(walletAccountData)


                val navController = view.findNavController()

                val bundle = Bundle()
                bundle.putString(view.context.getString(R.string.walletAccountPassArgKey), json)
                navController.navigate(R.id.action_viewWalletAccountFragment_to_detailsWalletAccountFragment, bundle)

            }



        }
    }

}