package diiin.ui.insert_expense_screen

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
 * This screen is used by user to create the expense register
 *
 * @author Gabriel Moro
 */
class InsertExpenseActivity : AppCompatActivity(), InsertExpenseContract.View {

    companion object {
        const val INTENT_KEY_EXPENSEID: String = "IdOfExpenseToEdit"
    }

    private var presenter: InsertExpenseContract.Presenter? = null
    private var mspSpinnerExpenseType: Spinner? = null
    private var metValue: EditText? = null
    private var metDescription: EditText? = null
    private var mtvDate: TextView? = null
    private var mibChangeDate: ImageButton? = null
    private var clCalenderChoosed: Calendar = Calendar.getInstance()
    private var mbtSave: Button? = null
    private var mnExpenseId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_expense)

        mspSpinnerExpenseType = findViewById(R.id.spinnerExpenseType)
        metValue = findViewById(R.id.etPriceValue)
        metDescription = findViewById(R.id.etDescriptionValue)
        mtvDate = findViewById(R.id.tvDateChoosed)
        mibChangeDate = findViewById(R.id.ibChangeDate)
        mbtSave = findViewById(R.id.btnSave)

        if (intent.hasExtra(INTENT_KEY_EXPENSEID)) {
            mnExpenseId = intent.extras.getLong(INTENT_KEY_EXPENSEID)
            title = resources.getString(R.string.title_editexpense)
        }
        presenter = InsertExpensePresenter(this)
        presenter?.loadExpenseValues(mnExpenseId)
        presenter?.loadCategories(mnExpenseId)

    }

    override fun onStart() {
        super.onStart()

        mtvDate?.text = MathService.calendarTimeToString(clCalenderChoosed.time, DindinApp.mstrDateFormat)

        metValue?.addTextChangedListener(TWEditPrice(metValue!!))

        loadDataPickerListener()

        mbtSave?.setTextColor(ContextCompat.getColor(this, R.color.activityColorBackground))

        mbtSave?.setOnClickListener {
            val strValue = metValue?.text.toString()
            val strDescription = metDescription?.text.toString()
            val strExpenseType = mspSpinnerExpenseType?.selectedItem.toString()
            presenter?.saveExpense(mnExpenseId, strExpenseType, strDescription, strValue, clCalenderChoosed.time)
        }
    }

    /**
     * Define data picker setup.
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
     * Show the sucess message when user save a new expense or update it.
     */
    override fun showSucessMessage() {
        MessageDialog.showToastMessage(this, resources.getString(R.string.sucessaction))
    }

    /**
     * Show the unsucess message when user save a new expense or update it.
     */
    override fun showUnsucessMessage() {
        MessageDialog.showToastMessage(this, resources.getString(R.string.fillAreFields))
    }

    /**
     * Fill the spinner content with expense types available in app.
     */
    override fun fillCategoriesInSpinnerContentAndSelectSomeone(alstValues: ArrayList<String>, anItem: Int) {
        val lstArrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, alstValues)
        lstArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        mspSpinnerExpenseType?.adapter = lstArrayAdapter
        mspSpinnerExpenseType?.setSelection(anItem)
    }

    /**
     * Change the description text value.
     */
    override fun setDescription(astrDescription: String) {
        metDescription?.setText(astrDescription)
    }

    /**
     * Change the date text value.
     */
    override fun setDate(astrDate: String) {
        mtvDate?.text = astrDate
    }

    /**
     * Change the value text.
     */
    override fun setValue(astrValue: String) {
        metValue?.setText(astrValue)
    }
}
