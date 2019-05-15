package diiin.ui.main_screen.expenselist_tab

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import br.com.gbmoro.diiin.R
import diiin.DindinApp
import diiin.model.Expense
import diiin.ui.RVWithFLoatingButtonControl
import diiin.ui.insert_expense_screen.InsertExpenseActivity
import diiin.ui.main_screen.RefreshData
import kotlin.collections.ArrayList

/**
 * Screen that shows to user the expenses filter and list
 *
 * @author Gabriel Moro
 * @since 11/09/2018
 * @version 1.0.9
 */
class FragmentExpensesList : androidx.fragment.app.Fragment(), RefreshData, ExpenseListContract.View {

    private var mspMonthSelector: Spinner? = null
    private var mrvExpenseList: RecyclerView? = null
    private var mbtInsertExpense: FloatingActionButton? = null
    private var presenter: ExpenseListContract.Presenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_expenseslist, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mspMonthSelector = view.findViewById(R.id.spMonthSelector)
        mrvExpenseList = view.findViewById(R.id.rvExpenseList)
        mbtInsertExpense = view.findViewById(R.id.btnaddExpense)
        mbtInsertExpense?.setOnClickListener {
            activity?.startActivity(Intent(activity, InsertExpenseActivity::class.java))
        }

        val llManager = LinearLayoutManager(context)
        mrvExpenseList?.layoutManager = llManager
        loadExpenseListAdapter(ArrayList())
        if (mbtInsertExpense != null)
            mrvExpenseList?.setOnTouchListener(RVWithFLoatingButtonControl(mbtInsertExpense!!))

        presenter = FragmentExpenseListPresenter(this)
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    /**
     * Refresh is used to restart the adapter list content with the current state.
     */
    override fun refresh() {
        val mnMonthSelected = DindinApp.mmtMonthSelected ?: return
        val mnYearSelected = DindinApp.mnYearSelected ?: return
        presenter?.loadExpenses(mnMonthSelected, mnYearSelected)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        mrvExpenseList?.adapter?.notifyDataSetChanged()
    }

    /**
     * Change the adapter list.
     */
    override fun loadExpenseListAdapter(alstExpenses: ArrayList<Expense>) {
        val elAdapter = ExpenseListAdapter(
                object : ExpenseListAdapterContract {
                    override fun onRemoveItem() =
                            (activity!!.application as DindinApp)
                                    .bus()
                                    .send(true)

                    override fun currentContext(): Context = context!!
                }, alstExpenses)
        mrvExpenseList?.adapter = elAdapter
    }


}