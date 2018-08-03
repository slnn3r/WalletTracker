package com.example.slnn3r.wallettrackermvp.View.Activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.slnn3r.wallettrackermvp.R
import com.example.slnn3r.wallettrackermvp.Interface.ViewInterface
import com.example.slnn3r.wallettrackermvp.Interface.PresenterInterface
import com.example.slnn3r.wallettrackermvp.Presenter.Presenter
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(), ViewInterface.LoginView {

    private lateinit var presenter: PresenterInterface.Presenter
    private lateinit var mGoogleApiClient: GoogleApiClient
    private val REQUEST_CODE_SIGN_IN= 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        presenter = Presenter(this)

        SignInButton.setOnClickListener{

            displayLoginAccount()
        }
    }

    private fun displayLoginAccount(){

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.GoogleSignInOptionKey))
                .requestEmail()
                .build()

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this) {

                    Toast.makeText(this,getString(R.string.GAPCCError),Toast.LENGTH_LONG).show()
                }
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        val intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)

        mGoogleApiClient.stopAutoManage(this)
        mGoogleApiClient.disconnect()

        this.startActivityForResult(intent, REQUEST_CODE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        presenter.loginGoogleExecute(this,requestCode,resultCode,data)
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


    override fun displayLoginLoading(mainContext:Context):ProgressDialog {

        return ProgressDialog.show(mainContext, getString(R.string.loadingTitle), getString(R.string.loadingMessage))
    }

    override fun dismissLoginLoading(loginLoading:ProgressDialog) {

        loginLoading.dismiss()
    }
}

