package com.example.slnn3r.wallettrackermvp.View

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.widget.Toast
import com.example.slnn3r.wallettrackermvp.R
import com.example.slnn3r.wallettrackermvp.Interface.ViewInterface
import com.example.slnn3r.wallettrackermvp.Interface.PresenterInterface

import com.example.slnn3r.wallettrackermvp.Presenter.Presenter;
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), ViewInterface.LoginView {


    private lateinit var presenter: PresenterInterface.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        presenter = Presenter(this)

        SignInButton.setOnClickListener(){

            presenter.loginGoogleRequest(this)
        }

    }

    override fun loginSuccess(mainContext: Context?) {

        val myIntent = Intent(mainContext, MenuActivity::class.java)
        mainContext?.startActivity(myIntent)
        Toast.makeText(mainContext, "Login Success",Toast.LENGTH_LONG).show()
        // Manifested - android:noHistory="true"
    }

    override fun loginFail(mainContext: Context?) {
        Log.d("sdfsadfasdfa","LOGINFAIL?!!")

            Toast.makeText(mainContext, "No Internet Connection",Toast.LENGTH_LONG).show()

    }

    override fun displayLoginOption(mainContext: Context, fragment: FragmentActivity, intent: Intent, REQUEST_CODE_SIGN_IN: Int) {

        fragment.startActivityForResult(intent, REQUEST_CODE_SIGN_IN)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

        presenter.loginGoogleExecute(this.applicationContext,requestCode,resultCode,data)
    }
}
