package diiin.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import br.com.gbmoro.diiin.R
import diiin.StaticCollections
import diiin.model.ExpenseType
import diiin.ui.adapter.ExpenseTypeListAdapter

class SettingsActivity : AppCompatActivity() {

    private var mrvExpenseTypeList: RecyclerView? = null
            var mbtInsertExpenseType: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        mbtInsertExpenseType = findViewById(R.id.btnaddExpenseType)
        mrvExpenseTypeList = findViewById(R.id.rvExpenseTypeList)

        val llManager = LinearLayoutManager(this)
        mrvExpenseTypeList?.layoutManager = llManager

        val lstExpenseType = StaticCollections.mappDataBuilder?.expenseTypeDao()?.all()
        if(lstExpenseType!=null) mrvExpenseTypeList?.adapter = ExpenseTypeListAdapter(this, lstExpenseType as ArrayList<ExpenseType>)
    }
}
