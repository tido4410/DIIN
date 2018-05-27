package com.example.gabrielbronzattimoro.diiin.ui

import android.app.DatePickerDialog
import android.media.Image
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.gabrielbronzattimoro.diiin.R
import com.example.gabrielbronzattimoro.diiin.dao.SalarySharedPreferences
import com.example.gabrielbronzattimoro.diiin.model.Salary
import com.example.gabrielbronzattimoro.diiin.util.MathService
import java.util.*

class InsertSalaryActivity : AppCompatActivity() {

    var clCalenderChoosed : Calendar = Calendar.getInstance()
    var mtvDate: TextView? = null
    var mibChangeDate: ImageButton? = null
    var metDescriptionValue : EditText? = null
    var metPriceValue : EditText? = null
    var mbtnSave : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_salary)

        mtvDate = findViewById(R.id.tvDateChoosed)
        mibChangeDate = findViewById(R.id.ibChangeDate)
        metDescriptionValue= findViewById(R.id.etDescriptionValue)
        metPriceValue= findViewById(R.id.etPriceValue)
        mbtnSave = findViewById(R.id.btnSave)

    }

    override fun onStart() {
        super.onStart()

        mtvDate?.text = MathService.calendarTimeToString(clCalenderChoosed.time)

        metPriceValue?.addTextChangedListener(TWEditPrice(metPriceValue!!))

        loadDataPickerListener()

        mbtnSave?.setOnClickListener {
            val sValue = MathService.formatCurrencyValueToFloat(metPriceValue?.text.toString()) ?: 0f
            val dtDate = clCalenderChoosed.time
            val strDescription = metDescriptionValue?.text.toString()

            val newSalary = Salary(strDescription, sValue, dtDate)
            SalarySharedPreferences.insertNewSalary(application.applicationContext, newSalary)
            finish()
        }
    }


    private fun loadDataPickerListener() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { vw, year, month, day ->
            clCalenderChoosed.set(Calendar.YEAR, year)
            clCalenderChoosed.set(Calendar.MONTH, month)
            clCalenderChoosed.set(Calendar.DAY_OF_MONTH, day)

            if(MathService.isTheDateInCurrentYear(clCalenderChoosed.time)) {
                mtvDate?.text = MathService.calendarTimeToString(clCalenderChoosed.time)
            } else {
                clCalenderChoosed = Calendar.getInstance()
                Toast.makeText(this, resources.getString(R.string.messageAboutWrongYear), Toast.LENGTH_LONG).show()
            }
            mtvDate?.text = MathService.calendarTimeToString(clCalenderChoosed.time)
        }
        mibChangeDate?.setOnClickListener {
            DatePickerDialog(this, dateSetListener,
                    clCalenderChoosed.get(Calendar.YEAR),
                    clCalenderChoosed.get(Calendar.MONTH),
                    clCalenderChoosed.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

}
