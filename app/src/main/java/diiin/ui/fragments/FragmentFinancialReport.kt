package diiin.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import br.com.gbmoro.diiin.R
import diiin.StaticCollections
import diiin.model.Expense
import diiin.model.ExpenseType
import diiin.util.MathService
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import java.util.*


class FragmentFinancialReport : Fragment() {

    companion object {
        const val NAME = "FragmentFinancialReport"
    }

    private var mpcPieChart: PieChart? = null
    private var mrlPieChartContainer : RelativeLayout? = null
    private var mtvExpenseTotalValue : TextView? = null
    private var mtvSalaryTotalValue : TextView? = null
    private var mtvWalletTotalValue : TextView? = null



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_financialreport, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        mpcPieChart = view?.findViewById(R.id.pchart)
        mrlPieChartContainer = view?.findViewById(R.id.rlPieChart)
        mtvExpenseTotalValue = view?.findViewById(R.id.tvExpenseTotalValue)
        mtvSalaryTotalValue = view?.findViewById(R.id.tvSalaryValue)
        mtvWalletTotalValue = view?.findViewById(R.id.tvWalletValue)

        mpcPieChart?.setUsePercentValues(true)
        mpcPieChart?.description?.isEnabled = false
        mpcPieChart?.setExtraOffsets(2f, 5f, 2f, 2f)
        mpcPieChart?.dragDecelerationFrictionCoef = 0.95f
        mpcPieChart?.rotationAngle = 0f
        mpcPieChart?.isRotationEnabled = true
        mpcPieChart?.isDrawHoleEnabled = true
        mpcPieChart?.transparentCircleRadius = 10f
        mpcPieChart?.holeRadius = 7f
        mpcPieChart?.setHoleColor(ContextCompat.getColor(context,R.color.whiteColor))

