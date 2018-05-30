package br.com.gbmoro.diiin.ui.activity

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.*
import br.com.gbmoro.diiin.R
import br.com.gbmoro.diiin.util.ExpenseSharedPreferences
import br.com.gbmoro.diiin.model.Expense
import br.com.gbmoro.diiin.model.ExpenseType
import br.com.gbmoro.diiin.ui.TWEditPrice
import br.com.gbmoro.diiin.util.MathService
import br.com.gbmoro.diiin.util.MessageDialog
import java.util.*


class InsertExpenseActivity : AppCompatActivity() {

    private var mspSpinnerExpenseType: Spinner? = null
    private var metValue: EditText? = null
    private var metDescription: EditText? = null
    private var mtvDate: TextView? = null
    private var mibChangeDate: ImageButton? = null
    private var clCalenderChoosed : Calendar = Calendar.getInstance()
    private var mbtSave : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_expense)

        mspSpinnerExpenseType = findViewById(R.id.spinnerExpenseType)
        metValue = findViewById(R.id.etPriceValue)
        metDescription = findViewById(R.id.etDescriptionValue)
        mtvDate = findViewById(R.id.tvDateChoosed)
        mibChangeDate = findViewById(R.id.ibChangeDate)
        mbtSave = findViewById(R.id.btnSave)
    }


    override fun onStart() {
        super.onStart()

        mtvDate?.text = MathService.calendarTimeToString(clCalenderChoosed.time)

        loadSpinnersContent()

        metValue?.addTextChangedListener(TWEditPrice(metValue!!))

        loadDataPickerListener()

        mbtSave?.setTextColor(ContextCompat.getColor(this, R.color.activityColorBackground))

        mbtSave?.setOnClickListener {
            MessageDialog.showMessageDialog(this,
                    resources.getString(R.string.msgAreYouSure),
                    DialogInterface.OnClickListener { adialog, _ ->
                        if(metValue?.text.toString().isEmpty()) {
                            MessageDialog.showToastMessage(this, resources.getString(R.string.valueIsImportant))
                            adialog.dismiss()
                        } else {
                            val strExpenseType = mspSpinnerExpenseType?.selectedItem.toString()
                            val nExpenseTypeId = ExpenseType.gettingIdFromDescription(this, strExpenseType)
                            val etExpenseType = if (nExpenseTypeId != null)
                                ExpenseType.fromInt(nExpenseTypeId)
                            else null
                            val strDescription = metDescription?.text.toString()
                            val sValue = MathService.formatCurrencyValueToFloat(metValue?.text.toString())
                            val dtDate = clCalenderChoosed.time

                            val newExpense = Expense(null, sValue, strDescription, dtDate, etExpenseType)
                            ExpenseSharedPreferences.insertNewExpense(application.applicationContext, newExpense)
                            adialog.dismiss()
                            finish()
                        }
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
