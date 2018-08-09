package diiin.ui.adapter

import android.content.Context
import android.content.res.Configuration
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import br.com.gbmoro.diiin.R
import diiin.StaticCollections
import diiin.model.Expense
import diiin.util.MathService
import java.util.*

/**
 * This adapter is the manager of expense list.
 * @author Gabriel Moro
 */
class ExpenseListAdapter(actxContext : Context, alstExpenseList: ArrayList<Expense>)
    : RecyclerView.Adapter<ExpenseListAdapter.ExpenseListItemViewHolder>() {

    val mltExpenseList: ArrayList<Expense> = alstExpenseList
    private val mctContext: Context = actxContext

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseListItemViewHolder {
        return ExpenseListItemViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_expenseslist_item, parent, false))
    }

    override fun getItemCount(): Int {
        return mltExpenseList.size
    }

    override fun onBindViewHolder(holder: ExpenseListItemViewHolder, position: Int) {
        val expenseItem = mltExpenseList[position]

        if (expenseItem.msValue != null)
            holder.tvValue.text = MathService.formatFloatToCurrency(expenseItem.msValue!!)

        holder.tvDescription.text = expenseItem.mstrDescription

        holder.tvDate.text = expenseItem.mstrDate

        val strDescription = StaticCollections.mappDataBuilder?.expenseTypeDao()?.getDescription(expenseItem.mnID)

        if (expenseItem.mstrDescription.isEmpty()) {
            holder.tvExpenseType.text = strDescription
            holder.tvDescription.text = ""
        } else {
            holder.tvExpenseType.text = expenseItem.mstrDescription
            holder.tvDescription.text = strDescription?.toUpperCase()
        }


//        if (expenseItem.metType != null) {
//            holder.vwExpenseType.setBackgroundColor(expenseItem.metType.backgroundColor(mctContext))
//            holder.ivExpenseType.setImageResource(expenseItem.metType.imageIconId())
//            holder.tvValue.setTextColor(expenseItem.metType.backgroundColor(mctContext))
//        }

        holder.llLine2.visibility = LinearLayout.GONE

        if (mctContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            holder.llLine2.visibility = LinearLayout.VISIBLE

        if (StaticCollections.mbEditMode) {
            holder.ivReorder.visibility = ImageView.VISIBLE
        } else {
            holder.ivReorder.visibility = ImageView.GONE
        }
    }

    class ExpenseListItemViewHolder(avwView: View) : RecyclerView.ViewHolder(avwView) {
        val ivExpenseType: ImageView = avwView.findViewById(R.id.ivExpenseType)
        val tvExpenseType: TextView = avwView.findViewById(R.id.tvExpenseType)
        val tvDescription: TextView = avwView.findViewById(R.id.tvDescription)
        val tvDate: TextView = avwView.findViewById(R.id.tvDate)
        val tvValue: TextView = avwView.findViewById(R.id.tvValue)
        val vwExpenseType: View = avwView.findViewById(R.id.vwExpenseType)
        val ivReorder: ImageView = avwView.findViewById(R.id.ivReorder)
        val llLine2: LinearLayout = avwView.findViewById(R.id.llLine2)
    }
}