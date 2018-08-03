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

    private var ADDITION:String = "+"
    private var SUBTRACTION:String = "-"
    private var MULTIPLICATION:String = "*"
    private var DIVISION:String = "/"

    private var CURRENT_ACTION = ""

    private var rewriting=false

    private lateinit var decFormat: DecimalFormat

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        decFormat = DecimalFormat("#.##")

        return inflater.inflate(R.layout.calculator_custom_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mArgs = this.arguments
        val myValue = mArgs!!.getString("trxAmount")
        textView.setText(myValue)

        textView.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("","")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val text = textView.text.toString()
                if (text.contains(".") && text.substring(text.indexOf(".") + 1).length > 2) {
                    textView.setText(text.substring(0, text.length - 1))
                    textView.setSelection(textView.text.length)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                Log.d("","")
            }
        })

        btnConfirm.setOnClickListener{

            if(textView.text.toString()=="NaN"){
                go.calculatorInput("")
            }else{
                go.calculatorInput(textView.text.toString())
            }

            dialog.dismiss()
        }

        btnCancel.setOnClickListener{
            go.calculatorInput(myValue)
            dialog.dismiss()
        }

        btn0.setOnClickListener{
            textView.setTextColor(Color.BLACK)

            if(!rewriting){
                textView.text = textView.text.append(btn0.text)

            }else{
                textView.setText(btn0.text.toString())
                rewriting=false
            }

        }

        btn1.setOnClickListener{
            textView.setTextColor(Color.BLACK)

            if(!rewriting){
                textView.text = textView.text.append(btn1.text)
            }else{
                textView.setText(btn1.text.toString())
                rewriting=false
            }

        }

        btn2.setOnClickListener{
            textView.setTextColor(Color.BLACK)

            if(!rewriting){
                textView.text = textView.text.append(btn2.text)
            }else{
                textView.setText(btn2.text.toString())
                rewriting=false
            }
        }

        btn3.setOnClickListener{

            textView.setTextColor(Color.BLACK)

            if(!rewriting){
                textView.text = textView.text.append(btn3.text)
            }else{
                textView.setText(btn3.text.toString())
                rewriting=false
            }

        }

        btn4.setOnClickListener{
            textView.setTextColor(Color.BLACK)

            if(!rewriting){
                textView.text = textView.text.append(btn4.text)
            }else{
                textView.setText(btn4.text.toString())
                rewriting=false
            }

        }

        btn5.setOnClickListener{
            textView.setTextColor(Color.BLACK)

            if(!rewriting){
                textView.text = textView.text.append(btn5.text)
            }else{
                textView.setText(btn5.text.toString())
                rewriting=false
            }
        }

        btn6.setOnClickListener{
            textView.setTextColor(Color.BLACK)

            if(!rewriting){
                textView.text = textView.text.append(btn6.text)
            }else{
                textView.setText(btn6.text.toString())
                rewriting=false
            }
        }

        btn7.setOnClickListener{
            textView.setTextColor(Color.BLACK)

            if(!rewriting){
                textView.text = textView.text.append(btn7.text)
            }else{
                textView.setText(btn7.text.toString())
                rewriting=false
            }
        }

        btn8.setOnClickListener{
            textView.setTextColor(Color.BLACK)

            if(!rewriting){
                textView.text = textView.text.append(btn8.text)
            }else{
                textView.setText(btn8.text.toString())
                rewriting=false
            }
        }

        btn9.setOnClickListener{
            textView.setTextColor(Color.BLACK)

            if(!rewriting){
                textView.text = textView.text.append(btn9.text)
            }else{
                textView.setText(btn9.text.toString())
                rewriting=false
            }
        }

        btnDot.setOnClickListener{
            textView.setTextColor(Color.BLACK)

            if(!rewriting){
                textView.text = textView.text.append(btnDot.text)
            }else{
                textView.setText(btnDot.text.toString())
                rewriting=false
            }
        }

        btnDelete.setOnClickListener{

            if(!rewriting){
                val length = textView.text.length
                if (length > 0) {
                    textView.text.delete(length - 1, length)
                }
            }


        }


        btnClear.setOnClickListener{
            textView.setText("")
            resetCalculator()
        }

        btnPlus.setOnClickListener{

            computeCalculation()
            CURRENT_ACTION = ADDITION

            btnPlus.setTextColor(Color.BLUE)
            btnMinus.setTextColor(Color.BLACK)
            btnMultiply.setTextColor(Color.BLACK)
            btnDivide.setTextColor(Color.BLACK)

            textView.setText(decFormat.format(valueOne))
            textView.setTextColor(Color.BLUE)
            rewriting=true
        }

        btnMinus.setOnClickListener{

            computeCalculation()
            CURRENT_ACTION = SUBTRACTION

            btnPlus.setTextColor(Color.BLACK)
            btnMinus.setTextColor(Color.BLUE)
            btnMultiply.setTextColor(Color.BLACK)
            btnDivide.setTextColor(Color.BLACK)

            textView.setText(decFormat.format(valueOne))
            textView.setTextColor(Color.BLUE)
            rewriting=true

        }

        btnMultiply.setOnClickListener{

            computeCalculation()
            CURRENT_ACTION = MULTIPLICATION

            btnPlus.setTextColor(Color.BLACK)
            btnMinus.setTextColor(Color.BLACK)
            btnMultiply.setTextColor(Color.BLUE)
            btnDivide.setTextColor(Color.BLACK)

            textView.setText(decFormat.format(valueOne))
            textView.setTextColor(Color.BLUE)
            rewriting=true

        }

        btnDivide.setOnClickListener{

            computeCalculation()
            CURRENT_ACTION = DIVISION

            btnPlus.setTextColor(Color.BLACK)
            btnMinus.setTextColor(Color.BLACK)
            btnMultiply.setTextColor(Color.BLACK)
            btnDivide.setTextColor(Color.BLUE)

            textView.setText(decFormat.format(valueOne))
            textView.setTextColor(Color.BLUE)
            rewriting=true

        }


        btnEqual.setOnClickListener{

            computeCalculation()
            textView.setText(decFormat.format(valueOne))
            textView.setTextColor(Color.BLUE)

            resetCalculator()


        }



    }

    private fun computeCalculation() {
        if (!java.lang.Double.isNaN(valueOne)) {

            if(textView.text.toString().toDoubleOrNull()==null){
                valueTwo=Double.NaN
            }else{
                valueTwo = textView.text.toString().toDouble()
            }

            textView.text = null

            if (CURRENT_ACTION == ADDITION){
                valueOne += valueTwo
                checkExcessiveAmount()
            }else if (CURRENT_ACTION == SUBTRACTION){
                valueOne -= valueTwo
                checkExcessiveAmount()
            }else if (CURRENT_ACTION == MULTIPLICATION){
                valueOne *= valueTwo
                checkExcessiveAmount()

            }else if (CURRENT_ACTION == DIVISION){
                valueOne /= valueTwo
                checkExcessiveAmount()

            }
        } else {
            try {
                valueOne = textView.text.toString().toDouble()
            } catch (e: Exception) {
            }
        }
    }

    private fun checkExcessiveAmount() {
        if(valueOne>999999999.99 || valueOne<-999999999.99){
            valueOne = Double.NaN
            textView.setText(decFormat.format(valueOne))
            textView.setTextColor(Color.BLUE)
            CURRENT_ACTION = ""
            rewriting=false
        }
    }

        override fun onAttach(context: Context?) {
        super.onAttach(context)

        try{

            go = this.targetFragment as OnInputSelected

        }catch (e:ClassCastException){
            Log.e("0",e.toString())
        }

    }

    private fun resetCalculator(){
        btnPlus.setTextColor(Color.BLACK)
        btnMinus.setTextColor(Color.BLACK)
        btnMultiply.setTextColor(Color.BLACK)
        btnDivide.setTextColor(Color.BLACK)
        valueOne = Double.NaN
        CURRENT_ACTION = ""
        rewriting=true
    }


}