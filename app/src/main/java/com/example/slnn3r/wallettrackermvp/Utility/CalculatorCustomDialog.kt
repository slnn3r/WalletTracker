package com.example.slnn3r.wallettrackermvp.Utility

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.slnn3r.wallettrackermvp.R
import kotlinx.android.synthetic.main.calculator_custom_dialog.*
import java.text.DecimalFormat


class CalculatorCustomDialog: DialogFragment(){

    interface OnInputSelected{
        fun calculatorInput( input:String)
    }

    private lateinit var go: OnInputSelected

    private var valueOne = java.lang.Double.NaN
    private var valueTwo: Double = 0.toDouble()

    private lateinit var addition:String
    private lateinit var subtraction:String
    private lateinit var multiplication:String
    private lateinit var division:String
    private var currentAction = ""

    private var rewriting=false

    private lateinit var decFormat: DecimalFormat

    override fun onCreateView
            (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        addition=getString(R.string.additionStatus)
        subtraction=getString(R.string.subtractionStatus)
        multiplication=getString(R.string.multiplicationStatus)
        division=getString(R.string.divisionStatus)
        decFormat = DecimalFormat(getString(R.string.decimalFormat))

        return inflater.inflate(R.layout.calculator_custom_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mArgs = this.arguments
        val myValue = mArgs!!.getString(getString(R.string.trxAmountPassArgKey))
        calCustomDialogTextView.setText(myValue)

        calCustomDialogTextView.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("","")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val text = calCustomDialogTextView.text.toString()
                if (text.contains(".") && text.substring(text.indexOf(".") + 1).length > 2) {
                    calCustomDialogTextView.setText(text.substring(0, text.length - 1))
                    calCustomDialogTextView.setSelection(calCustomDialogTextView.text.length)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                Log.d("","")
            }
        })

        btnConfirm.setOnClickListener{

            if(calCustomDialogTextView.text.toString()==getString(R.string.NotANumber)){
                go.calculatorInput("")
            }else{
                go.calculatorInput(calCustomDialogTextView.text.toString())
            }

            dialog.dismiss()
        }

        btnCancel.setOnClickListener{
            go.calculatorInput(myValue)
            dialog.dismiss()
        }

        btn0.setOnClickListener{
            calCustomDialogTextView.setTextColor(Color.BLACK)

            if(!rewriting){
                calCustomDialogTextView.text = calCustomDialogTextView.text.append(btn0.text)

            }else{
                calCustomDialogTextView.setText(btn0.text.toString())
                rewriting=false
            }
        }

        btn1.setOnClickListener{
            calCustomDialogTextView.setTextColor(Color.BLACK)

            if(!rewriting){
                calCustomDialogTextView.text = calCustomDialogTextView.text.append(btn1.text)
            }else{
                calCustomDialogTextView.setText(btn1.text.toString())
                rewriting=false
            }
        }

        btn2.setOnClickListener{
            calCustomDialogTextView.setTextColor(Color.BLACK)

            if(!rewriting){
                calCustomDialogTextView.text = calCustomDialogTextView.text.append(btn2.text)
            }else{
                calCustomDialogTextView.setText(btn2.text.toString())
                rewriting=false
            }
        }

        btn3.setOnClickListener{

            calCustomDialogTextView.setTextColor(Color.BLACK)

            if(!rewriting){
                calCustomDialogTextView.text = calCustomDialogTextView.text.append(btn3.text)
            }else{
                calCustomDialogTextView.setText(btn3.text.toString())
                rewriting=false
            }
        }

        btn4.setOnClickListener{
            calCustomDialogTextView.setTextColor(Color.BLACK)

            if(!rewriting){
                calCustomDialogTextView.text = calCustomDialogTextView.text.append(btn4.text)
            }else{
                calCustomDialogTextView.setText(btn4.text.toString())
                rewriting=false
            }
        }

        btn5.setOnClickListener{
            calCustomDialogTextView.setTextColor(Color.BLACK)

            if(!rewriting){
                calCustomDialogTextView.text = calCustomDialogTextView.text.append(btn5.text)
            }else{
                calCustomDialogTextView.setText(btn5.text.toString())
                rewriting=false
            }
        }

        btn6.setOnClickListener{
            calCustomDialogTextView.setTextColor(Color.BLACK)

            if(!rewriting){
                calCustomDialogTextView.text = calCustomDialogTextView.text.append(btn6.text)
            }else{
                calCustomDialogTextView.setText(btn6.text.toString())
                rewriting=false
            }
        }

        btn7.setOnClickListener{
            calCustomDialogTextView.setTextColor(Color.BLACK)

            if(!rewriting){
                calCustomDialogTextView.text = calCustomDialogTextView.text.append(btn7.text)
            }else{
                calCustomDialogTextView.setText(btn7.text.toString())
                rewriting=false
            }
        }

        btn8.setOnClickListener{
            calCustomDialogTextView.setTextColor(Color.BLACK)

            if(!rewriting){
                calCustomDialogTextView.text = calCustomDialogTextView.text.append(btn8.text)
            }else{
                calCustomDialogTextView.setText(btn8.text.toString())
                rewriting=false
            }
        }

