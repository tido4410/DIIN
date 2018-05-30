package br.com.gbmoro.diiin.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import br.com.gbmoro.diiin.R
import br.com.gbmoro.diiin.model.Expense
import br.com.gbmoro.diiin.ui.ActivityDeleteCellsFromList
import br.com.gbmoro.diiin.util.MathService

/**
 * This adapter is the manager of expense list.
 * @author Gabriel Moro
 */
class ExpenseListAdapter(actxContext : Context, alstExpenseList: ArrayList<Expense>) : RecyclerView.Adapter<ExpenseListAdapter.ExpenseListItemViewHolder>() {

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

        if(expenseItem.msValue!=null)
            holder.tvValue.text = MathService.formatFloatToCurrency(expenseItem.msValue!!)
        holder.tvDescription.text = expenseItem.msrDescription
        if(expenseItem.mdtDate!=null)
            holder.tvDate.text = MathService.calendarTimeToString(expenseItem.mdtDate!!)
        holder.tvExpenseType.text = expenseItem.metType?.description(mctContext)?.toUpperCase()
        val nExpenseBkgColor = expenseItem.metType?.backgroundColor(mctContext)
        val nExpenseFontColor = expenseItem.metType?.fontColor(mctContext)
        if(nExpenseBkgColor!=null && nExpenseFontColor != null) {
            holder.tvExpenseType.setBackgroundColor(nExpenseBkgColor)
            holder.tvExpenseType.setTextColor(nExpenseFontColor)
        }
        holder.itemView?.setOnLongClickListener {
            if(holder.ivCheckedImage.visibility == ImageView.VISIBLE) {
                expenseItem.mbSelected = false
                holder.ivCheckedImage.visibility = ImageView.GONE
                (mctContext as ActivityDeleteCellsFromList).hideMenu()
            } else {
                holder.ivCheckedImage.visibility = ImageView.VISIBLE
                expenseItem.mbSelected = true
                (mctContext as ActivityDeleteCellsFromList).showMenu()
            }
            true
        }
    }


    class ExpenseListItemViewHolder(avwView: View) : RecyclerView.ViewHolder(avwView) {
        val tvValue: TextView = avwView.findViewById(R.id.tvValue)
        val tvExpenseType: TextView = avwView.findViewById(R.id.tvExpenseType)
        val tvDescription: TextView = avwView.findViewById(R.id.tvDescription)
        val tvDate: TextView = avwView.findViewById(R.id.tvDate)
        val ivCheckedImage : ImageView = avwView.findViewById(R.id.ivChecked)
    }
}