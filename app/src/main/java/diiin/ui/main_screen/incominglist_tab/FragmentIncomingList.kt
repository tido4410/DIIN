package diiin.ui.main_screen.incominglist_tab

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import br.com.gbmoro.diiin.R
import diiin.DindinApp
import diiin.model.Incoming
import diiin.ui.RVWithFLoatingButtonControl
import diiin.ui.insert_incoming_screen.InsertIncomingActivity
import diiin.ui.main_screen.RefreshData
import java.util.*

/**
 * Screen that shows to user the incomes filter and salary list
 * @author Gabriel Moro
 * @since 11/09/2018
 * @version 1.0.9
 */
class FragmentIncomingList : androidx.fragment.app.Fragment(), RefreshData, SalaryListContract.View {

    private var mspMonthSelector: Spinner? = null
    private var mrvSalaryList: RecyclerView? = null
    var mbtnInsertSalary: FloatingActionButton? = null
    private var presenter: SalaryListContract.Presenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_salarylist, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mspMonthSelector = view.findViewById(R.id.spMonthSelector)
        mrvSalaryList = view.findViewById(R.id.rvSalaryList)
        mbtnInsertSalary = view.findViewById(R.id.btnaddSalary)
        mbtnInsertSalary?.setOnClickListener {
            activity?.startActivity(Intent(activity, InsertIncomingActivity::class.java))
        }

        presenter = FragmentIncomingPresenter(this)

        val llManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        mrvSalaryList?.layoutManager = llManager
        loadSalaryListAdapter(ArrayList())
        if (mbtnInsertSalary != null)
            mrvSalaryList?.setOnTouchListener(RVWithFLoatingButtonControl(mbtnInsertSalary!!))
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    /**
     * This method refresh to current data state
     */
    override fun refresh() {
        val mnMonthSelected = DindinApp.mmtMonthSelected ?: return
        val mnYear = DindinApp.mnYearSelected ?: return

        presenter?.loadSalaries(mnMonthSelected, mnYear)

    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        mrvSalaryList?.adapter?.notifyDataSetChanged()
    }

    /**
     * Load the adapter content.
     */
    override fun loadSalaryListAdapter(alstIncoming: ArrayList<Incoming>) {
        val slAdapter = IncomingListAdapter(alstIncoming, object : IncomingListAdapterContract {
            override fun onRemoveItem() =
                    (activity?.application as DindinApp)
                            .bus()
                            .send(true)

            override fun currentContext(): Context = context!!
        })
        mrvSalaryList?.adapter = slAdapter
    }

}