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
import br.com.gbmoro.diiin.model.Expense
import br.com.gbmoro.diiin.ui.RVWithFLoatingButtonControl
import br.com.gbmoro.diiin.ui.activity.InsertExpenseActivity
import br.com.gbmoro.diiin.ui.adapter.ExpenseListAdapter
import java.util.*
import kotlin.collections.ArrayList

class FragmentExpensesList : Fragment() {

    private var mspMonthSelector: Spinner? = null
    private var mrvExpenseList: RecyclerView? = null
    private var mbtInsertExpense: FloatingActionButton? = null

    companion object {
        const val NAME = "FragmentExpensesList"
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_expenseslist, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mspMonthSelector = view?.findViewById(R.id.spMonthSelector)
        mrvExpenseList = view?.findViewById(R.id.rvExpenseList)
        mbtInsertExpense = view?.findViewById(R.id.btnaddExpense)
        mbtInsertExpense?.setOnClickListener {
            context.startActivity(Intent(context, InsertExpenseActivity::class.java))
        }

        val llManager = LinearLayoutManager(context)
        mrvExpenseList?.layoutManager = llManager
        if(mbtInsertExpense!=null)
            mrvExpenseList?.setOnTouchListener(RVWithFLoatingButtonControl(mbtInsertExpense!!))
    }

    override fun onResume() {
        super.onResume()
        loadExpenseList()
    }


    fun loadExpenseList() {
        val lstExpenses = StaticCollections.mastExpenses ?: return
        if(StaticCollections.mmtMonthSelected == null)
            mrvExpenseList?.adapter = ExpenseListAdapter(context, lstExpenses)
        else {
            val lstExpenseFiltered = ArrayList<Expense>()
            lstExpenses.forEach{
                val clCalendar = Calendar.getInstance()
                clCalendar.time = it.mdtDate
                if(clCalendar.get(Calendar.MONTH)== StaticCollections.mmtMonthSelected?.aid)
                    lstExpenseFiltered.add(it)
            }
            mrvExpenseList?.adapter = ExpenseListAdapter(context, lstExpenseFiltered)
        }
    }

    fun gettingSelectedExpenses() : ArrayList<Expense> {
        val lstExpenseFiltered = ArrayList<Expense>()
        (mrvExpenseList?.adapter as ExpenseListAdapter).mltExpenseList.forEach {
            val clCalendar = Calendar.getInstance()
            clCalendar.time = it.mdtDate
            if(clCalendar.get(Calendar.MONTH)== StaticCollections.mmtMonthSelected?.aid) {
                if(it.mbSelected) lstExpenseFiltered.add(it)
            }
        }
        return lstExpenseFiltered
    }



}