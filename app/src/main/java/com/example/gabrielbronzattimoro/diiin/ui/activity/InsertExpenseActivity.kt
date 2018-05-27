package com.example.gabrielbronzattimoro.diiin.ui.activity

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.*
import com.example.gabrielbronzattimoro.diiin.R
import com.example.gabrielbronzattimoro.diiin.util.ExpenseSharedPreferences
import com.example.gabrielbronzattimoro.diiin.model.Expense
import com.example.gabrielbronzattimoro.diiin.model.ExpenseType
import com.example.gabrielbronzattimoro.diiin.ui.TWEditPrice
import com.example.gabrielbronzattimoro.diiin.util.MathService
import com.example.gabrielbronzattimoro.diiin.util.MessageDialog
import java.util.*


class InsertExpenseActivity : AppCompatActivity() {

    var mspSpinnerExpenseType: Spinner? = null
    var metValue: EditText? = null
    var metDescription: EditText? = null
    var mtvDate: TextView? = null
    var mibChangeDate: ImageButton? = null
    var clCalenderChoosed : Calendar = Calendar.getInstance()
    var mbtnSave : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_expense)

        mspSpinnerExpenseType = findViewById(R.id.spinnerExpenseType)
        metValue = findViewById(R.id.etPriceValue)
        metDescription = findViewById(R.id.etDescriptionValue)
        mtvDate = findViewById(R.id.tvDateChoosed)
        mibChangeDate = findViewById(R.id.ibChangeDate)
        mbtnSave = findViewById(R.id.btnSave)
    }


    override fun onStart() {
        super.onStart()

        mtvDate?.text = MathService.calendarTimeToString(clCalenderChoosed.time)

        loadSpinnersContent()

        metValue?.addTextChangedListener(TWEditPrice(metValue!!))

        loadDataPickerListener()

        mbtnSave?.setOnClickListener {
            MessageDialog.showMessageDialog(this,
                    resources.getString(R.string.msgAreYouSure),
                    DialogInterface.OnClickListener { adialog, _ ->
                        val strExpenseType = mspSpinnerExpenseType?.selectedItem.toString()
                        val nExpenseTypeId = ExpenseType.gettingIdFromDescription(this, strExpenseType)
                        val etExpenseType = if(nExpenseTypeId!=null)
                            ExpenseType.fromInt(nExpenseTypeId)
                        else null
                        val strDescription = metDescription?.text.toString()
                        val sValue = MathService.formatCurrencyValueToFloat(metValue?.text.toString())
                        val dtDate = clCalenderChoosed.time

                        val newExpense = Expense(null, sValue, strDescription, dtDate, etExpenseType)
                        ExpenseSharedPreferences.insertNewExpense(application.applicationContext, newExpense)
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

    private fun loadSpinnersContent() {
        val lstExpenseTypesTitle = ArrayList<String>()
        ExpenseType.values().forEach { lstExpenseTypesTitle.add(it.description(this)) }
        val lstArrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lstExpenseTypesTitle)
        lstArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        mspSpinnerExpenseType?.adapter = lstArrayAdapter
    }
}
