package com.example.slnn3r.wallettrackermvp.View.Fragment.WalletAccount


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.slnn3r.wallettrackermvp.Adapter.WalletAccountAdapter
import com.example.slnn3r.wallettrackermvp.Model.UserProfile

import com.example.slnn3r.wallettrackermvp.R
import com.google.gson.Gson
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

        walletAccountRecyclerView.layoutManager = LinearLayoutManager(context)
        //.layoutManager = LinearLayoutManager(context)


        val editor = context!!.getSharedPreferences("UserProfile", Context.MODE_PRIVATE)

        // user GSON convert to object
        val gson = Gson()
        val json = editor.getString("UserProfile", "")

        val userProfile = gson.fromJson<UserProfile>(json, UserProfile::class.java!!)

        walletAccountRecyclerView.adapter = WalletAccountAdapter(userProfile)
        /////

        button2.setOnClickListener(){

            val navController = view.findNavController()
            navController.navigate(R.id.action_viewWalletAccountFragment_to_createWalletAccountFragment)

        }



    }



}
