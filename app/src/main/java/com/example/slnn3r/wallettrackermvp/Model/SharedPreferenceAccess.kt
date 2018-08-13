package com.example.slnn3r.wallettrackermvp.Model

import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.example.slnn3r.wallettrackermvp.Interface.ModelInterface
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.UserProfile
import com.example.slnn3r.wallettrackermvp.R
import com.google.gson.Gson

class SharedPreferenceAccess: ModelInterface.SharedPreference{


    override fun getUserData(mainContext: Context): UserProfile {

        val editor = mainContext.getSharedPreferences(mainContext.getString(R.string.userProfileKey), AppCompatActivity.MODE_PRIVATE)

        // user GSON convert to object
        val gson = Gson()
        val json = editor.getString(mainContext.getString(R.string.userProfileKey), "")

        return gson.fromJson<UserProfile>(json, UserProfile::class.java)
    }

    override fun saveSelectedAccount(mainContext: Context, selection: String) {
        val editor = mainContext.getSharedPreferences(mainContext.getString(R.string.selectedAccount), AppCompatActivity.MODE_PRIVATE)!!.edit()
        editor.putString(mainContext.getString(R.string.selectedAccount), selection)
        editor.apply()
        editor.commit()
    }

    override fun getSelectedAccountData(mainContext: Context): String {
        val editor = mainContext.getSharedPreferences(mainContext.getString(R.string.selectedAccount), AppCompatActivity.MODE_PRIVATE)

        return editor.getString(mainContext.getString(R.string.selectedAccount),"")
    }

    override fun saveUserData(mainContext: Context, userData: String){
        val editor = mainContext.getSharedPreferences(mainContext.getString(R.string.userProfileKey), AppCompatActivity.MODE_PRIVATE)!!.edit()
        editor.putString(mainContext.getString(R.string.userProfileKey), userData)
        editor.apply()
        editor.commit()


        val sync = mainContext.getSharedPreferences("SyncDateTime", AppCompatActivity.MODE_PRIVATE)!!.edit()
        sync.putString("SyncDateTime", "Never Sync Before")
        sync.apply()
        sync.commit()
    }

    override fun removeUserData(mainContext: Context) {

        val editor = mainContext.getSharedPreferences(mainContext.getString(R.string.userProfileKey), Context.MODE_PRIVATE).edit()
        editor.remove(mainContext.getString(R.string.userProfileKey)).apply()
        editor.remove(mainContext.getString(R.string.selectedAccount)).apply()
    }
}