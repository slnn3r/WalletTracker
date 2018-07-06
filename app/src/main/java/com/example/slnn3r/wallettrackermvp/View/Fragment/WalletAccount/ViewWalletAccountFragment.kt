package com.example.slnn3r.wallettrackermvp.View.Fragment.WalletAccount


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.slnn3r.wallettrackermvp.Adapter.WalletAccountAdapter

import com.example.slnn3r.wallettrackermvp.R
import com.example.slnn3r.wallettrackermvp.Utility.DummyDataAccListItem
import kotlinx.android.synthetic.main.fragment_view_wallet_account.*



class ViewWalletAccountFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        (activity as? AppCompatActivity)?.supportActionBar?.title = "Wallet Account List"


        return inflater.inflate(R.layout.fragment_view_wallet_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        ///// Dummy RecycleView

        VWARecyclerView.layoutManager = LinearLayoutManager(context)
        VWARecyclerView.adapter = WalletAccountAdapter(DummyDataAccListItem().getListItem())
        /////

        WVAGoToCWA.setOnClickListener(){

            val navController = view.findNavController()
            navController.navigate(R.id.action_viewWalletAccountFragment_to_createWalletAccountFragment)

        }



    }



}
