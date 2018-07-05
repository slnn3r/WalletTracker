package com.example.slnn3r.wallettrackermvp.View.Fragment


import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.ArrayAdapter
import androidx.navigation.findNavController
import com.example.slnn3r.wallettrackermvp.Adapter.DashBoardTrxAdapter

import com.example.slnn3r.wallettrackermvp.R
import com.example.slnn3r.wallettrackermvp.View.Activity.MenuActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_dash_board.*
import android.widget.Toast
import com.example.slnn3r.wallettrackermvp.Interface.ViewInterface
import com.example.slnn3r.wallettrackermvp.Utility.DummyDataTrxListItem


class DashBoardFragment : Fragment(),ViewInterface.DashBoardView {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        (activity as? AppCompatActivity)?.supportActionBar?.title = "Dashboard"


        return inflater.inflate(R.layout.fragment_dash_board, container, false)
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ///// Dummy RecycleView
        DBTrxRecyclerView.layoutManager = LinearLayoutManager(context)


        DBTrxRecyclerView.adapter = DashBoardTrxAdapter(DummyDataTrxListItem().getListItem())
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

        DBAccountSpinner.adapter = dataAdapter

        /////

        DBIncomeFab.setOnClickListener(){

            // Testing Purpose
            val navController = view.findNavController()
            navController.navigate(R.id.action_dashBoardFragment_to_addNewTrx)
            (activity as MenuActivity).setupNavigationMode()

            Toast.makeText(context,"Add",Toast.LENGTH_SHORT).show()

        }


        DBExpenseFab.setOnClickListener(){

            // Testing Purpose
            val navController = view.findNavController()
            navController.navigate(R.id.action_dashBoardFragment_to_addNewTrx)
            (activity as MenuActivity).setupNavigationMode()

            Toast.makeText(context,"Minus",Toast.LENGTH_SHORT).show()


        }




    }


    //// In Progress

    override fun populateWalletAccountSpinner() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