        btn9.setOnClickListener{
            calCustomDialogTextView.setTextColor(Color.BLACK)

            if(!rewriting){
                calCustomDialogTextView.text = calCustomDialogTextView.text.append(btn9.text)
            }else{
                calCustomDialogTextView.setText(btn9.text.toString())
                rewriting=false
            }
        }

        btnDot.setOnClickListener{
            calCustomDialogTextView.setTextColor(Color.BLACK)

            if(!rewriting){
                calCustomDialogTextView.text = calCustomDialogTextView.text.append(btnDot.text)
            }else{
                calCustomDialogTextView.setText(btnDot.text.toString())
                rewriting=false
            }
        }

        btnDelete.setOnClickListener{

            if(!rewriting){
                val length = calCustomDialogTextView.text.length
                if (length > 0) {
                    calCustomDialogTextView.text.delete(length - 1, length)
                }
            }
        }

        btnClear.setOnClickListener{
            calCustomDialogTextView.setText("")
            resetCalculator()
        }

        btnPlus.setOnClickListener{

            computeCalculation()
            currentAction = addition

            btnPlus.setTextColor(Color.BLUE)
            btnMinus.setTextColor(Color.BLACK)
            btnMultiply.setTextColor(Color.BLACK)
            btnDivide.setTextColor(Color.BLACK)

            calCustomDialogTextView.setText(decFormat.format(valueOne))
            calCustomDialogTextView.setTextColor(Color.BLUE)
            rewriting=true
        }

        btnMinus.setOnClickListener{

            computeCalculation()
            currentAction = subtraction

            btnPlus.setTextColor(Color.BLACK)
            btnMinus.setTextColor(Color.BLUE)
            btnMultiply.setTextColor(Color.BLACK)
            btnDivide.setTextColor(Color.BLACK)

            calCustomDialogTextView.setText(decFormat.format(valueOne))
            calCustomDialogTextView.setTextColor(Color.BLUE)
            rewriting=true
        }

        btnMultiply.setOnClickListener{

            computeCalculation()
            currentAction = multiplication

            btnPlus.setTextColor(Color.BLACK)
            btnMinus.setTextColor(Color.BLACK)
            btnMultiply.setTextColor(Color.BLUE)
            btnDivide.setTextColor(Color.BLACK)

            calCustomDialogTextView.setText(decFormat.format(valueOne))
            calCustomDialogTextView.setTextColor(Color.BLUE)
            rewriting=true
        }

        btnDivide.setOnClickListener{

            computeCalculation()
            currentAction = division

            btnPlus.setTextColor(Color.BLACK)
            btnMinus.setTextColor(Color.BLACK)
            btnMultiply.setTextColor(Color.BLACK)
            btnDivide.setTextColor(Color.BLUE)

            calCustomDialogTextView.setText(decFormat.format(valueOne))
            calCustomDialogTextView.setTextColor(Color.BLUE)
            rewriting=true
        }


        btnEqual.setOnClickListener{

            computeCalculation()
            calCustomDialogTextView.setText(decFormat.format(valueOne))
            calCustomDialogTextView.setTextColor(Color.BLUE)

            resetCalculator()
        }
    }

    private fun computeCalculation() {
        if (!java.lang.Double.isNaN(valueOne)) {

            valueTwo = if(calCustomDialogTextView.text.toString().toDoubleOrNull()==null){
                Double.NaN
            }else{
                calCustomDialogTextView.text.toString().toDouble()
            }

            calCustomDialogTextView.text = null

            if (currentAction == addition){
                valueOne += valueTwo
                checkExcessiveAmount()
            }else if (currentAction == subtraction){
                valueOne -= valueTwo
                checkExcessiveAmount()
            }else if (currentAction == multiplication){
                valueOne *= valueTwo
                checkExcessiveAmount()

            }else if (currentAction == division){
                valueOne /= valueTwo
                checkExcessiveAmount()

            }
        } else {
            try {
                valueOne = calCustomDialogTextView.text.toString().toDouble()
            } catch (e: Exception) {
            }
        }
    }

    private fun checkExcessiveAmount() {
        if(valueOne>getString(R.string.maxCalAmount).toDouble() || valueOne<getString(R.string.minCalAmount).toDouble()){
            valueOne = Double.NaN
            calCustomDialogTextView.setText(decFormat.format(valueOne))
            calCustomDialogTextView.setTextColor(Color.BLUE)
            currentAction = ""
            rewriting=false
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try{

            go = this.targetFragment as OnInputSelected

        }catch (e:ClassCastException){
            Log.e("",e.toString())
        }
    }

    private fun resetCalculator(){
        btnPlus.setTextColor(Color.BLACK)
        btnMinus.setTextColor(Color.BLACK)
        btnMultiply.setTextColor(Color.BLACK)
        btnDivide.setTextColor(Color.BLACK)
        valueOne = Double.NaN
        currentAction = ""
        rewriting=true
    }
}