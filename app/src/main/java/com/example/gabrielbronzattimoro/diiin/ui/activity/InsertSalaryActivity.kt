package com.example.gabrielbronzattimoro.diiin.ui.activity

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.gabrielbronzattimoro.diiin.R
import com.example.gabrielbronzattimoro.diiin.util.SalarySharedPreferences
import com.example.gabrielbronzattimoro.diiin.model.Salary
import com.example.gabrielbronzattimoro.diiin.ui.TWEditPrice
import com.example.gabrielbronzattimoro.diiin.util.MathService
import com.example.gabrielbronzattimoro.diiin.util.MessageDialog
import java.util.*

class InsertSalaryActivity : AppCompatActivity() {

    private var clCalenderChoosed: Calendar = Calendar.getInstance()
    private var mtvDate: TextView? = null
    private var mibChangeDate: ImageButton? = null
    private var metDescriptionValue: EditText? = null
    private var metPriceValue: EditText? = null
    private var mbtSave: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_salary)

        mtvDate = findViewById(R.id.tvDateChoosed)
        mibChangeDate = findViewById(R.id.ibChangeDate)
        metDescriptionValue = findViewById(R.id.etDescriptionValue)
        metPriceValue = findViewById(R.id.etPriceValue)
        mbtSave = findViewById(R.id.btnSave)
    }

    override fun onStart() {
        super.onStart()

        mtvDate?.text = MathService.calendarTimeToString(clCalenderChoosed.time)

        metPriceValue?.addTextChangedListener(TWEditPrice(metPriceValue!!))

        loadDataPickerListener()

        mbtSave?.setOnClickListener {

            MessageDialog.showMessageDialog(this,
                    resources.getString(R.string.msgAreYouSure),
                    DialogInterface.OnClickListener { adialog, _ ->
                        val sValue = MathService.formatCurrencyValueToFloat(metPriceValue?.text.toString())
                                ?: 0f
                        val dtDate = clCalenderChoosed.time
                        val strDescription = metDescriptionValue?.text.toString()

                        val newSalary = Salary(strDescription, sValue, dtDate)
                        SalarySharedPreferences.insertNewSalary(application.applicationContext, newSalary)
                        adialog.dismiss()
                        finish()
                    },
                    DialogInterface.OnClickListener { adialog, _ ->
                        adialog.dismiss()
                        finish()
                    })
        }
    }


    private fun loadDataPickerListener() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            clCalenderChoosed.set(Calendar.YEAR, year)
            clCalenderChoosed.set(Calendar.MONTH, month)
            clCalenderChoosed.set(Calendar.DAY_OF_MONTH, day)

            if (MathService.isTheDateInCurrentYear(clCalenderChoosed.time)) {
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
