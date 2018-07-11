package com.example.slnn3r.wallettrackermvp.View.Activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.widget.Toast
import com.example.slnn3r.wallettrackermvp.R
import com.example.slnn3r.wallettrackermvp.Interface.ViewInterface
import com.example.slnn3r.wallettrackermvp.Interface.PresenterInterface
import com.example.slnn3r.wallettrackermvp.Presenter.Presenter
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

    override fun loginSuccess(mainContext: Context?, successLoginMessage: String) {

        val myIntent = Intent(mainContext, MenuActivity::class.java)
        mainContext?.startActivity(myIntent)
        Toast.makeText(mainContext, successLoginMessage,Toast.LENGTH_LONG).show()

        (mainContext as Activity).finish()
    }

    override fun loginFail(mainContext: Context?, errorMessage: String) {

        Toast.makeText(mainContext, errorMessage,Toast.LENGTH_LONG).show()
    }

    override fun displayLoginAccount(mainContext: Context, fragment: FragmentActivity, intent: Intent, REQUEST_CODE_SIGN_IN: Int) {

        fragment.startActivityForResult(intent, REQUEST_CODE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        presenter.loginGoogleExecute(this,requestCode,resultCode,data)
    }


    // if not need rx then call them during onActivityResult and Login Success/fail
    override fun displayLoginLoading(mainContext:Context):ProgressDialog {
        val loginLoading: ProgressDialog = ProgressDialog.show(mainContext, getString(R.string.loadingTitle), getString(R.string.loadingMessage))
        return loginLoading
    }

    override fun dismissLoginLoading(loginLoading:ProgressDialog) {
        loginLoading.dismiss()
    }



}

