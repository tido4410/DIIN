package com.example.gabrielbronzattimoro.diiin.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import com.example.gabrielbronzattimoro.diiin.R
import com.example.gabrielbronzattimoro.diiin.StaticCollections
import com.example.gabrielbronzattimoro.diiin.model.Salary
import java.util.*

class FragmentSalaryList : Fragment() {

    private var mspMonthSelector: Spinner? = null
    private var mrvSalaryList: RecyclerView? = null
    private var mbtnInsertSalary: Button? = null

    companion object {
        val TAGNAME = "FragmentSalaryList"
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_salarylist, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mspMonthSelector = view?.findViewById(R.id.spMonthSelector)
        mrvSalaryList = view?.findViewById(R.id.rvSalaryList)
        mbtnInsertSalary = view?.findViewById(R.id.btnaddSalary)
        mbtnInsertSalary?.setOnClickListener {
            context.startActivity(Intent(context, InsertSalaryActivity::class.java))
        }

        val llManager = LinearLayoutManager(context)
        mrvSalaryList?.layoutManager = llManager
    }

    override fun onResume() {
        super.onResume()
        loadSalaryList()
    }

    fun loadSalaryList() {
        val lstSalary = StaticCollections.mlstSalary ?: return
        if(StaticCollections.mmtMonthSelected == null)
            mrvSalaryList?.adapter = SalaryListAdapter(context, lstSalary)
        else {
            val lstSalaryFiltered = ArrayList<Salary>()
            lstSalary.forEach{
                val clCalendar = Calendar.getInstance()
                clCalendar.time = it.mdtDate
                if(clCalendar.get(Calendar.MONTH)== StaticCollections.mmtMonthSelected?.aid)
                    lstSalaryFiltered.add(it)
            }
            mrvSalaryList?.adapter = SalaryListAdapter(context, lstSalaryFiltered)
        }
    }

}