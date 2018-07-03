package com.example.slnn3r.wallettrackermvp.View.Fragment


import android.content.Context.MODE_PRIVATE
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.ArrayAdapter
import androidx.navigation.findNavController
import com.example.slnn3r.wallettrackermvp.Adapter.DashBoardTrxAdapter
import com.example.slnn3r.wallettrackermvp.Model.UserProfile

import com.example.slnn3r.wallettrackermvp.R
import com.example.slnn3r.wallettrackermvp.View.Activity.MenuActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_dash_board.*
import android.widget.Toast
import com.firebase.ui.auth.AuthUI.getApplicationContext




class DashBoardFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        (activity as? AppCompatActivity)?.supportActionBar?.title = "Dashboard"


        return inflater.inflate(R.layout.fragment_dash_board, container, false)
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ///// Dummy RecycleView
        dashBoardRecyclerView.layoutManager = LinearLayoutManager(context)


        val editor = context!!.getSharedPreferences("UserProfile", MODE_PRIVATE)

        // user GSON convert to object
        val gson = Gson()
        val json = editor.getString("UserProfile", "")

        val userProfile = gson.fromJson<UserProfile>(json, UserProfile::class.java!!)

        dashBoardRecyclerView.adapter = DashBoardTrxAdapter(userProfile)
        /////

        ///// Dummy Spinner
        val categories = ArrayList<String>()
        categories.add("Personal")
        categories.add("Business Services")
        categories.add("Education")

        // Creating adapter for spinner
        val dataAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, categories)

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        dashBoardSpinner.adapter = dataAdapter

        /////

        fabIncome.setOnClickListener(){

            // Testing Purpose
            val navController = view.findNavController()
            navController.navigate(R.id.action_dashBoardFragment_to_addNewTrx)
            (activity as MenuActivity).setupNavigationMode()

            Toast.makeText(context,"Add",Toast.LENGTH_LONG).show()

        }


        fabExpense.setOnClickListener(){

            // Testing Purpose
            val navController = view.findNavController()
            navController.navigate(R.id.action_dashBoardFragment_to_addNewTrx)
            (activity as MenuActivity).setupNavigationMode()

            Toast.makeText(context,"Minus",Toast.LENGTH_LONG).show()


        }


    }

}
