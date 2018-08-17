package com.example.slnn3r.wallettrackermvp.View.Fragment.TrxCategory


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.slnn3r.wallettrackermvp.Adapter.TrxCategoryAdapter
import com.example.slnn3r.wallettrackermvp.Interface.PresenterInterface
import com.example.slnn3r.wallettrackermvp.Interface.ViewInterface
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.TransactionCategory
import com.example.slnn3r.wallettrackermvp.Presenter.Presenter
import com.example.slnn3r.wallettrackermvp.R
import kotlinx.android.synthetic.main.fragment_view_trx_category.*


class ViewTrxCategoryFragment : Fragment(), ViewInterface.TrxCategoryView {

    private lateinit var presenter: PresenterInterface.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.viewTrxCategoryFragmentTitle)

        return inflater.inflate(R.layout.fragment_view_trx_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = Presenter(this)

        hideDisplayedKeyboard(view)  // Hide Keyboard

        // Get SharedPreference data
        val userProfile = presenter.getUserData(context!!)
        val userID = userProfile.UserUID

        presenter = Presenter(this) // Populate RecycleView


        // Listener Setter
        VTCGoToCTC.setOnClickListener {
            createButtonClick(view)
        }

        VTCTrxTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                trxTypeSpinnerClick(position, userID)
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }
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

    private fun createButtonClick(view: View) {
        val navController = view.findNavController()
        navController.navigate(R.id.action_viewTrxCategoryFragment_to_createTrxCategoryFragment)
    }

    private fun trxTypeSpinnerClick(position: Int, userID: String) {

        if (position >= 0) {
            presenter.checkTransactionCategory(context!!, userID, VTCTrxTypeSpinner.selectedItem.toString())
        }
    }


    // Presenter Callback
    override fun populateTrxCategoryRecycleView(mainContext: Context, trxCategoryList: ArrayList<TransactionCategory>) {

        val vTCRecyclerView = (mainContext as Activity).findViewById(R.id.VTCRecyclerView) as RecyclerView

        vTCRecyclerView.layoutManager = LinearLayoutManager(mainContext)
        vTCRecyclerView.adapter = TrxCategoryAdapter(trxCategoryList)
    }


    override fun populateTrxCategoryRecycleViewFail(mainContext: Context, errorMessage: String) {
        Toast.makeText(mainContext, errorMessage, Toast.LENGTH_LONG).show()
    }
}
