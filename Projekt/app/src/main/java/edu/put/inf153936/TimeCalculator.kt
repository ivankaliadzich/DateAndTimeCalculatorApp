package edu.put.inf153936

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class TimeCalculator : AppCompatActivity(), View.OnClickListener, TextView.OnEditorActionListener, View.OnFocusChangeListener{

    var inputMethodManager: InputMethodManager? = null
    var NumHourFirst: EditText?=null
    var NumMinuteFirst: EditText?=null
    var NumSecondFirst: EditText?=null
    var NumHourSecond: EditText?=null
    var NumMinuteSecond : EditText?=null
    var NumSecondSecond : EditText?=null
    var AddButton :  Button?=null
    var SubstractButton :Button?=null
    var ClearButton :Button?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_calculator)

        inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        NumHourFirst = findViewById(R.id.numHourFirst)
        NumMinuteFirst = findViewById(R.id.numMinuteFirst)
        NumSecondFirst = findViewById(R.id.numSecondFirst)
        NumHourSecond = findViewById(R.id.numHourSecond)
        NumMinuteSecond = findViewById(R.id.numMinuteSecond)
        NumSecondSecond = findViewById(R.id.numSecondSecond)
        AddButton = findViewById(R.id.addButton)
        SubstractButton = findViewById(R.id.subtractButton)
        ClearButton = findViewById(R.id.clearButton)

        NumHourFirst?.setOnClickListener(this)
        NumHourFirst?.onFocusChangeListener = this
        NumMinuteFirst?.setOnClickListener(this)
        NumMinuteFirst?.onFocusChangeListener = this
        NumSecondFirst?.setOnClickListener(this)
        NumSecondFirst?.onFocusChangeListener = this
        NumHourSecond?.setOnClickListener(this)
        NumHourSecond?.onFocusChangeListener = this
        NumMinuteSecond?.setOnClickListener(this)
        NumMinuteSecond?.onFocusChangeListener = this
        NumSecondSecond?.setOnClickListener(this)
        NumSecondSecond?.onFocusChangeListener = this
        AddButton?.setOnClickListener(this)
        SubstractButton?.setOnClickListener(this)
        ClearButton?.setOnClickListener(this)
    }


    override fun onClick(v: View?){
        var negative = false
        if (NumHourFirst?.editableText.toString() == "-0"){
            negative = true
        }

        var hourFirst: Int = NumHourFirst?.editableText.toString().toInt()
        var hourSecond: Int = NumHourSecond?.editableText.toString().toInt()
        var minuteFirst: Int = NumMinuteFirst?.editableText.toString().toInt()
        var minuteSecond: Int = NumMinuteSecond?.editableText.toString().toInt()
        var secondFirst: Int = NumSecondFirst?.editableText.toString().toInt()
        var secondSecond: Int = NumSecondSecond?.editableText.toString().toInt()

        var fullTimeFirst: Int
        var fullTimeSecond: Int
        if (hourFirst < 0 || negative){
            fullTimeFirst = 3600*hourFirst-60*minuteFirst-secondFirst
        } else {
            fullTimeFirst = 3600*hourFirst+60*minuteFirst+secondFirst
        }

        if (hourSecond < 0){
            fullTimeSecond = 3600*hourSecond-60*minuteSecond-secondSecond
        } else {
            fullTimeSecond = 3600*hourSecond+60*minuteSecond+secondSecond
        }

        if(v?.id == R.id.subtractButton){
                fullTimeSecond *= -1
        }

        NumHourFirst?.setText("0")
        NumMinuteFirst?.setText("0")
        NumSecondFirst?.setText("0")
        NumHourSecond?.setText("0")
        NumMinuteSecond?.setText("0")
        NumSecondSecond?.setText("0")

        if (v?.id!=R.id.clearButton){
            var sum = fullTimeFirst+fullTimeSecond
            negative = false

            if (sum < 0){
                negative = true
            }
            if (sum < 0 && sum/3600 == 0){
                NumHourFirst?.setText("-0")
            }else {
                NumHourFirst?.setText((sum / 3600).toString())
            }
            sum%=3600
            if (negative){
                sum*=-1
            }
            NumMinuteFirst?.setText((sum/60).toString())
            sum%=60
            NumSecondFirst?.setText((sum).toString())

        }
    }


    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean{
        if (actionId == EditorInfo.IME_ACTION_DONE){
            v?.clearFocus()
            inputMethodManager!!.hideSoftInputFromWindow(v?.windowToken, 0)
        }
        return false
    }


    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if(hasFocus){
            (v as EditText).editableText.clear()
        } else {
            if ((v as EditText).editableText.isEmpty()){
                v.setText("0")
            }
        }
    }
}