package com.example.slnn3r.wallettrackermvp.View.Fragment


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import androidx.navigation.findNavController
import com.example.slnn3r.wallettrackermvp.Adapter.DashBoardTrxAdapter

import com.example.slnn3r.wallettrackermvp.R
import com.example.slnn3r.wallettrackermvp.View.Activity.MenuActivity
import kotlinx.android.synthetic.main.fragment_dash_board.*
import com.example.slnn3r.wallettrackermvp.Interface.PresenterInterface
import com.example.slnn3r.wallettrackermvp.Interface.ViewInterface
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.WalletAccount
import com.example.slnn3r.wallettrackermvp.Presenter.Presenter
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.util.*
import android.app.Activity
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.Transaction
import kotlin.collections.ArrayList


class DashBoardFragment : Fragment(),ViewInterface.DashBoardView {


    private lateinit var presenter: PresenterInterface.Presenter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.dashBoardFragmentTitle)

        return inflater.inflate(R.layout.fragment_dash_board, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = Presenter(this)

        hideDisplayedKeyboard(view) // Hide Keyboard
        displayDummyGraph() // Dummy Graph Setup

        // Get SharedPreference data
        val userProfile = presenter.getUserData(context!!)
        val userID = userProfile.UserUID

        // Display Account Selection to Spinner
        presenter.checkWalletAccount(context!!, userID )


        // Listener Setter
        DBIncomeFab.setOnClickListener{
            onIncomeButtonClick(view, userID)
        }

        DBExpenseFab.setOnClickListener{
            onExpenseButtonClick(view, userID)
        }
    }


    // Function Implementation
    private fun hideDisplayedKeyboard(view: View) {

        // To Hide KeyBoard
        val inputManager = view
                .context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        val binder = view.windowToken
        inputManager.hideSoftInputFromWindow(binder,
                InputMethodManager.HIDE_NOT_ALWAYS)
    }

    private fun onIncomeButtonClick(view: View, userID: String) {

        val walletAccountData = presenter.getAccountData(context!!, userID)

        val navController = view.findNavController()

        val bundle = Bundle()
        bundle.putString(getString(R.string.trxTypePassArgKey), getString(R.string.income))
        bundle.putString(getString(R.string.walletAccountPassArgKey), walletAccountData[DBAccountSpinner.selectedItemPosition].WalletAccountName)

        navController.navigate(R.id.action_dashBoardFragment_to_addNewTrx, bundle)

        (activity as MenuActivity).setupNavigationMode()
    }

    private fun onExpenseButtonClick(view: View, userID: String) {

        val walletAccountData = presenter.getAccountData(context!!, userID)

        val navController = view.findNavController()

        val bundle = Bundle()
        bundle.putString(getString(R.string.trxTypePassArgKey), getString(R.string.expense))
        bundle.putString(getString(R.string.walletAccountPassArgKey), walletAccountData[DBAccountSpinner.selectedItemPosition].WalletAccountName)


        navController.navigate(R.id.action_dashBoardFragment_to_addNewTrx,bundle)

        (activity as MenuActivity).setupNavigationMode()

    }

    private fun displayDummyGraph() {
        // generate Dates
        val calendar = Calendar.getInstance()

        val d1 = calendar.time

        calendar.add(Calendar.DATE, 1)
        val d2 = calendar.time

        calendar.add(Calendar.DATE, 1)
        val d3 = calendar.time

        calendar.add(Calendar.DATE, 1)
        val d4 = calendar.time

        calendar.add(Calendar.DATE, 1)
        val d5 = calendar.time


        val graph = DBTrxGraph as GraphView

        // you can directly pass Date objects to DataPoint-Constructor
        // this will convert the Date to double via Date#getTime()
        val series = LineGraphSeries<DataPoint>(arrayOf<DataPoint>(DataPoint(d1, 34.0), DataPoint(d2, 23.0), DataPoint(d3, 5.0), DataPoint(d4, 13.0),DataPoint(d5, 8.0)))

        graph.addSeries(series)

        // set date label formatter
        graph.gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(activity)
        graph.gridLabelRenderer.numHorizontalLabels = 5

        // set manual x bounds to have nice steps
        graph.viewport.setMinX(d1.time.toDouble())
        graph.viewport.setMaxX(d5.time.toDouble())
        graph.viewport.isXAxisBoundsManual = true

        // as we use dates as labels, the human rounding to nice readable numbers
        // is not necessary
        graph.gridLabelRenderer.setHumanRounding(false)

    }


    //// Presenter Callback
    override fun firstTimeSetup(mainContext: Context) {

        // Get SharedPreference data
        presenter = Presenter(this)
        val userProfile = presenter.getUserData(mainContext)
        val userID = userProfile.UserUID

        presenter.firstTimeDatabaseSetup(mainContext, userID)

    }


    override fun firstTimeSetupSuccess(mainContext: Context, walletAccount: WalletAccount) {

        presenter = Presenter(this)
        presenter.checkWalletAccount(mainContext, walletAccount.UserUID )
    }

    override fun firstTimeSetupFail(mainContext: Context, errorMessage: String) {

        Toast.makeText(mainContext,errorMessage,Toast.LENGTH_LONG).show()

    }



    override fun populateWalletAccountSpinner(mainContext: Context, walletAccountList: ArrayList<WalletAccount>) {

        presenter = Presenter(this)

        val categories = ArrayList<String>()

        walletAccountList.forEach {
            data->
            categories.add(data.WalletAccountName)
        }

        // Creating adapter for spinner
        val dataAdapter = ArrayAdapter(mainContext, android.R.layout.simple_spinner_item, categories)

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        val spinner = (mainContext as Activity).findViewById(R.id.DBAccountSpinner) as Spinner
        spinner.adapter = dataAdapter


        // Get SharedPreference saved Selection and Set to Spinner Selection
        val go = presenter.getSelectedAccount(mainContext)

        val spinnerPosition = dataAdapter.getPosition(go)
        spinner.setSelection(spinnerPosition)


        val UserProfile = presenter.getUserData(context!!)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                presenter.setSelectedAccount(mainContext, walletAccountList[spinner.selectedItemPosition].WalletAccountName) //Save Select Account in SharedPreference for future use
                presenter.checkTransaction(mainContext, walletAccountList[spinner.selectedItemPosition].WalletAccountID, UserProfile.UserUID)

                // display Balance
                presenter.getAllIncome(context!!, UserProfile.UserUID, walletAccountList[spinner.selectedItemPosition].WalletAccountID)
                presenter.getThisMonthExpense(context!!, UserProfile.UserUID, walletAccountList[spinner.selectedItemPosition].WalletAccountID)

            }
        }



    }

    override fun populateWalletAccountSpinnerFail(mainContext: Context, errorMessage: String) {

        Toast.makeText(mainContext,errorMessage,Toast.LENGTH_LONG).show()

    }



    override fun populateTransactionRecycleView(mainContext: Context, transactionList: ArrayList<Transaction>) {

        val dBTrxRecyclerView = (mainContext as Activity).findViewById(R.id.DBTrxRecyclerView) as RecyclerView
        
        dBTrxRecyclerView.layoutManager = LinearLayoutManager(mainContext)
        dBTrxRecyclerView.adapter = DashBoardTrxAdapter(transactionList)


    }

    override fun populateTransactionRecycleViewFail(mainContext: Context, errorMessage: String) {

        Toast.makeText(mainContext,errorMessage,Toast.LENGTH_LONG).show()

    }


    override fun populateCurrentBalance(mainContext: Context, currentBalance: Double) {

        val currentBalanceView = (mainContext as Activity).findViewById(R.id.DBCurrentBalTextView) as TextView
        currentBalanceView.text = "$ "+currentBalance.toString()
        currentBalanceView.setTextColor(Color.GREEN)
    }

    override fun populateThisMonthExpense(mainContext: Context, thisMonthExpense: Double) {
        val thisMonthExpenseView = (mainContext as Activity).findViewById(R.id.DBMonthlyExpTextView) as TextView
        thisMonthExpenseView.text = "$ "+thisMonthExpense.toString()
        thisMonthExpenseView.setTextColor(Color.RED)

    }




}
