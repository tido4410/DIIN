package diiin.ui.activity

import android.annotation.SuppressLint
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

class SettingsActivity : AppCompatActivity() {

    private var mrvExpenseTypeList: RecyclerView? = null
            var mbtInsertExpenseType: FloatingActionButton? = null
    private var metYear : EditText? = null
    private var mbtnSaveYear : Button? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        metYear = findViewById(R.id.etYearSelected)
        mbtnSaveYear = findViewById(R.id.btnToSaveTheYearSelected)
        mbtInsertExpenseType = findViewById(R.id.btnaddExpenseType)
        mrvExpenseTypeList = findViewById(R.id.rvExpenseTypeList)

        val llManager = LinearLayoutManager(this)
        mrvExpenseTypeList?.layoutManager = llManager

        mbtInsertExpenseType?.setOnClickListener {
            startActivity(Intent(this, InsertExpenseTypeActivity::class.java))
        }
        mbtnSaveYear?.setOnClickListener {
            MessageDialog.showMessageDialog(this,
                    resources.getString(R.string.msgAreYouSure),
                    DialogInterface.OnClickListener { adialog, _ ->
                        val nYear = metYear?.text?.toString()?.toIntOrNull()
                        if(nYear != null) {
                            SelectionSharedPreferences.insertYearSelectPreference(this, nYear)
                        }
                    },
                    DialogInterface.OnClickListener { adialog, _ ->
                        adialog.dismiss()
                    })
        }
        if(mbtInsertExpenseType!=null)
            mrvExpenseTypeList?.setOnTouchListener(RVWithFLoatingButtonControl(mbtInsertExpenseType!!))
    }

    override fun onResume() {
        super.onResume()
        loadExpenseTypes()
        metYear?.setText(StaticCollections.mnYearSelected.toString())
    }

    private fun loadExpenseTypes() {
        DindinApp.mlcmDataManager?.getAllExpenseTypeObjects(object : LocalCacheManager.DatabaseCallBack {
            override fun onExpensesLoaded(alstExpenses: List<Expense>) { }
            override fun onExpenseTypeLoaded(alstExpensesType: List<ExpenseType>) {
                mrvExpenseTypeList?.adapter = ExpenseTypeListAdapter(applicationContext, ArrayList(alstExpensesType))
            }
            override fun onSalariesLoaded(alstSalaries: List<Salary>) { }
            override fun onExpenseIdReceived(aexpense: Expense) { }
            override fun onExpenseTypeColorReceived(astrColor: String) { }
            override fun onExpenseTypeDescriptionReceived(astrDescription: String) { }
            override fun onExpenseTypeIDReceived(anID: Long?) { }
            override fun onSalaryObjectByIdReceived(aslSalary: Salary) { }
        })
    }
}
