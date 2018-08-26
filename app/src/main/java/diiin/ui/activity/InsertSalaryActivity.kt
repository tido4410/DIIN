package diiin.ui.activity

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.*
import br.com.gbmoro.diiin.R
import diiin.StaticCollections
import diiin.model.Salary
import diiin.ui.TWEditPrice
import diiin.util.MathService
import diiin.util.MessageDialog
import java.util.*

/**
 * This screen is used by user to create the incoming register
 *
 * @author Gabriel Moro
 */
class InsertSalaryActivity : AppCompatActivity() {

    companion object {
        const val INTENT_KEY_SALARYID : String = "IdOfSalaryToEdit"
    }

    private var clCalenderChoosed: Calendar = Calendar.getInstance()
    private var mtvDate: TextView? = null
    private var mibChangeDate: ImageButton? = null
    private var metDescriptionValue: EditText? = null
    private var metPriceValue: EditText? = null
    private var mbtSave: Button? = null
    private var mnSalaryId : Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_salary)

        mtvDate = findViewById(R.id.tvDateChoosed)
        mibChangeDate = findViewById(R.id.ibChangeDate)
        metDescriptionValue = findViewById(R.id.etDescriptionValue)
        metPriceValue = findViewById(R.id.etPriceValue)
        mbtSave = findViewById(R.id.btnSave)

        if(intent.hasExtra(INTENT_KEY_SALARYID)) {
            mnSalaryId = intent.extras.getLong(INTENT_KEY_SALARYID)
            title = resources.getString(R.string.title_editsalary)
            val salaryTarget = StaticCollections.mappDataBuilder?.salaryDao()?.all()?.filter { it.mnID == mnSalaryId }?.first()
            val strValue =
                    if(salaryTarget?.msValue!=null) MathService.formatFloatToCurrency(salaryTarget.msValue!!)
                    else ""
            metPriceValue?.setText(strValue)
            metDescriptionValue?.setText(salaryTarget?.mstrSource)
        }
    }

    override fun onStart() {
        super.onStart()

        mtvDate?.text = MathService.calendarTimeToString(clCalenderChoosed.time, StaticCollections.mstrDateFormat)

        metPriceValue?.addTextChangedListener(TWEditPrice(metPriceValue!!))

        loadDataPickerListener()

        mbtSave?.setTextColor(ContextCompat.getColor(this, R.color.activityColorBackground))

        mbtSave?.setOnClickListener {

            MessageDialog.showMessageDialog(this,
                    resources.getString(R.string.msgAreYouSure),
                    DialogInterface.OnClickListener { adialog, _ ->
                        if(metPriceValue?.text.toString().isEmpty()) {
                            MessageDialog.showToastMessage(this, resources.getString(R.string.valueIsImportant))
                            adialog.dismiss()
                        } else {
                            val sValue = MathService.formatCurrencyValueToFloat(metPriceValue?.text.toString())
                                    ?: 0f
                            val dtDate = MathService.calendarTimeToString(clCalenderChoosed.time, StaticCollections.mstrDateFormat)
                            val strDescription = metDescriptionValue?.text.toString()

                            if(mnSalaryId != null){
                                val currentSalary = Salary(mnSalaryId, sValue, strDescription, dtDate)
                                StaticCollections.mappDataBuilder?.salaryDao()?.update(currentSalary)
                            } else {
                                val newSalary = Salary(null, sValue, strDescription, dtDate)
                                StaticCollections.mappDataBuilder?.salaryDao()?.add(newSalary)
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

            if (MathService.isTheDateInCurrentYear(clCalenderChoosed.time)) {
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

}
