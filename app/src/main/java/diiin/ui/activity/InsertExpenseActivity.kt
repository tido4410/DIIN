package diiin.ui.activity

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.*
import br.com.gbmoro.diiin.R
import diiin.StaticCollections
import diiin.model.Expense
import diiin.model.ExpenseType
import diiin.ui.TWEditPrice
import diiin.util.MathService
import diiin.util.MessageDialog
import java.util.*


/**
 * This screen is used by user to create the expense register
 *
 * @author Gabriel Moro
 */
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

        mtvDate?.text = MathService.calendarTimeToString(clCalenderChoosed.time, StaticCollections.mstrDateFormat)

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
                            val nExpenseTypeId = StaticCollections.mappDataBuilder?.expenseTypeDao()?.getId(strExpenseType)
                            if(nExpenseTypeId != null) {
                                val strDescription = metDescription?.text.toString()
                                val sValue = MathService.formatCurrencyValueToFloat(metValue?.text.toString())
                                val dtDate = MathService.calendarTimeToString(clCalenderChoosed.time, StaticCollections.mstrDateFormat)

                                val newExpense = Expense(null, sValue, strDescription, dtDate, nExpenseTypeId)
                                StaticCollections.mappDataBuilder?.expenseDao()?.add(newExpense)
                            }
                            adialog.dismiss()
                            finish()
                        }
                    },
                    DialogInterface.OnClickListener { adialog, _ ->
                        adialog.dismiss()
                    })
        }
    }

    private fun loadDataPickerListener() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            clCalenderChoosed.set(Calendar.YEAR, year)
            clCalenderChoosed.set(Calendar.MONTH, month)
            clCalenderChoosed.set(Calendar.DAY_OF_MONTH, day)

            if(MathService.isTheDateInCurrentYear(clCalenderChoosed.time)) {
                mtvDate?.text = MathService.calendarTimeToString(clCalenderChoosed.time, StaticCollections.mstrDateFormat)
            } else {
                clCalenderChoosed = Calendar.getInstance()
                Toast.makeText(this, resources.getString(R.string.messageAboutWrongYear), Toast.LENGTH_LONG).show()
            }
            mtvDate?.text = MathService.calendarTimeToString(clCalenderChoosed.time, StaticCollections.mstrDateFormat)
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
        StaticCollections.mappDataBuilder?.expenseTypeDao()?.all()?.forEach {
            lstExpenseTypesTitle.add(it.mstrDescription)
        }
        val lstArrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lstExpenseTypesTitle)
        lstArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        mspSpinnerExpenseType?.adapter = lstArrayAdapter
    }
}
