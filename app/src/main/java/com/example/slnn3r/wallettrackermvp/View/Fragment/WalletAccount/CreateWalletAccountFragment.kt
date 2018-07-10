package com.example.slnn3r.wallettrackermvp.View.Fragment.WalletAccount


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.slnn3r.wallettrackermvp.Interface.PresenterInterface
import com.example.slnn3r.wallettrackermvp.Interface.ViewInterface
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.UserProfile
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.WalletAccount
import com.example.slnn3r.wallettrackermvp.Presenter.Presenter

import com.example.slnn3r.wallettrackermvp.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_create_wallet_account.*
import java.util.*


class CreateWalletAccountFragment : Fragment(), ViewInterface.CreateWalletAccountView {



    private lateinit var presenter: PresenterInterface.Presenter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        (activity as? AppCompatActivity)?.supportActionBar?.title = "Create Wallet Account"

        return inflater.inflate(R.layout.fragment_create_wallet_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = Presenter(this)


        CWACreateSubmit.setOnClickListener(){



            val uniqueID = UUID.randomUUID().toString()

            ////
            ////
            val editor = context!!.getSharedPreferences("UserProfile", AppCompatActivity.MODE_PRIVATE)

            // user GSON convert to object
            val gson = Gson()
            val json = editor.getString("UserProfile", "")

            val userProfile = gson.fromJson<UserProfile>(json, UserProfile::class.java)

            val userID = userProfile.UserUID
            ////
            ////


            var walletAccountInput= WalletAccount(
                    uniqueID,
                    CWAAccNameInput.text.toString(),
                    CWAAccBalanceInput.text.toString().toDouble(),
                    userID,
                    "New"
            )


            presenter.createWalletAccount(context!!, walletAccountInput)


        }



    }


    override fun createWalletAccountSuccess(mainContext: Context) {

        Toast.makeText(mainContext,"Create Wallet Account Success",Toast.LENGTH_LONG).show()

        (mainContext as Activity).onBackPressed()


    }

    override fun createWalletAccountFail(mainContext: Context, errorMessage: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
