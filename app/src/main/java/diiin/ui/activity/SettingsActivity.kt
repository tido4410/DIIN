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
import diiin.StaticCollections
import diiin.model.ExpenseType
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
            startActivity(Intent(this, InsertExpenseType::class.java))
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
        val lstExpenseType = StaticCollections.mappDataBuilder?.expenseTypeDao()?.all()
        if (lstExpenseType != null) mrvExpenseTypeList?.adapter = ExpenseTypeListAdapter(this, lstExpenseType as ArrayList<ExpenseType>)
    }
}
