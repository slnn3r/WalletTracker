
package com.example.slnn3r.wallettrackermvp.View.Activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.example.slnn3r.wallettrackermvp.Interface.PresenterInterface
import com.example.slnn3r.wallettrackermvp.Interface.ViewInterface
import com.example.slnn3r.wallettrackermvp.Presenter.Presenter
import com.example.slnn3r.wallettrackermvp.R

import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.app_bar_menu.*
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController

import kotlinx.android.synthetic.main.fragment_trx_history.*


class MenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,  ViewInterface.MenuView {


    private lateinit var presenter: PresenterInterface.Presenter

    private var isNavigated:Boolean =false // Set Initial Navigation Status to false
    private val initialScreen:Int = R.id.dashBoardFragment

    private var doubleBackToExitPressedOnce = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        setSupportActionBar(toolbar)

        // display User info to Drawer
        displayUserInfo()

        nav_view.setNavigationItemSelectedListener(this)

        setupNavigationUpButton()

    }


    private fun setupNavigationUpButton() {

        // Display the Navigation Drawer Button (Default Generated Function)
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        toolbar.setNavigationIcon(R.drawable.ic_drawer_icon)


        // Setup Custom Navigation Drawer Button Listener
        toolbar.setNavigationOnClickListener {

            // Check if Screen is navigated or not
            if (isNavigated) { //
                setupNavigationFlow()
            } else {
                drawer_layout.openDrawer(GravityCompat.START)
            }
        }

    }

    private fun setupNavigationFlow() {

        onSupportNavigateUp() // Override the Navigation Up Button

        val currentScreen = findNavController(R.id.navMenu).currentDestination.id

        if (currentScreen == initialScreen) { // if Current screen is initial screen (Dashboard), switch Navigation Up button back to Drawer function
            setupDrawerMode()
        }

    }

    override fun onSupportNavigateUp() = findNavController(R.id.navMenu).navigateUp()


    fun setupNavigationMode() {
        isNavigated = true
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material)

    }

    private fun setupDrawerMode() {
        isNavigated = false
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        toolbar.setNavigationIcon(R.drawable.ic_drawer_icon)

    }


    ////


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else if (isNavigated) {
            setupNavigationFlow()

        } else{

            val currentScreen = findNavController(R.id.navMenu).currentDestination.id

            if(currentScreen==initialScreen){
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed()
                    return
                }

                this.doubleBackToExitPressedOnce = true
                Toast.makeText(this, getString(R.string.exitAppConfirmation), Toast.LENGTH_SHORT).show()

                Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
            }else{
                super.onBackPressed()

            }


        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        presenter = Presenter(this)

        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.navDrawer_WalletAccount -> {

                setupNavigationMode()
                Navigation.findNavController(this, R.id.navMenu).navigate(R.id.action_dashBoardFragment_to_viewWalletAccountFragment)

            }
            R.id.navDrawer_TrxCategory -> {

                setupNavigationMode()
                Navigation.findNavController(this, R.id.navMenu).navigate(R.id.action_dashBoardFragment_to_viewTrxCategoryFragment)
            }
            R.id.navDrawer_TrxHistory -> {

                setupNavigationMode()
                Navigation.findNavController(this, R.id.navMenu).navigate(R.id.action_dashBoardFragment_to_trxHistoryFragment)

            }

            R.id.navDrawer_SignOut -> {
                presenter.logoutGoogleExecute(this)

            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    override fun logoutSuccess(mainContext: Context, successLogoutMessage: String) {

        val myIntent = Intent(mainContext, LoginActivity::class.java)
        mainContext.startActivity(myIntent)
        Toast.makeText(mainContext, successLogoutMessage, Toast.LENGTH_LONG).show()
        (mainContext as Activity).finish()

    }

    override fun logoutFail(mainContext: Context, errorMessage: String) {
        Toast.makeText(mainContext, errorMessage, Toast.LENGTH_LONG).show()
    }


    private fun displayUserInfo() {

        // Get SharedPreference data
        presenter = Presenter(this)
        val userProfile = presenter.getUserData(this)


        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val headerView = navigationView.getHeaderView(0)

        val navUserName = headerView.findViewById(R.id.userProfileName) as TextView
        val navUserEmail = headerView.findViewById(R.id.userProfileEmail) as TextView
        val navUserPicture = headerView.findViewById(R.id.userProfileImageView) as ImageView

        navUserName.text = userProfile.UserName
        navUserEmail.text = userProfile.UserEmail
        Picasso.get().load(userProfile.UserPicURL).into(navUserPicture)

    }






}
