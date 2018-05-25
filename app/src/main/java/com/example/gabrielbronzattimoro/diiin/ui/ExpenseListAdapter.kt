package com.example.gabrielbronzattimoro.diiin.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.gabrielbronzattimoro.diiin.R
import com.example.gabrielbronzattimoro.diiin.model.Expense
import com.example.gabrielbronzattimoro.diiin.util.MathService

class ExpenseListAdapter(actxContext : Context, alstExpenseList: ArrayList<Expense>) : RecyclerView.Adapter<ExpenseListAdapter.ExpenseListItemViewHolder>() {

    private val mlstExpenseList: ArrayList<Expense> = alstExpenseList
    private val mctxContext: Context = actxContext

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseListItemViewHolder {
        return ExpenseListItemViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_expenseslist_item, parent, false))
    }

    override fun getItemCount(): Int {
        return mlstExpenseList.size
    }

    override fun onBindViewHolder(holder: ExpenseListItemViewHolder, position: Int) {
        val expenseItem = mlstExpenseList[position]

        if(expenseItem.msValue!=null)
            holder.tvValue.text = MathService.formatFloatToCurrency(expenseItem.msValue!!)
        holder.tvDescription.text = expenseItem.mstrDescription
        if(expenseItem.mdtDate!=null)
            holder.tvDate.text = MathService.calendarTimeToString(expenseItem.mdtDate!!)
        holder.tvExpenseType.text = expenseItem.mexpenseType?.description(mctxContext)
        val nExpenseBkgColor = expenseItem.mexpenseType?.backgroundColor(mctxContext)
        val nExpenseFontColor = expenseItem.mexpenseType?.fontColor(mctxContext)
        if(nExpenseBkgColor!=null && nExpenseFontColor != null) {
            holder.tvExpenseType.setBackgroundColor(nExpenseBkgColor)
            holder.tvExpenseType.setTextColor(nExpenseFontColor)
        }
    }


    class ExpenseListItemViewHolder(avwView: View) : RecyclerView.ViewHolder(avwView) {
        val tvValue: TextView = avwView.findViewById(R.id.tvValue)
        val tvExpenseType: TextView = avwView.findViewById(R.id.tvExpenseType)
        val tvDescription: TextView = avwView.findViewById(R.id.tvDescription)
        val tvDate: TextView = avwView.findViewById(R.id.tvDate)
    }
}