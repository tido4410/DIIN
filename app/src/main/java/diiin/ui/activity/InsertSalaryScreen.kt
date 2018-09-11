package diiin.ui.activity

import android.app.DatePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.*
import br.com.gbmoro.diiin.R
import diiin.DindinApp
import diiin.dao.LocalCacheManager
import diiin.model.Expense
import diiin.model.ExpenseType
import diiin.model.Salary
import diiin.ui.TWEditPrice
import diiin.util.MathService
import diiin.util.MessageDialog
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Define a contract between view and presenter.
 * MVP pattern.
 * @author Gabriel Moro
 * @since 24/08/2018
 * @version 1.0.9
 */
interface InsertSalaryContract {
    /**
     * Define all view operations.
     */
    interface View {
        fun showSucessMessage()
        fun showUnsucessMessage()
        fun setDescription(astrDescription: String)
        fun setValue(astrValue: String)
    }

    /**
     * Define all operations connected to model layer
     */
    interface Presenter {
        fun loadSalaryValues(anSalaryID: Long?)
        fun saveSalary(anSalaryID: Long?, astrDescription: String, astrValue: String, adtDate: Date)
    }
}

/**
 * Presenter of InsertSalaryContract.View
 * @author Gabriel Moro
 * @since 11/09/2018
 * @version 1.0.9
 */
class InsertSalaryPresenter(avwView: InsertSalaryContract.View) : InsertSalaryContract.Presenter {

    private var view: InsertSalaryContract.View = avwView

    /**
     * Load the last salary registry according to ID.
     * If the Id is not null, this load occurs.
     */
    override fun loadSalaryValues(anSalaryID: Long?) {
        if (anSalaryID != null) {
            DindinApp.mlcmDataManager?.getSalaryAccordingId(anSalaryID,
                    object : LocalCacheManager.DatabaseCallBack {
                        override fun onExpensesLoaded(alstExpenses: List<Expense>) {}
                        override fun onExpenseTypeLoaded(alstExpensesType: List<ExpenseType>) {}
                        override fun onSalariesLoaded(alstSalaries: List<Salary>) {}
                        override fun onExpenseIdReceived(aexpense: Expense) {}
                        override fun onExpenseTypeColorReceived(astrColor: String) {}
                        override fun onExpenseTypeDescriptionReceived(astrDescription: String) {}
                        override fun onExpenseTypeIDReceived(anID: Long?) {}
                        override fun onSalaryObjectByIdReceived(aslSalary: Salary) {
                            view.setDescription(aslSalary.mstrSource)
                            if (aslSalary.msValue != null) view.setValue(MathService.formatFloatToCurrency(aslSalary.msValue!!))
                        }

                    })
        }
    }

    /**
     * Save the new salary or the update of it.
     */
    override fun saveSalary(anSalaryID: Long?, astrDescription: String, astrValue: String, adtDate: Date) {
        if (astrDescription.isEmpty() && astrValue.isEmpty()) {
            view.showUnsucessMessage()
        } else {
            val salaryTarget = Salary(anSalaryID, MathService.formatCurrencyValueToFloat(astrValue), astrDescription, MathService.calendarTimeToString(adtDate, DindinApp.mstrDateFormat))
            Observable.just(anSalaryID != null).subscribeOn(Schedulers.io())
                    .subscribe {
                        if (it)
                            DindinApp.mlcmDataManager?.mappDataBaseBuilder?.salaryDao()?.update(salaryTarget)
                        else
                            DindinApp.mlcmDataManager?.mappDataBaseBuilder?.salaryDao()?.add(salaryTarget)
                    }
            view.showSucessMessage()
        }
    }

}

/**
 * This screen is used by user to create the incoming register
 *
 * @author Gabriel Moro
 * @since 11/09/2018
 * @version 1.0.9
 */
class InsertSalaryActivity : AppCompatActivity(), InsertSalaryContract.View {

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
    private var presenter: InsertSalaryContract.Presenter? = null

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
            title = resources.getString(R.string.title_editsalary)
        }
        presenter = InsertSalaryPresenter(this)
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
