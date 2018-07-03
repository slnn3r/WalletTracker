package com.example.slnn3r.wallettrackermvp.Adapter


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.slnn3r.wallettrackermvp.Model.UserProfile
import com.example.slnn3r.wallettrackermvp.R
import kotlinx.android.synthetic.main.transaction_list_row.view.*

class DashBoardTrxAdapter(val homeFeed: UserProfile): RecyclerView.Adapter<DashBoardViewHolder>(){


    // numberOfItems
    override  fun getItemCount(): Int{
        return homeFeed.userName.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashBoardViewHolder {

        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.transaction_list_row, parent, false)


        return DashBoardViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: DashBoardViewHolder, position: Int) {

        val video = homeFeed.userName.get(position)

        holder?.view?.adapterDateView?.text = video.toString();
        holder?.view?.adapterCategoryView?.text = video.toString();
        holder?.view?.adapterAmountView?.text = video.toString();


    }

}



class DashBoardViewHolder(val view: View): RecyclerView.ViewHolder(view){

    init{
        view.setOnClickListener{
            println("click")



        }
    }

}