package com.example.slnn3r.wallettrackermvp.View.Fragment.TrxCategory


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.slnn3r.wallettrackermvp.Adapter.TrxCategoryAdapter
import com.example.slnn3r.wallettrackermvp.Interface.PresenterInterface
import com.example.slnn3r.wallettrackermvp.Interface.ViewInterface
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.TransactionCategory
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.UserProfile
import com.example.slnn3r.wallettrackermvp.Presenter.Presenter

import com.example.slnn3r.wallettrackermvp.R
import com.example.slnn3r.wallettrackermvp.Utility.DefaultDataCategoryListItem
import com.example.slnn3r.wallettrackermvp.Utility.FilterTrxTypeSpinnerItem
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_view_trx_category.*


class ViewTrxCategoryFragment : Fragment(), ViewInterface.TrxCategoryView {


    private lateinit var presenter: PresenterInterface.Presenter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.viewTrxCategoryFragmentTitle)

        return inflater.inflate(R.layout.fragment_view_trx_category, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = Presenter(this)

        VTCGoToCTC.setOnClickListener(){
            val navController = view.findNavController()
            navController.navigate(R.id.action_viewTrxCategoryFragment_to_createTrxCategoryFragment)

        }


        // Get SharedPreference data
        presenter = Presenter(this)
        val userProfile = presenter.getUserData(context!!)
        val userID = userProfile.UserUID

        presenter.checkTransactionCategory(context!!, userID)

    }


    override fun populateTrxCategoryRecycleView(mainContext: Context, trxCategoryList: ArrayList<TransactionCategory>) {

        val VTCRecyclerView = (mainContext as Activity).findViewById(R.id.VTCRecyclerView) as RecyclerView

        VTCRecyclerView.layoutManager = LinearLayoutManager(mainContext)

        VTCRecyclerView.adapter = TrxCategoryAdapter(trxCategoryList)

    }


    override fun populateTrxCategoryRecycleViewFail(mainContext: Context, errorMessage: String) {

        Toast.makeText(mainContext,errorMessage,Toast.LENGTH_LONG).show()
    }


}
