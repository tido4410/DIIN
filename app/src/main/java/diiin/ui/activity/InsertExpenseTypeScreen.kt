package diiin.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import br.com.gbmoro.diiin.R
import android.graphics.Color
import android.view.View
import android.widget.EditText
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import diiin.DindinApp
import diiin.dao.LocalCacheManager
import diiin.model.Expense
import diiin.model.ExpenseType
import diiin.model.Salary
import diiin.util.MessageDialog
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers


interface InsertExpenseTypeContract {
    interface View {
        fun showSucessMessage()
        fun showUnsucessMessage()
        fun setColor(anColorValue: Int)
        fun setDecription(astrDescription: String)
    }

    interface Presenter {
        fun loadExpenseTypeValues(anLongExpenseID: Long?)
        fun saveExpenseType(anExpenseTypeID: Long?, astrDescription: String, anColorSelected: Int)
    }
}

class InsertExpenseTypePresenter(avwView: InsertExpenseTypeContract.View) : InsertExpenseTypeContract.Presenter {

    private var view: InsertExpenseTypeContract.View = avwView

    override fun loadExpenseTypeValues(anLongExpenseID: Long?) {
        anLongExpenseID ?: return

        DindinApp.mlcmDataManager?.getExpenseTypeColor(anLongExpenseID, object : LocalCacheManager.DatabaseCallBack {
            override fun onSalaryObjectByIdReceived(aslSalary: Salary) {}
            override fun onExpensesLoaded(alstExpenses: List<Expense>) {}
            override fun onExpenseTypeLoaded(alstExpensesType: List<ExpenseType>) {}
            override fun onSalariesLoaded(alstSalaries: List<Salary>) {}
            override fun onExpenseTypeColorReceived(astrColor: String) {
                val nColor = Color.parseColor(astrColor)
                view.setColor(nColor)
            }

            override fun onExpenseTypeDescriptionReceived(astrDescription: String) {}
            override fun onExpenseTypeIDReceived(anID: Long?) {}
            override fun onExpenseIdReceived(aexpense: Expense) {}
        })

        DindinApp.mlcmDataManager?.getExpenseTypeDescription(anLongExpenseID, object : LocalCacheManager.DatabaseCallBack {
            override fun onSalaryObjectByIdReceived(aslSalary: Salary) {}
            override fun onExpensesLoaded(alstExpenses: List<Expense>) {}
            override fun onExpenseTypeLoaded(alstExpensesType: List<ExpenseType>) {}
            override fun onSalariesLoaded(alstSalaries: List<Salary>) {}
            override fun onExpenseTypeColorReceived(astrColor: String) {}
            override fun onExpenseTypeDescriptionReceived(astrDescription: String) {
                view.setDecription(astrDescription)
            }

            override fun onExpenseTypeIDReceived(anID: Long?) {}
            override fun onExpenseIdReceived(aexpense: Expense) {}
        })
    }

    override fun saveExpenseType(anExpenseTypeID: Long?, astrDescription: String, anColorSelected: Int) {
        val expenseTarget = ExpenseType(anExpenseTypeID, astrDescription, "#${Integer.toHexString(anColorSelected)}")
        if (astrDescription.isEmpty()) {
            view.showUnsucessMessage()
        } else {

            Observable.just(anExpenseTypeID != null).subscribeOn(Schedulers.io()).subscribe {
                if (it)
                    DindinApp.mlcmDataManager?.mappDataBaseBuilder?.expenseTypeDao()?.update(expenseTarget)
                else
                    DindinApp.mlcmDataManager?.mappDataBaseBuilder?.expenseTypeDao()?.add(expenseTarget)
            }
            view.showSucessMessage()
        }
    }

}

class InsertExpenseTypeActivity : AppCompatActivity(), InsertExpenseTypeContract.View {

    private var presenter: InsertExpenseTypeContract.Presenter? = null
    private var mbtnChangeColor: Button? = null
    private var mvwCurrentColor: View? = null
    private var mbtnSaveExpenseType: Button? = null
    private var metEditText: EditText? = null
    private var mnColorSelected: Int = 0
    private var mnExpenseTypeId: Long? = null

    companion object {
        const val INTENT_KEY_EXPENSETYPEID: String = "IdOfExpenseTypeToEdit"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_expense_type)

        mbtnChangeColor = findViewById(R.id.btnChangeColor)
        mvwCurrentColor = findViewById(R.id.vwCurrentColor)
        mbtnSaveExpenseType = findViewById(R.id.btnSaveExpenseType)
        metEditText = findViewById(R.id.etExpenseTypeDescription)

        if (intent.hasExtra(INTENT_KEY_EXPENSETYPEID)) {
            mnExpenseTypeId = intent.extras.getLong(INTENT_KEY_EXPENSETYPEID)
            title = resources.getString(R.string.title_editexpensetype)
        }

        presenter = InsertExpenseTypePresenter(this)
        presenter?.loadExpenseTypeValues(mnExpenseTypeId)

        mbtnChangeColor?.setOnClickListener {
            ColorPickerDialogBuilder
                    .with(this)
                    .setTitle(resources.getString(R.string.chooseColor))
                    .initialColor(Color.WHITE)
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setOnColorSelectedListener { selectedColor -> mnColorSelected = selectedColor }
                    .setPositiveButton(resources.getString(R.string.ok)) { _, selectedColor, _ -> mvwCurrentColor?.setBackgroundColor(selectedColor) }
                    .setNegativeButton(resources.getString(R.string.cancel)) { _, _ -> }
                    .build()
                    .show()
        }
        mbtnSaveExpenseType?.setOnClickListener {
            presenter?.saveExpenseType(mnExpenseTypeId, metEditText?.text.toString(), mnColorSelected)
        }
    }

    override fun showSucessMessage() {
        MessageDialog.showToastMessage(this, resources.getString(R.string.sucessaction))
    }

    override fun showUnsucessMessage() {
        MessageDialog.showToastMessage(this, resources.getString(R.string.fillAreFields))
    }

    override fun setColor(anColorValue: Int) {
        mvwCurrentColor?.setBackgroundColor(anColorValue)
    }

    override fun setDecription(astrDescription: String) {
        metEditText?.setText(astrDescription)
    }
}
