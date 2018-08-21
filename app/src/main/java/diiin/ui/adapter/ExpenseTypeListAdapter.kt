package diiin.ui.adapter

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import br.com.gbmoro.diiin.R
import diiin.StaticCollections
import diiin.model.ExpenseType


/**
 * This adapter is the manager of expense list.
 * @author Gabriel Moro
 */
class ExpenseTypeListAdapter(actxContext : Context, alstExpenseList: ArrayList<ExpenseType>)
    : RecyclerView.Adapter<ExpenseTypeListAdapter.ExpenseTypeListItemViewHolder>() {

    val mltExpenseTypeList: ArrayList<ExpenseType> = alstExpenseList
    private val mctContext: Context = actxContext

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseTypeListItemViewHolder {
        return ExpenseTypeListItemViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_settings_expense_item, parent, false), this)
    }

    override fun getItemCount(): Int {
        return mltExpenseTypeList.size
    }

    override fun onBindViewHolder(holder: ExpenseTypeListItemViewHolder, position: Int) {
        val expenseTypeItem = mltExpenseTypeList[position]
        holder.tvExpenseType.text = expenseTypeItem.mstrDescription
        holder.vwColorExpenseType.setBackgroundColor(Color.parseColor(expenseTypeItem.mstrColor))
        holder.nCurrentPosition = position
    }

    fun loadContent() {
        val lstExpenseType = StaticCollections.mappDataBuilder?.expenseTypeDao()?.all()
        if(lstExpenseType!=null) {
            mltExpenseTypeList.clear()
            mltExpenseTypeList.addAll(lstExpenseType)
            notifyDataSetChanged()
        }
    }

    class ExpenseTypeListItemViewHolder(avwView: View, adapter: ExpenseTypeListAdapter) : RecyclerView.ViewHolder(avwView), View.OnClickListener{

        val vwColorExpenseType: View = avwView.findViewById(R.id.vwColorRepresentation)
        val tvExpenseType: TextView = avwView.findViewById(R.id.tvExpenseType)
        val ibRemoveButton : ImageButton = avwView.findViewById(R.id.ibBtnRemove)
        var nCurrentPosition : Int? = null
        val madapter : ExpenseTypeListAdapter = adapter

        init {
            ibRemoveButton.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            if(nCurrentPosition!=null) {
                val expenseType = madapter.mltExpenseTypeList[nCurrentPosition!!]
                val lstExpenses = StaticCollections.mappDataBuilder?.expenseDao()?.all()
                val bIsItPossibleToClear = lstExpenses?.filter { it.mnExpenseType!! == expenseType.mnExpenseTypeID}?.count() == 0
                if(bIsItPossibleToClear) {
                    StaticCollections.mappDataBuilder?.expenseTypeDao()?.delete(expenseType)
                    madapter.loadContent()
                }
            }
        }

    }
}
