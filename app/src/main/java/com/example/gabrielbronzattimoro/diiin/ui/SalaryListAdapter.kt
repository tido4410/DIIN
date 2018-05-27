package com.example.gabrielbronzattimoro.diiin.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.gabrielbronzattimoro.diiin.R
import com.example.gabrielbronzattimoro.diiin.model.Salary
import com.example.gabrielbronzattimoro.diiin.util.MathService


class SalaryListAdapter(actxContext : Context, alstSalaryList: ArrayList<Salary>)  : RecyclerView.Adapter<SalaryListAdapter.SalaryListItemViewHolder>() {

    private val mlstSalaryList: ArrayList<Salary> = alstSalaryList
    private val mctxContext: Context = actxContext

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SalaryListItemViewHolder? {
        return SalaryListItemViewHolder(LayoutInflater.from(parent?.context)
                .inflate(R.layout.fragment_salarylist_item, parent, false))
    }

    override fun getItemCount(): Int {
        return mlstSalaryList.size
    }

    override fun onBindViewHolder(holder: SalaryListItemViewHolder?, position: Int) {
        val salaryItem = mlstSalaryList[position]

        if(salaryItem.mdtDate!=null)
            holder?.tvDate?.text = MathService.calendarTimeToString(salaryItem.mdtDate!!)
        if(salaryItem.msValue!=null)
            holder?.tvValue?.text = MathService.formatFloatToCurrency(salaryItem.msValue!!)
        holder?.tvSource?.text = salaryItem.mstrSource
    }


    class SalaryListItemViewHolder(avwView : View) : RecyclerView.ViewHolder(avwView) {
        val tvValue: TextView = avwView.findViewById(R.id.tvValue)
        val tvSource: TextView = avwView.findViewById(R.id.tvSource)
        val tvDate: TextView = avwView.findViewById(R.id.tvDate)
    }
}