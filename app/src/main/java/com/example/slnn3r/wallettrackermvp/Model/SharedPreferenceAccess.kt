package com.example.slnn3r.wallettrackermvp.Model

import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.example.slnn3r.wallettrackermvp.Interface.ModelInterface
import com.example.slnn3r.wallettrackermvp.Interface.PresenterInterface
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.UserProfile
import com.example.slnn3r.wallettrackermvp.R
import com.google.gson.Gson

class SharedPreferenceAccess: ModelInterface.SharedPreference{

    private lateinit var presenter: PresenterInterface.Presenter

    override fun getUserData(mainContext: Context): UserProfile {

        val editor = mainContext.getSharedPreferences(mainContext.getString(R.string.userProfileKey), AppCompatActivity.MODE_PRIVATE)

        // user GSON convert to object
        val gson = Gson()
        val json = editor.getString(mainContext.getString(R.string.userProfileKey), "")

        val userProfile = gson.fromJson<UserProfile>(json, UserProfile::class.java)

        return userProfile

    }


}