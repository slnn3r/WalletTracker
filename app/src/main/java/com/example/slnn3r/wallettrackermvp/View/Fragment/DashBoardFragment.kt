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
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context.JOB_SCHEDULER_SERVICE
import android.graphics.Color
import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.slnn3r.wallettrackermvp.JobService.theJobService
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.Transaction
import com.example.slnn3r.wallettrackermvp.Utility.CustomMarkerView
import kotlin.collections.ArrayList
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.data.LineDataSet


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


        // Get SharedPreference data
        val userProfile = presenter.getUserData(context!!)
        val userID = userProfile.UserUID

        // Display Account Selection to Spinner
        presenter.checkWalletAccount(context!!, userID )

        setupUI(userID)


        // Listener Setter
        DBIncomeFab.setOnClickListener{
            /*val scheduler: JobScheduler = context!!.applicationContext.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
            scheduler.cancel(123)
            Log.e("","Job Cancelled")*/

            onIncomeButtonClick(view, userID)
        }

        DBExpenseFab.setOnClickListener{

            // JobScheduler test
            /*val component=ComponentName(context!!, theJobService::class.java)
            val info = JobInfo.Builder(123, component)
                    .setRequiresCharging(true)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                    .setPersisted(true)
                    .build()
            val scheduler: JobScheduler = context!!.applicationContext.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
            val resultCode = scheduler.schedule(info)

            if(resultCode== JobScheduler.RESULT_SUCCESS){
                Log.e("","Job Schedule")
            }else{
                Log.e("","Job Schedule Failed")

            }*/

            onExpenseButtonClick(view, userID)
        }
    }


    // Function Implementation
    private fun setupUI(userID: String){
        DBTrxGraph.setNoDataText(getString(R.string.DBGraphLoading))

        // Initial Input
        val thisMonth =mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        DBMonthTextView.text = getString(R.string.formatDisplayMonth,thisMonth)



    }

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

    private fun displayDummyDateGraph(entries:ArrayList<Entry>, xAxisLabel:ArrayList<String>) {

        val dataSet = LineDataSet(entries, null)

        dataSet.setDrawFilled(true)
        dataSet.valueFormatter = MyValueFormatter()
        dataSet.setColors(Color.LTGRAY)
        dataSet.setDrawFilled(true)
        //dataset.mode = LineDataSet.Mode.CUBIC_BEZIER

        val data = LineData(dataSet)

        val xAxis = DBTrxGraph.xAxis
        xAxis.isGranularityEnabled = true
        xAxis.valueFormatter = MyXAxisValueFormatter(xAxisLabel)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)

        //DBTrxGraph.description= null

        DBTrxGraph.description.text = getString(R.string.DBGraphInfo)
        DBTrxGraph.description.textAlign = Paint.Align.CENTER
        DBTrxGraph.description.setPosition(DBTrxGraph.pivotX,25f)

        DBTrxGraph.data = data
        //DBTrxGraph.setTouchEnabled(false)
        //DBTrxGraph.isScaleYEnabled = false
        //DBTrxGraph.zoom(4f, 0f, 0f, 0f)
        DBTrxGraph.legend.isEnabled = false

        val mv = CustomMarkerView(context!!, R.layout.marker_view)
        DBTrxGraph.markerView = mv

        DBTrxGraph.notifyDataSetChanged() // this line solve weird auto resize when refresh graph(becuz of being call from spinner listener change)
        DBTrxGraph.invalidate() // refresh

    }

    // Format the Y Axis Value to 2Decimal value + add Dollar Sign
    inner class MyValueFormatter : IValueFormatter {

        //private val mFormat: DecimalFormat = DecimalFormat("###,###,##0.00")

        override fun getFormattedValue(value: Float, entry: Entry, dataSetIndex: Int, viewPortHandler: ViewPortHandler): String {
            // write your logic here
            //return "$ "+mFormat.format(value) // e.g. append a dollar-sign
            return "" // Override the Implementation Display Nothing
        }
    }

    // Format the X Axis Value to Display Desired String Value
    inner class MyXAxisValueFormatter(private val mValues: ArrayList<String>) : IAxisValueFormatter {

        /** this is only needed if numbers are returned, else return 0  */
        val decimalDigits: Int
            get() = 0

        override fun getFormattedValue(value: Float, axis: AxisBase): String {
            // "value" represents the position of the label on the axis (x or y)
            return mValues[value.toInt()]
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

            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                presenter.setSelectedAccount(mainContext, walletAccountList[spinner.selectedItemPosition].WalletAccountName) //Save Select Account in SharedPreference for future use

                presenter.checkTransaction(mainContext, walletAccountList[spinner.selectedItemPosition].WalletAccountID, userProfile.UserUID)


                //!!!!!!! LOAD TRX LIST, BALANCE FIGURE IS HERE
                presenter.getAllIncome(mainContext, userProfile.UserUID, walletAccountList[spinner.selectedItemPosition].WalletAccountID)

                val thisMonth =mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
                presenter.getThisMonthExpense(mainContext, userProfile.UserUID, walletAccountList[spinner.selectedItemPosition].WalletAccountID, thisMonth)

                presenter.getRecentExpenses(mainContext,userProfile.UserUID,walletAccountList[spinner.selectedItemPosition].WalletAccountID)
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


    override fun populateExpenseGraph(mainContext: Context, entryList: ArrayList<Entry>, xAxisList: ArrayList<String>) {

        displayDummyDateGraph(entryList, xAxisList)
    }

}