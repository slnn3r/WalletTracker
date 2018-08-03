package com.example.slnn3r.wallettrackermvp.View.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.slnn3r.wallettrackermvp.Interface.ViewInterface
import com.example.slnn3r.wallettrackermvp.Interface.PresenterInterface
import com.example.slnn3r.wallettrackermvp.Presenter.Presenter
import com.example.slnn3r.wallettrackermvp.R

class MainActivity : AppCompatActivity(),ViewInterface.MainView {

    private lateinit var presenter: PresenterInterface.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = Presenter(this)
        presenter.checkLogin()
    }

    override fun navigateToLoginScreen() {

        val myIntent = Intent(this, LoginActivity::class.java)
        this.startActivity(myIntent)
        // Manifested - android:noHistory="true"
    }

    override fun navigateToMenuScreen(userName: String?) {

        val myIntent = Intent(this, MenuActivity::class.java)
        val welcomeMessage = getString(R.string.mainActivityWelcomeMessage,userName)

        this.startActivity(myIntent)
        Toast.makeText(this,welcomeMessage,Toast.LENGTH_SHORT).show()
        // Manifested - android:noHistory="true"
    }

    override fun checkLoginFail(errorMessage:String) {

        val errorContent = getString(R.string.mainActivityCheckLoginFail,errorMessage)
        Toast.makeText(this,errorContent,Toast.LENGTH_LONG).show()
    }
}