        loadChartData()
    }

    fun loadChartData() {

        val lstExpenses : ArrayList<Expense> = StaticCollections.mastExpenses ?: return

        var sSumTotal = 0f
        var sumTotalFood = 0f
        var sumTotalTransport = 0f
        var sumTotalPhone = 0f
        var sumTotalPets = 0f
        var sumTotalEducation = 0f
        var sumTotalHealth = 0f
        var sumTotalFun = 0f
        var sumTotalRent = 0f
        var sumTotalTravel = 0f
        var sumTotalOthers = 0f

        lstExpenses.forEach{
            val clCalendar = Calendar.getInstance()
            clCalendar.time = it.mdtDate
            if(clCalendar.get(Calendar.MONTH)== StaticCollections.mmtMonthSelected?.aid) {
                if(it.msValue!=null)
                    sSumTotal += it.msValue!!
                when(it.metType) {
                    ExpenseType.FOOD -> {
                        if(it.msValue != null)
                            sumTotalFood += it.msValue!!
                    }
                    ExpenseType.TRANSPORT -> {
                        if(it.msValue != null)
                            sumTotalTransport+=it.msValue!!
                    }
                    ExpenseType.PHONE -> {
                        if(it.msValue != null)
                            sumTotalPhone+=it.msValue!!
                    }
                    ExpenseType.PETS -> {
                        if(it.msValue != null)
                            sumTotalPets+=it.msValue!!
                    }
                    ExpenseType.EDUCATION -> {
                        if(it.msValue != null)
                            sumTotalEducation+=it.msValue!!
                    }
                    ExpenseType.HEALTH -> {
                        if(it.msValue != null)
                            sumTotalHealth+=it.msValue!!
                    }
                    ExpenseType.FUN -> {
                        if(it.msValue != null)
                            sumTotalFun+=it.msValue!!
                    }
                    ExpenseType.RENT -> {
                        if(it.msValue != null)
                            sumTotalRent+=it.msValue!!
                    }
                    ExpenseType.TRAVEL -> {
                        if(it.msValue != null)
                            sumTotalTravel+=it.msValue!!
                    }
                    ExpenseType.OTHERS -> {
                        if(it.msValue != null)
                            sumTotalOthers+=it.msValue!!
                    }
                }
            }
        }

        val lstEntries = ArrayList<PieEntry>()
        val lstColors = ArrayList<Int>()

        if(sumTotalFood > 0) {
            lstEntries.add(initPieEntry(sumTotalFood / sSumTotal, ExpenseType.FOOD.description(context)))
            lstColors.add(ExpenseType.FOOD.backgroundColor(context))
        }
        if(sumTotalTransport > 0) {
            lstEntries.add(initPieEntry(sumTotalTransport / sSumTotal, ExpenseType.TRANSPORT.description(context)))
            lstColors.add(ExpenseType.TRANSPORT.backgroundColor(context))
        }
        if(sumTotalPhone > 0) {
            lstEntries.add(initPieEntry(sumTotalPhone / sSumTotal, ExpenseType.PHONE.description(context)))
            lstColors.add(ExpenseType.PHONE.backgroundColor(context))
        }
        if(sumTotalPets > 0) {
            lstEntries.add(initPieEntry(sumTotalPets / sSumTotal, ExpenseType.PETS.description(context)))
            lstColors.add(ExpenseType.PETS.backgroundColor(context))
        }
        if(sumTotalEducation > 0) {
            lstEntries.add(initPieEntry(sumTotalEducation / sSumTotal, ExpenseType.EDUCATION.description(context)))
            lstColors.add(ExpenseType.EDUCATION.backgroundColor(context))
        }
        if(sumTotalHealth > 0) {
            lstEntries.add(initPieEntry(sumTotalHealth / sSumTotal, ExpenseType.HEALTH.description(context)))
            lstColors.add(ExpenseType.HEALTH.backgroundColor(context))
        }
        if(sumTotalFun > 0) {
            lstEntries.add(initPieEntry(sumTotalFun / sSumTotal, ExpenseType.FUN.description(context)))
            lstColors.add(ExpenseType.FUN.backgroundColor(context))
        }
        if(sumTotalRent > 0) {
            lstEntries.add(initPieEntry(sumTotalRent / sSumTotal, ExpenseType.RENT.description(context)))
            lstColors.add(ExpenseType.RENT.backgroundColor(context))
        }
        if(sumTotalTravel > 0) {
            lstEntries.add(initPieEntry(sumTotalTravel / sSumTotal, ExpenseType.TRAVEL.description(context)))
            lstColors.add(ExpenseType.TRAVEL.backgroundColor(context))
        }
        if(sumTotalOthers > 0) {
            lstEntries.add(initPieEntry(sumTotalOthers / sSumTotal, ExpenseType.OTHERS.description(context)))
            lstColors.add(ExpenseType.OTHERS.backgroundColor(context))
        }

        val dataSet = PieDataSet(lstEntries, "")
        dataSet.sliceSpace = 2f
        dataSet.formLineWidth = 120f
        dataSet.colors = lstColors
        val dataPie = PieData(dataSet)
        dataPie.setValueFormatter(PercentFormatter())
        dataPie.setValueTextSize(14f)
        dataPie.setValueTextColor(Color.WHITE)
        mpcPieChart?.legend?.textColor = ContextCompat.getColor(context, R.color.whiteColor)
        mpcPieChart?.legend?.textSize = 12f
        mpcPieChart?.legend?.isWordWrapEnabled = true
        mpcPieChart?.setDrawEntryLabels(false)
        mpcPieChart?.data = dataPie
        mpcPieChart?.highlightValues(null)
        mpcPieChart?.invalidate()

        mtvExpenseTotalValue?.text = MathService.formatFloatToCurrency(sSumTotal)

        var sTotalSalary = 0f
        StaticCollections.mastSalary?.forEach {
            val clCalendar = Calendar.getInstance()
            clCalendar.time = it.mdtDate
            if(clCalendar.get(Calendar.MONTH) == StaticCollections.mmtMonthSelected?.aid && clCalendar.get(Calendar.YEAR) == StaticCollections.mnYearSelected){
                if(it.msValue != null)
                    sTotalSalary += it.msValue!!
            }
        }

        mtvSalaryTotalValue?.text = MathService.formatFloatToCurrency(sTotalSalary)

        val sWalletValue = sTotalSalary - sSumTotal
        mtvWalletTotalValue?.text = MathService.formatFloatToCurrency(sWalletValue)
    }

    private fun initPieEntry(asFloatPercent : Float, astrString : String) : PieEntry {
        val entry = PieEntry(asFloatPercent)
        entry.label = astrString
        return entry
    }

}