package com.example.gabrielbronzattimoro.diiin.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import com.example.gabrielbronzattimoro.diiin.R
import com.example.gabrielbronzattimoro.diiin.StaticCollections
import com.example.gabrielbronzattimoro.diiin.model.Salary
import com.example.gabrielbronzattimoro.diiin.ui.RVWithFLoatingButtonControl
import com.example.gabrielbronzattimoro.diiin.ui.activity.InsertSalaryActivity
import com.example.gabrielbronzattimoro.diiin.ui.adapter.SalaryListAdapter
import java.util.*

class FragmentSalaryList : Fragment() {

    private var mspMonthSelector: Spinner? = null
    private var mrvSalaryList: RecyclerView? = null
    private var mbtnInsertSalary: FloatingActionButton? = null

    companion object {
        const val NAME = "FragmentSalaryList"
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_salarylist, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
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
        if(mbtnInsertSalary!=null)
            mrvSalaryList?.setOnTouchListener(RVWithFLoatingButtonControl(mbtnInsertSalary!!))
    }

    override fun onResume() {
        super.onResume()
        loadSalaryList()
    }

    fun loadSalaryList() {
        val lstSalary = StaticCollections.mastSalary ?: return
        if(StaticCollections.mmtMonthSelected == null)
            mrvSalaryList?.adapter = SalaryListAdapter(lstSalary, context)
        else {
            val lstSalaryFiltered = ArrayList<Salary>()
            lstSalary.forEach{
                val clCalendar = Calendar.getInstance()
                clCalendar.time = it.mdtDate
                if(clCalendar.get(Calendar.MONTH)== StaticCollections.mmtMonthSelected?.aid)
                    lstSalaryFiltered.add(it)
            }
            mrvSalaryList?.adapter = SalaryListAdapter(lstSalaryFiltered, context)
        }
    }

}