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
import java.util.*
import android.app.Activity
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.Transaction
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlin.collections.ArrayList
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import com.github.mikephil.charting.formatter.IValueFormatter
import java.text.DecimalFormat


class DashBoardFragment : Fragment(),ViewInterface.DashBoardView {

    private lateinit var presenter: PresenterInterface.Presenter
    private val mCalendar = Calendar.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.dashBoardFragmentTitle)

        return inflater.inflate(R.layout.fragment_dash_board, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = Presenter(this)

        hideDisplayedKeyboard(view) // Hide Keyboard

        // Initial Input
        val thisMonth =mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        DBMonthTextView.text = getString(R.string.formatDisplayMonth,thisMonth)

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

    private fun displayDummyDateGraph() {

        val xAxisLabel = ArrayList<String>()
            xAxisLabel.add("Mon")
            xAxisLabel.add("Tue")
            xAxisLabel.add("Wed")
            xAxisLabel.add("Thu")
            xAxisLabel.add("Fri")
            xAxisLabel.add("Sat")



        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(0f, 30.26f))
        entries.add(BarEntry(1f, 80.88f))
        entries.add(BarEntry(2f, 60f))
        entries.add(BarEntry(3f, 50f))
        entries.add(BarEntry(4f, 70f))
        entries.add(BarEntry(5f, 60f))

        val set = BarDataSet(entries, "Total Expenses")

        set.setValueFormatter(MyValueFormatter())

        val data = BarData(set)
        data.barWidth = 0.7f // set custom bar width
        DBTrxGraph.setData(data)
        DBTrxGraph.setFitBars(true) // make the x-axis fit exactly all bars
        DBTrxGraph.invalidate() // refresh

        DBTrxGraph.setTouchEnabled(false)
        DBTrxGraph.description=null

        val xAxis = DBTrxGraph.getXAxis();

        xAxis.setValueFormatter { value, axis ->

            xAxisLabel.get(value.toInt())
        }
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
    }



    inner class MyValueFormatter : IValueFormatter {

        private val mFormat: DecimalFormat

        init {
            mFormat = DecimalFormat("###,###,##0.00") // use one decimal
        }

        override fun getFormattedValue(value: Float, entry: Entry, dataSetIndex: Int, viewPortHandler: ViewPortHandler): String {
            // write your logic here
            return "$ "+mFormat.format(value) // e.g. append a dollar-sign
        }
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


        val userProfile = presenter.getUserData(mainContext)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                presenter.setSelectedAccount(mainContext, walletAccountList[spinner.selectedItemPosition].WalletAccountName) //Save Select Account in SharedPreference for future use

                presenter.checkTransaction(mainContext, walletAccountList[spinner.selectedItemPosition].WalletAccountID, userProfile.UserUID)


                //!!!!!!! LOAD TRX LIST, BALANCE FIGURE IS HERE
                presenter.getAllIncome(mainContext, userProfile.UserUID, walletAccountList[spinner.selectedItemPosition].WalletAccountID)

                val thisMonth =mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
                presenter.getThisMonthExpense(mainContext, userProfile.UserUID, walletAccountList[spinner.selectedItemPosition].WalletAccountID, thisMonth)

                displayDummyDateGraph() // Dummy Graph Setup - Must Reload the Graph everytime Account selection change !!!!!!!!!! NO USE

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

        if(currentBalance>0){

            currentBalanceView.text = String.format(mainContext.getString(R.string.formatDisplay2DecimalMoney), currentBalance)
            currentBalanceView.setTextColor(Color.GREEN)
        }else{

            currentBalanceView.text = String.format(mainContext.getString(R.string.formatDisplay2DecimalMoney), currentBalance)
            currentBalanceView.setTextColor(Color.RED)
        }


    }

    override fun populateThisMonthExpense(mainContext: Context, thisMonthExpense: Double) {

        val thisMonthExpenseView = (mainContext as Activity).findViewById(R.id.DBMonthlyExpTextView) as TextView
        thisMonthExpenseView.text = String.format(mainContext.getString(R.string.formatDisplay2DecimalMoney), thisMonthExpense)
        thisMonthExpenseView.setTextColor(Color.RED)
    }


    override fun populateExpenseGraph(mainContext: Context, transactionList: ArrayList<Transaction>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
