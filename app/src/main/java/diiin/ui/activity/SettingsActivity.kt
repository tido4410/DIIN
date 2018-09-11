package diiin.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.EditText
import br.com.gbmoro.diiin.R
import diiin.DindinApp
import diiin.StaticCollections
import diiin.dao.LocalCacheManager
import diiin.model.Expense
import diiin.model.ExpenseType
import diiin.model.Salary
import diiin.ui.RVWithFLoatingButtonControl
import diiin.ui.adapter.ExpenseTypeListAdapter
import diiin.util.MessageDialog
import diiin.util.SelectionSharedPreferences
import java.util.*

interface SettingsScreenContract {
    interface View {
        fun callExpenseTypeInsertScreen()
        fun setExpenseTypeList(alstArrayList: ArrayList<ExpenseType>)
        fun setYear(astrYear: String)
    }

    interface Presenter {
        fun saveYear(actxContext: Context, astrYear: String)
        fun loadYear()
        fun loadExpenseTypes()
    }
}

class SettingsPresenter(avwView: SettingsScreenContract.View) : SettingsScreenContract.Presenter {
    private val view: SettingsScreenContract.View = avwView

    override fun saveYear(actxContext: Context, astrYear: String) {
        val nYear = astrYear.toIntOrNull() ?: return
        SelectionSharedPreferences.insertYearSelectPreference(actxContext, nYear)
    }

    override fun loadYear() {
        val strCurrentYear = StaticCollections.mnYearSelected.toString()
        view.setYear(strCurrentYear)
    }

    override fun loadExpenseTypes() {
        DindinApp.mlcmDataManager?.getAllExpenseTypeObjects(object : LocalCacheManager.DatabaseCallBack {
            override fun onExpensesLoaded(alstExpenses: List<Expense>) {}
            override fun onExpenseTypeLoaded(alstExpensesType: List<ExpenseType>) {
                view.setExpenseTypeList(ArrayList(alstExpensesType))
                /**
                 * Update the global structure used to represent
                 * the expense types hashmap
                 */
                DindinApp.mhmExpenseType = HashMap()
                alstExpensesType.forEach { expense ->
                    if (expense.mnExpenseTypeID != null)
                        DindinApp.mhmExpenseType?.put(expense.mnExpenseTypeID!!, expense)
                }
            }

            override fun onSalariesLoaded(alstSalaries: List<Salary>) {}
            override fun onExpenseIdReceived(aexpense: Expense) {}
            override fun onExpenseTypeColorReceived(astrColor: String) {}
            override fun onExpenseTypeDescriptionReceived(astrDescription: String) {}
            override fun onExpenseTypeIDReceived(anID: Long?) {}
            override fun onSalaryObjectByIdReceived(aslSalary: Salary) {}
        })
    }

}

class SettingsActivity : AppCompatActivity(), SettingsScreenContract.View {

    private var mrvExpenseTypeList: RecyclerView? = null
    var mbtInsertExpenseType: FloatingActionButton? = null
    private var metYear: EditText? = null
    private var mbtnSaveYear: Button? = null
    private var presenter: SettingsScreenContract.Presenter? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        metYear = findViewById(R.id.etYearSelected)
        mbtnSaveYear = findViewById(R.id.btnToSaveTheYearSelected)
        mbtInsertExpenseType = findViewById(R.id.btnaddExpenseType)
        mrvExpenseTypeList = findViewById(R.id.rvExpenseTypeList)

        presenter = SettingsPresenter(this)

        val llManager = LinearLayoutManager(this)
        mrvExpenseTypeList?.layoutManager = llManager

        mbtInsertExpenseType?.setOnClickListener {
            callExpenseTypeInsertScreen()
        }
        mbtnSaveYear?.setOnClickListener {
            MessageDialog.showMessageDialog(this,
                    resources.getString(R.string.msgAreYouSure),
                    DialogInterface.OnClickListener { adialog, _ ->
                        val strYear = metYear?.text.toString()
                        presenter?.saveYear(this, strYear)
                    },
                    DialogInterface.OnClickListener { adialog, _ ->
                        adialog.dismiss()
                    })
        }
        if (mbtInsertExpenseType != null)
            mrvExpenseTypeList?.setOnTouchListener(RVWithFLoatingButtonControl(mbtInsertExpenseType!!))
    }

    override fun onResume() {
        super.onResume()
        presenter?.loadExpenseTypes()
        presenter?.loadYear()
    }

    override fun callExpenseTypeInsertScreen() {
        startActivity(Intent(this, InsertExpenseTypeActivity::class.java))
    }

    override fun setExpenseTypeList(alstArrayList: ArrayList<ExpenseType>) {
        mrvExpenseTypeList?.adapter = ExpenseTypeListAdapter(this, alstArrayList)
    }

    override fun setYear(astrYear: String) {
        metYear?.setText(astrYear)
    }
}
