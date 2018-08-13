
package com.example.slnn3r.wallettrackermvp.View.Activity

import android.animation.ValueAnimator
import android.app.Activity
import android.app.job.JobScheduler
import android.content.Context
import android.content.DialogInterface
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
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.UserProfile
import com.example.slnn3r.wallettrackermvp.Utility.AlertDialog
import com.google.firebase.database.FirebaseDatabase


class MenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,  ViewInterface.MenuView {


    private lateinit var presenter: PresenterInterface.Presenter

    private var isNavigated:String? ="" // Set Initial Navigation Status to false
    private var trxScreenSelection=""

    private val initialScreen:Int = R.id.dashBoardFragment

    private var doubleBackToExitPressedOnce = false

    private lateinit var toggle:ActionBarDrawerToggle

    private val alertDialog: AlertDialog = AlertDialog()

    private lateinit var userProfile: UserProfile


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        setSupportActionBar(toolbar)

        // Get SharedPreference data
        presenter = Presenter(this)
        userProfile = presenter.getUserData(this)

        // display User info to Drawer
        displayUserInfo()
        displaySyncDateTime()
        presenter.syncDataPeriodically(this, userProfile.UserUID)

        nav_view.setNavigationItemSelectedListener(this)

        setupNavigationUpButton()
    }



    private fun displayUserInfo() {

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val headerView = navigationView.getHeaderView(0)

        val navUserName = headerView.findViewById(R.id.userProfileName) as TextView
        val navUserEmail = headerView.findViewById(R.id.userProfileEmail) as TextView
        val navUserPicture = headerView.findViewById(R.id.userProfileImageView) as ImageView

        navUserName.text = userProfile.UserName
        navUserEmail.text = userProfile.UserEmail
        Picasso.get().load(userProfile.UserPicURL).into(navUserPicture)


    }

    private fun displaySyncDateTime(){
        val editor = getSharedPreferences(userProfile.UserUID, AppCompatActivity.MODE_PRIVATE)

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val menu = navigationView.menu
        val navSyncData = menu.findItem(R.id.navDrawer_title2)
        navSyncData.title = getString(R.string.navDrawer_title2, editor.getString(userProfile.UserUID,""))
    }


    private fun setupNavigationUpButton() {

        // Display the Navigation Drawer Button (Default Generated Function)
        toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        // Setup Custom Navigation Drawer Button Listener
        toolbar.setNavigationOnClickListener {

            // Check if Screen is navigated or not
            if (isNavigated==getString(R.string.navigatedIndicate)) {
                setupNavigationFlow()
            } else if(isNavigated==getString(R.string.trxHistoryNavigatedIndicate)){

                if(trxScreenSelection==getString(R.string.trxHistoryNavSpecificKey)){

                    val navController = (this as Activity).findNavController(R.id.trxHistoryFragmentNavMenu)
                    navController.navigate(R.id.action_detailsTrxFragment_to_trxHistorySpecificDateFragment)

                }else{

                    val navController = (this as Activity).findNavController(R.id.trxHistoryFragmentNavMenu)
                    navController.navigate(R.id.action_detailsTrxFragment_to_trxHistoryRangeDateFragment)
                }

                setupNavigationMode()
            }else if(isNavigated==getString(R.string.disableNavigatedIndicate)){
                // Do Nothing for the ToolBar at Dialogfragment Display
            }else {
                displaySyncDateTime()
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

    private fun setupDrawerMode() {
        isNavigated = ""
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        animateIcon(1,0,800) // with Animation no need to deal with any Icon Change stuff

        //supportActionBar!!.setDisplayHomeAsUpEnabled(false) // Remove the ActionBar
        //setupNavigationUpButton() // Recreate new Action Bar with Drawer Icon
    }

    fun setupToDisable(){
        isNavigated = getString(R.string.disableNavigatedIndicate)
    }

    fun setupNavigationMode() {
        isNavigated = getString(R.string.navigatedIndicate)
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        animateIcon(0,1,800) // with Animation no need to deal with any Icon Change stuff

        //supportActionBar!!.setDisplayHomeAsUpEnabled(true) // Make the button become Back Icon
    }

    fun trxHistoryBack(trxScreen:String){
        trxScreenSelection=trxScreen
        isNavigated = getString(R.string.trxHistoryNavigatedIndicate)
    }

    private fun animateIcon(start:Int, end:Int, duration:Int){

        val anim= ValueAnimator.ofFloat(start.toFloat(),end.toFloat())

        anim.addUpdateListener { animation ->
            val slideOffset = animation?.animatedValue as Float
            toggle.onDrawerSlide(drawer_layout, slideOffset)
        }

        anim.interpolator = DecelerateInterpolator()
        anim.duration = duration.toLong()
        anim.start()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else if (isNavigated==getString(R.string.navigatedIndicate)) {
            setupNavigationFlow()
        } else if(isNavigated==getString(R.string.trxHistoryNavigatedIndicate)){ // for TrxHistory Navigation

            if(trxScreenSelection==getString(R.string.trxHistoryNavSpecificKey)){

                val navController = (this as Activity).findNavController(R.id.trxHistoryFragmentNavMenu)
                navController.navigate(R.id.action_detailsTrxFragment_to_trxHistorySpecificDateFragment)

            }else{

                val navController = (this as Activity).findNavController(R.id.trxHistoryFragmentNavMenu)
                navController.navigate(R.id.action_detailsTrxFragment_to_trxHistoryRangeDateFragment)
            }

            setupNavigationMode()
        }else{

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

    override fun onSupportNavigateUp() = findNavController(R.id.navMenu).navigateUp()

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        presenter = Presenter(this)

        Handler().postDelayed({ //For Avoid Lagging Animation

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

                    alertDialog.confirmationDialog(this,"Sign Out","Please Make Sure to Complete the Sync Data Before Sign Out. All Pending Sync Data Process will Remove.",resources.getDrawable(android.R.drawable.ic_dialog_alert),
                            DialogInterface.OnClickListener { dialogBox, which ->

                                // Cancel Job Schedule
                                val scheduler: JobScheduler = this.applicationContext.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
                                scheduler.cancel(123)

                                presenter.logoutGoogleExecute(this)

                            }).show()
                }
                R.id.navDrawer_SyncData -> {
                    val userProfile = presenter.getUserData(this)
                    presenter.syncDataManually(this, userProfile.UserUID)


                }

            }

        }, 200)

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }



    override fun startPeriodicSyncSuccess(mainContext: Context) {

        Toast.makeText(mainContext, "Auto Sync Run Every 15mins from Now", Toast.LENGTH_LONG).show()
    }

    override fun startPeriodicSyncFail(mainContext: Context, errorMessage: String) {

        Toast.makeText(mainContext, "Auto Sync Failed: "+errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun syncDataSuccess(mainContext: Context) {

        //Handler().postDelayed({

            Toast.makeText(mainContext, "Sync is Executed on Background", Toast.LENGTH_SHORT).show()

        //}, 200)
    }

    override fun syncDataFail(mainContext: Context, errorMessage: String) {

        Toast.makeText(mainContext, "Sync Data have Failed: "+errorMessage, Toast.LENGTH_LONG).show()
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
}