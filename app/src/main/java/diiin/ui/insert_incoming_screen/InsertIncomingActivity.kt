package diiin.ui.insert_incoming_screen

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.*
import br.com.gbmoro.diiin.R
import diiin.DindinApp
import diiin.ui.TWEditPrice
import diiin.util.MathService
import diiin.util.MessageDialog
import java.util.*

/**
 * This screen is used by user to create the incoming register
 *
 * @author Gabriel Moro
 * @since 11/09/2018
 * @version 1.0.9
 */
class InsertIncomingActivity : AppCompatActivity(), InsertIncomingContract.View {

    companion object {
        const val INTENT_KEY_SALARYID: String = "IdOfSalaryToEdit"
    }

    private var clCalenderChoosed: Calendar = Calendar.getInstance()
    private var mtvDate: TextView? = null
    private var mibChangeDate: ImageButton? = null
    private var metDescriptionValue: EditText? = null
    private var metPriceValue: EditText? = null
    private var mbtSave: Button? = null
    private var mnSalaryId: Long? = null
    private var presenter: InsertIncomingContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_salary)

        mtvDate = findViewById(R.id.tvDateChoosed)
        mibChangeDate = findViewById(R.id.ibChangeDate)
        metDescriptionValue = findViewById(R.id.etDescriptionValue)
        metPriceValue = findViewById(R.id.etPriceValue)
        mbtSave = findViewById(R.id.btnSave)

        if (intent.hasExtra(INTENT_KEY_SALARYID)) {
            mnSalaryId = intent.extras.getLong(INTENT_KEY_SALARYID)
            title = resources.getString(R.string.title_editincoming)
        }
        presenter = InsertIncomingPresenter(this)
        presenter?.loadSalaryValues(mnSalaryId)

    }

    override fun onStart() {
        super.onStart()

        mtvDate?.text = MathService.calendarTimeToString(clCalenderChoosed.time, DindinApp.mstrDateFormat)

        metPriceValue?.addTextChangedListener(TWEditPrice(metPriceValue!!))

        loadDataPickerListener()

        mbtSave?.setTextColor(ContextCompat.getColor(this, R.color.activityColorBackground))

        mbtSave?.setOnClickListener {
            presenter?.saveSalary(mnSalaryId, metDescriptionValue?.text.toString(), metPriceValue?.text.toString(), clCalenderChoosed.time)
        }
    }


    /**
     * Setup of data picker.
     */
    private fun loadDataPickerListener() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            clCalenderChoosed.set(Calendar.YEAR, year)
            clCalenderChoosed.set(Calendar.MONTH, month)
            clCalenderChoosed.set(Calendar.DAY_OF_MONTH, day)

            if (MathService.isTheDateInCurrentYear(clCalenderChoosed.time)) {
                mtvDate?.text = MathService.calendarTimeToString(clCalenderChoosed.time, DindinApp.mstrDateFormat)
            } else {
                clCalenderChoosed = Calendar.getInstance()
                Toast.makeText(this, resources.getString(R.string.messageAboutWrongYear), Toast.LENGTH_LONG).show()
            }
            mtvDate?.text = MathService.calendarTimeToString(clCalenderChoosed.time, DindinApp.mstrDateFormat)
        }
        mibChangeDate?.setOnClickListener {
            DatePickerDialog(this, dateSetListener,
                    clCalenderChoosed.get(Calendar.YEAR),
                    clCalenderChoosed.get(Calendar.MONTH),
                    clCalenderChoosed.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    /**
     * Show sucess message when operation result is correct.
     */
    override fun showSucessMessage() {
        MessageDialog.showToastMessage(this, resources.getString(R.string.sucessaction))
    }

    /**
     * Show unsucess message when operation result is not correct.
     */
    override fun showUnsucessMessage() {
        MessageDialog.showToastMessage(this, resources.getString(R.string.fillAreFields))
    }

    /**
     * Change the description text value.
     */
    override fun setDescription(astrDescription: String) {
        metDescriptionValue?.setText(astrDescription)
    }

    /**
     * Change the value text.
     */
    override fun setValue(astrValue: String) {
        metPriceValue?.setText(astrValue)
    }

}