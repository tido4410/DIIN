package br.com.gbmoro.diiin.ui.fragments

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
import br.com.gbmoro.diiin.R
import br.com.gbmoro.diiin.StaticCollections
import br.com.gbmoro.diiin.model.Salary
import br.com.gbmoro.diiin.ui.RVWithFLoatingButtonControl
import br.com.gbmoro.diiin.ui.activity.InsertSalaryActivity
import br.com.gbmoro.diiin.ui.adapter.SalaryListAdapter
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

    fun gettingSelectedSalaries() : ArrayList<Salary> {
        val lstSalaryFiltered = ArrayList<Salary>()
        (mrvSalaryList?.adapter as SalaryListAdapter).mltSalaryList.forEach {
            val clCalendar = Calendar.getInstance()
            clCalendar.time = it.mdtDate
            if(clCalendar.get(Calendar.MONTH)== StaticCollections.mmtMonthSelected?.aid) {
                if(it.mbSelected) lstSalaryFiltered.add(it)
            }
        }
        return lstSalaryFiltered
    }

}