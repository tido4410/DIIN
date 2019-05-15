package diiin.ui.settings_screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.EditText
import br.com.gbmoro.diiin.R
import diiin.model.ExpenseType
import diiin.ui.RVWithFLoatingButtonControl
import diiin.ui.insert_expense_type_screen.InsertExpenseTypeActivity
import diiin.util.MessageDialog
import java.util.*

/**
 * View of the SettingsScreen.
 * @author Gabriel Moro
 * @since 11/09/2018
 * @version 1.0.9
 */
class SettingsActivity : AppCompatActivity(), SettingsScreenContract.View {

    private var mrvExpenseTypeList: RecyclerView? = null
    private var mbtInsertExpenseType: FloatingActionButton? = null
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

    /**
     * Call the screen to insert a new expense type
     */
    override fun callExpenseTypeInsertScreen() {
        startActivity(Intent(this, InsertExpenseTypeActivity::class.java))
    }

    /**
     * Update adapter list with the current expense types list.
     */
    override fun setExpenseTypeList(alstArrayList: ArrayList<ExpenseType>) {
        mrvExpenseTypeList?.adapter = ExpenseTypeListAdapter(object : ExpenseTypeListAdapterContract {
            override fun currentContext(): Context {
                return this@SettingsActivity
            }
        }, alstArrayList)
    }

    /**
     * Change the year text value.
     */
    override fun setYear(astrYear: String) {
        metYear?.setText(astrYear)
    }
}
