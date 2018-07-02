
package com.example.slnn3r.wallettrackermvp.View.Activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.widget.Toast
import com.example.slnn3r.wallettrackermvp.Interface.PresenterInterface
import com.example.slnn3r.wallettrackermvp.Interface.ViewInterface
import com.example.slnn3r.wallettrackermvp.Presenter.Presenter
import com.example.slnn3r.wallettrackermvp.R

import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.app_bar_menu.*
import com.google.gson.Gson
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.slnn3r.wallettrackermvp.Adapter.dashBoardTransactionAdapter
import com.example.slnn3r.wallettrackermvp.Model.UserProfile
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.content_menu.*
import android.widget.ArrayAdapter



class MenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,  ViewInterface.MenuView {

    private lateinit var presenter: PresenterInterface.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        setSupportActionBar(toolbar)


        // display User info to Drawer
        displayUserInfo()

        fab.setOnClickListener { view ->

        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        ///// Dummy RecycleView
        dashBoardRecyclerView.layoutManager = LinearLayoutManager(this)


        val editor = getSharedPreferences("UserProfile", MODE_PRIVATE)

        // user GSON convert to object
        val gson = Gson()
        val json = editor.getString("UserProfile", "")

        val userProfile = gson.fromJson<UserProfile>(json, UserProfile::class.java!!)

        dashBoardRecyclerView.adapter = dashBoardTransactionAdapter(userProfile)
        /////

        ///// Dummy Spinner
        val categories = ArrayList<String>()
        categories.add("Personal")
        categories.add("Business Services")
        categories.add("Education")

        // Creating adapter for spinner
        val dataAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        dashBoardSpinner.adapter = dataAdapter

        /////

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else{
            super.onBackPressed()

        }
    }




    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        presenter = Presenter(this)


        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_WalletAccount -> {

                val myIntent = Intent(this, WalletAccountActivity::class.java)
                this.startActivity(myIntent)

            }
            R.id.nav_TrxCategory -> {

            }
            R.id.nav_TrxHistory -> {

            }

            R.id.nav_SignOut -> {
                presenter.logoutGoogleExecute(this)

            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    override fun logoutSuccess(mainContext: Context, successLogoutMessage: String) {

        // remove SharedPreference data
        val editor = mainContext.getSharedPreferences("UserProfile", MODE_PRIVATE).edit()
        editor.remove("UserProfile").commit()
        editor.remove("UserProfile").apply()

        val myIntent = Intent(mainContext, LoginActivity::class.java)
        mainContext?.startActivity(myIntent)
        Toast.makeText(mainContext, successLogoutMessage, Toast.LENGTH_LONG).show()
        (mainContext as Activity).finish()

    }

    override fun logoutFail(mainContext: Context, errorMessage: String) {
        Toast.makeText(mainContext, errorMessage, Toast.LENGTH_LONG).show()
    }


    fun displayUserInfo() {

        // Get SharedPreference data
        val editor = getSharedPreferences("UserProfile", MODE_PRIVATE)

        // user GSON convert to object
        val gson = Gson()
        val json = editor.getString("UserProfile", "")

        val userProfile = gson.fromJson<UserProfile>(json, UserProfile::class.java!!)


        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val headerView = navigationView.getHeaderView(0)

        val navUserName = headerView.findViewById(R.id.userProfileName) as TextView
        val navUserEmail = headerView.findViewById(R.id.userProfileEmail) as TextView
        val navUserPicture = headerView.findViewById(R.id.userProfileImageView) as ImageView

        navUserName.text = userProfile.userName
        navUserEmail.text = userProfile.userEmail
        Picasso.get().load(userProfile.userPicURL).into(navUserPicture)

    }



}
