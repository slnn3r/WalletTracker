package com.example.slnn3r.wallettrackermvp.View.Activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.slnn3r.wallettrackermvp.Interface.ViewInterface
import com.example.slnn3r.wallettrackermvp.Interface.PresenterInterface

import com.example.slnn3r.wallettrackermvp.Presenter.Presenter;


import com.example.slnn3r.wallettrackermvp.R

class MainActivity : AppCompatActivity(),ViewInterface.MainView {

    private lateinit var presenter: PresenterInterface.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = Presenter(this)
        presenter.checkLogin(this)

    }


    override fun navigateToLoginScreen(mainContext: Context) {

        val myIntent = Intent(mainContext, LoginActivity::class.java)
        mainContext.startActivity(myIntent)
        // Manifested - android:noHistory="true"
    }

    override fun navigateToMenuScreen(mainContext: Context) {

        val myIntent = Intent(mainContext, MenuActivity::class.java)
        mainContext.startActivity(myIntent)
        // Manifested - android:noHistory="true"
    }



}
