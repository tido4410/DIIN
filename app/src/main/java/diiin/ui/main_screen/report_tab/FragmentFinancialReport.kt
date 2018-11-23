package diiin.ui.main_screen.report_tab

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import br.com.gbmoro.diiin.R
import diiin.model.ExpenseType
import diiin.util.MathService
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import diiin.DindinApp
import diiin.ui.main_screen.RefreshData
import java.util.*

/**
 * Screen that shows to user the financial overview
 * @author Gabriel Moro
 * @since 11/09/2018
 * @version 1.0.9
 */
class FragmentFinancialReport : Fragment(), RefreshData, FinancialReportContract.View {

    private var mpcPieChart: PieChart? = null
    private var mrlChartItem: RelativeLayout? = null
    private var mrlPieChartContainer: RelativeLayout? = null
    private var mrlWalletPanel: RelativeLayout? = null
    private var mtvExpenseTotalValue: TextView? = null
    private var mtvSalaryTotalValue: TextView? = null
    private var mtvWalletTotalValue: TextView? = null

    /**
     * Chart item elements
     */
    private var mtvChartItemValue: TextView? = null
    private var mtvChartItemDate: TextView? = null
    private var mvwChartItemExpenseType: View? = null
    private var mllChartItemLinearLayout: LinearLayout? = null
    private var mtvChartItemExpenseType: TextView? = null
    private var mivChartItemButtonMenu: ImageView? = null
    private var presenter: FinancialReportContract.Presenter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_financialreport, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        mpcPieChart = view?.findViewById(R.id.pchart)
        mrlPieChartContainer = view?.findViewById(R.id.rlPieChart)
        mrlWalletPanel = view?.findViewById(R.id.rlWalletPanel)
        mtvExpenseTotalValue = view?.findViewById(R.id.tvExpenseTotalValue)
        mtvSalaryTotalValue = view?.findViewById(R.id.tvSalaryValue)
        mtvWalletTotalValue = view?.findViewById(R.id.tvWalletValue)
        mrlChartItem = view?.findViewById(R.id.rlChartItem)
        mtvChartItemValue = view?.findViewById(R.id.tvValue)
        mtvChartItemDate = view?.findViewById(R.id.tvDate)
        mtvChartItemExpenseType = view?.findViewById(R.id.tvExpenseType)
        mvwChartItemExpenseType = view?.findViewById(R.id.vwExpenseType)
        mllChartItemLinearLayout = view?.findViewById(R.id.llLine2)
        mivChartItemButtonMenu = view?.findViewById(R.id.ivMenuOption)

        presenter = FinancialReportPresenter(this)

        mpcPieChart?.setUsePercentValues(true)
        mpcPieChart?.description?.isEnabled = false
        mpcPieChart?.setExtraOffsets(2f, 5f, 2f, 2f)
        mpcPieChart?.dragDecelerationFrictionCoef = 0.95f
        mpcPieChart?.rotationAngle = 0f
        mpcPieChart?.isRotationEnabled = false
        mpcPieChart?.isDrawHoleEnabled = true
        mpcPieChart?.transparentCircleRadius = 10f
        mpcPieChart?.holeRadius = 7f
        mpcPieChart?.setHoleColor(ContextCompat.getColor(context, R.color.whiteColor))

        refresh()
    }

    @SuppressLint("CheckResult")
    override fun onResume() {
        super.onResume()

        mrlChartItem?.visibility = RelativeLayout.GONE
        mrlWalletPanel?.visibility = RelativeLayout.VISIBLE
    }

    /**
     * Refresh method is used to show the current data state.
     */
    override fun refresh() {
        val mnMonthSelected = DindinApp.mmtMonthSelected ?: return
        val nYear = DindinApp.mnYearSelected ?: return
        presenter?.dispareChartConstructor(mnMonthSelected, nYear)
        presenter?.loadDemonstrativePanel(mnMonthSelected, nYear)
    }

    /**
     * Draw the pie chart according to pie entries list and colors arguments
     */
    override fun drawPieChart(alstPieEntries: ArrayList<PieEntry>, alstColors: ArrayList<Int>) {
        val dataSet = PieDataSet(alstPieEntries, "")
        dataSet.sliceSpace = 2f
        dataSet.formLineWidth = 120f
        dataSet.colors = alstColors
        val dataPie = PieData(dataSet)
        dataPie.setValueFormatter(PercentFormatter())
        dataPie.setValueTextSize(14f)
        dataPie.setValueTextColor(Color.TRANSPARENT)
        mpcPieChart?.legend?.textColor = ContextCompat.getColor(activity, R.color.whiteColor)
        mpcPieChart?.legend?.textSize = 12f
        mpcPieChart?.legend?.isWordWrapEnabled = true
        mpcPieChart?.setDrawEntryLabels(false)
        mpcPieChart?.data = dataPie
        mpcPieChart?.highlightValues(null)
        mpcPieChart?.invalidate()

        mpcPieChart?.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onNothingSelected() {
                mrlChartItem?.visibility = RelativeLayout.GONE
                mrlWalletPanel?.visibility = RelativeLayout.VISIBLE
            }

            override fun onValueSelected(e: Entry, h: Highlight?) {
                val pieEntry = e as PieEntry
                presenter?.onClickInPiePiece(pieEntry)
            }
        })
    }

    /**
     * Show the cell with more details according the piece selected for user.
     */
    override fun showChartItemCard(aetExpenseType: ExpenseType, asValue: Float) {
        mtvChartItemDate?.visibility = TextView.GONE
        mivChartItemButtonMenu?.visibility = ImageView.GONE
        mtvChartItemValue?.text = MathService.formatFloatToCurrency(asValue)
        mtvChartItemExpenseType?.text = aetExpenseType.mstrDescription
        mvwChartItemExpenseType?.setBackgroundColor(Color.parseColor(aetExpenseType.mstrColor))
        mrlChartItem?.visibility = RelativeLayout.VISIBLE
        mrlWalletPanel?.visibility = RelativeLayout.GONE
    }

    /**
     * Change the salary text value
     */
    override fun setSalaryTotal(astrValue: String) {
        mtvSalaryTotalValue?.text = astrValue
    }

    /**
     * Change the expense text value
     */
    override fun setExpenseTotal(astrValue: String) {
        mtvExpenseTotalValue?.text = astrValue
    }

    /**
     * Change the wallet text value
     */
    override fun setWalletTotal(astrValue: String) {
        mtvWalletTotalValue?.text = astrValue
    }

    /**
     * Hide the chart item card.
     */
    override fun hideChartItem() {
        mrlChartItem?.visibility = RelativeLayout.GONE
    }


}