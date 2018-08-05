package diiin.ui.fragments

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
import diiin.StaticCollections
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
import diiin.model.Expense
import diiin.ui.activity.MainActivity
import java.util.*
import kotlin.collections.HashMap

/**
 * Screen that shows to user the financial overview
 *
 * @author Gabriel Moro
 */
class FragmentFinancialReport : Fragment(), MainActivity.MainPageFragments {


    companion object {
        const val NAME = "FragmentFinancialReport"
    }

    private var mpcPieChart: PieChart? = null
    private var mrlChartItem : RelativeLayout? = null
    private var mrlPieChartContainer : RelativeLayout? = null
    private var mrlWalletPanel : RelativeLayout? = null
    private var mtvExpenseTotalValue : TextView? = null
    private var mtvSalaryTotalValue : TextView? = null
    private var mtvWalletTotalValue : TextView? = null
    private val mhmExpenseByPercentage : HashMap<ExpenseType, Expense> = HashMap()
    /**
     * Chart item elements
     */
    private var mtvChartItemValue : TextView? = null
    private var mtvChartItemDate : TextView? = null
    private var mvwChartItemExpenseType : View? = null
    private var mllChartItemLinearLayout : LinearLayout? = null
    private var mivChartItemReorder : ImageView? = null
    private var mivExpenseType : ImageView? = null
    private var mtvChartItemExpenseType : TextView? = null

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
        mivChartItemReorder = view?.findViewById(R.id.ivReorder)
        mivExpenseType = view?.findViewById(R.id.ivExpenseType)

        mpcPieChart?.setUsePercentValues(true)
        mpcPieChart?.description?.isEnabled = false
        mpcPieChart?.setExtraOffsets(2f, 5f, 2f, 2f)
        mpcPieChart?.dragDecelerationFrictionCoef = 0.95f
        mpcPieChart?.rotationAngle = 0f
        mpcPieChart?.isRotationEnabled = false
        mpcPieChart?.isDrawHoleEnabled = true
        mpcPieChart?.transparentCircleRadius = 10f
        mpcPieChart?.holeRadius = 7f
        mpcPieChart?.setHoleColor(ContextCompat.getColor(context,R.color.whiteColor))

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()

        mrlChartItem?.visibility = RelativeLayout.GONE
        mrlWalletPanel?.visibility = RelativeLayout.VISIBLE
        loadPageContent()
    }

    override fun loadPageContent() {
        mhmExpenseByPercentage.clear()

        val lstExpenses : ArrayList<Expense> = ArrayList<Expense>()//StaticCollections.mastExpenses ?: return

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

//        lstExpenses.forEach{
//            val clCalendar = Calendar.getInstance()
//            clCalendar.time = it.mdtDate
//            if(clCalendar.get(Calendar.MONTH)== StaticCollections.mmtMonthSelected?.aid) {
//                if(it.msValue!=null)
//                    sSumTotal += it.msValue!!
//                when(it.metType) {
//                    ExpenseType.FOOD -> {
//                        if(it.msValue != null)
//                            sumTotalFood += it.msValue!!
//                    }
//                    ExpenseType.TRANSPORT -> {
//                        if(it.msValue != null)
//                            sumTotalTransport+=it.msValue!!
//                    }
//                    ExpenseType.PHONE -> {
//                        if(it.msValue != null)
//                            sumTotalPhone+=it.msValue!!
//                    }
//                    ExpenseType.PETS -> {
//                        if(it.msValue != null)
//                            sumTotalPets+=it.msValue!!
//                    }
//                    ExpenseType.EDUCATION -> {
//                        if(it.msValue != null)
//                            sumTotalEducation+=it.msValue!!
//                    }
//                    ExpenseType.HEALTH -> {
//                        if(it.msValue != null)
//                            sumTotalHealth+=it.msValue!!
//                    }
//                    ExpenseType.FUN -> {
//                        if(it.msValue != null)
//                            sumTotalFun+=it.msValue!!
//                    }
//                    ExpenseType.RENT -> {
//                        if(it.msValue != null)
//                            sumTotalRent+=it.msValue!!
//                    }
//                    ExpenseType.TRAVEL -> {
//                        if(it.msValue != null)
//                            sumTotalTravel+=it.msValue!!
//                    }
//                    ExpenseType.OTHERS -> {
//                        if(it.msValue != null)
//                            sumTotalOthers+=it.msValue!!
//                    }
//                }
//            }
//        }
//
//        val lstEntries = ArrayList<PieEntry>()
//        val lstColors = ArrayList<Int>()
//
//        if(sumTotalFood > 0) {
//            val perFood = sumTotalFood / sSumTotal
//            val expenseFood = Expense(0, sumTotalFood, "", null, ExpenseType.FOOD)
//            mhmExpenseByPercentage[ExpenseType.FOOD] = expenseFood
//            lstEntries.add(initPieEntry(perFood, expenseFood.metType!!.description(context)))
//            lstColors.add(expenseFood.metType.backgroundColor(context))
//        }
//        if(sumTotalTransport > 0) {
//            val perTransport = sumTotalTransport / sSumTotal
//            val expenseTransport = Expense(0, sumTotalTransport, "", null, ExpenseType.TRANSPORT)
//            mhmExpenseByPercentage[ExpenseType.TRANSPORT] = expenseTransport
//            lstEntries.add(initPieEntry(perTransport, expenseTransport.metType!!.description(context)))
//            lstColors.add(expenseTransport.metType.backgroundColor(context))
//        }
//        if(sumTotalPhone > 0) {
//            val perPhone = sumTotalPhone / sSumTotal
//            val expensePhone = Expense(0, sumTotalPhone, "", null, ExpenseType.PHONE)
//            mhmExpenseByPercentage[ExpenseType.PHONE] = expensePhone
//            lstEntries.add(initPieEntry(perPhone, expensePhone.metType!!.description(context)))
//            lstColors.add(expensePhone.metType.backgroundColor(context))
//        }
//        if(sumTotalPets > 0) {
//            val perPets = sumTotalPets / sSumTotal
//            val expensePets = Expense(0, sumTotalPets, "", null, ExpenseType.PETS)
//            mhmExpenseByPercentage[ExpenseType.PETS] = expensePets
//            lstEntries.add(initPieEntry(perPets, expensePets.metType!!.description(context)))
//            lstColors.add(expensePets.metType.backgroundColor(context))
//        }
//        if(sumTotalEducation > 0) {
//            val perEducation = sumTotalEducation / sSumTotal
//            val expenseEducation = Expense(0, sumTotalEducation, "", null, ExpenseType.EDUCATION)
//            mhmExpenseByPercentage[ExpenseType.EDUCATION] = expenseEducation
//            lstEntries.add(initPieEntry(perEducation, expenseEducation.metType!!.description(context)))
//            lstColors.add(expenseEducation.metType.backgroundColor(context))
//        }
//        if(sumTotalHealth > 0) {
//            val perHealth = sumTotalHealth / sSumTotal
//            val expenseHealth = Expense(0, sumTotalHealth, "", null, ExpenseType.HEALTH)
//            mhmExpenseByPercentage[ExpenseType.HEALTH] = expenseHealth
//            lstEntries.add(initPieEntry(perHealth, expenseHealth.metType!!.description(context)))
//            lstColors.add(expenseHealth.metType.backgroundColor(context))
//        }
//        if(sumTotalFun > 0) {
//            val perFun = sumTotalFun / sSumTotal
//            val expenseFun = Expense(0, sumTotalFun, "", null, ExpenseType.FUN)
//            mhmExpenseByPercentage[ExpenseType.FUN] = expenseFun
//            lstEntries.add(initPieEntry(perFun, expenseFun.metType!!.description(context)))
//            lstColors.add(expenseFun.metType.backgroundColor(context))
//        }
//        if(sumTotalRent > 0) {
//            val perRent = sumTotalRent / sSumTotal
//            val expenseRent = Expense(0, sumTotalRent, "", null, ExpenseType.RENT)
//            mhmExpenseByPercentage[ExpenseType.RENT] = expenseRent
//            lstEntries.add(initPieEntry(perRent, expenseRent.metType!!.description(context)))
//            lstColors.add(expenseRent.metType.backgroundColor(context))
//        }
//        if(sumTotalTravel > 0) {
//            val perTravel = sumTotalTravel / sSumTotal
//            val expenseTravel = Expense(0, sumTotalTravel, "", null, ExpenseType.TRAVEL)
//            mhmExpenseByPercentage[ExpenseType.TRAVEL] = expenseTravel
//            lstEntries.add(initPieEntry(perTravel, expenseTravel.metType!!.description(context)))
//            lstColors.add(expenseTravel.metType.backgroundColor(context))
//        }
//        if(sumTotalOthers > 0) {
//            val perOthers = sumTotalOthers / sSumTotal
//            val expenseOthers = Expense(0, sumTotalOthers, "", null, ExpenseType.OTHERS)
//            mhmExpenseByPercentage[ExpenseType.OTHERS] = expenseOthers
//            lstEntries.add(initPieEntry(perOthers, expenseOthers.metType!!.description(context)))
//            lstColors.add(expenseOthers.metType.backgroundColor(context))
//        }
//
//        val dataSet = PieDataSet(lstEntries, "")
//        dataSet.sliceSpace = 2f
//        dataSet.formLineWidth = 120f
//        dataSet.colors = lstColors
//        val dataPie = PieData(dataSet)
//        dataPie.setValueFormatter(PercentFormatter())
//        dataPie.setValueTextSize(14f)
//        dataPie.setValueTextColor(Color.WHITE)
//        mpcPieChart?.legend?.textColor = ContextCompat.getColor(context, R.color.whiteColor)
//        mpcPieChart?.legend?.textSize = 12f
//        mpcPieChart?.legend?.isWordWrapEnabled = true
//        mpcPieChart?.setDrawEntryLabels(false)
//        mpcPieChart?.data = dataPie
//        mpcPieChart?.highlightValues(null)
//        mpcPieChart?.invalidate()
//
//        mpcPieChart?.setOnChartValueSelectedListener(object : OnChartValueSelectedListener{
//            override fun onNothingSelected() {
//                mrlChartItem?.visibility = RelativeLayout.GONE
//                mrlWalletPanel?.visibility = RelativeLayout.VISIBLE
//            }
//
//            override fun onValueSelected(e: Entry, h: Highlight?) {
//
//                val pieEntry = e as PieEntry
//                val nId = ExpenseType.gettingIdFromDescription(context, pieEntry.label) ?: return
//                val expenseTarget = mhmExpenseByPercentage[ExpenseType.fromInt(nId)]
//                if(expenseTarget != null)
//                    loadChartItemCard(expenseTarget)
//            }
//        })
//
//        mtvExpenseTotalValue?.text = MathService.formatFloatToCurrency(sSumTotal)
//
//        var sTotalSalary = 0f
//        StaticCollections.mastSalary?.forEach {
//            val clCalendar = Calendar.getInstance()
//            clCalendar.time = it.mdtDate
//            if(clCalendar.get(Calendar.MONTH) == StaticCollections.mmtMonthSelected?.aid && clCalendar.get(Calendar.YEAR) == StaticCollections.mnYearSelected){
//                if(it.msValue != null)
//                    sTotalSalary += it.msValue!!
//            }
//        }
//
//        mtvSalaryTotalValue?.text = MathService.formatFloatToCurrency(sTotalSalary)
//
//        val sWalletValue = sTotalSalary - sSumTotal
//        mtvWalletTotalValue?.text = MathService.formatFloatToCurrency(sWalletValue)
//
//        mrlChartItem?.visibility = RelativeLayout.GONE
    }

    private fun initPieEntry(asFloatPercent : Float, astrString : String) : PieEntry {
        val entry = PieEntry(asFloatPercent)
        entry.label = astrString
        return entry
    }

    private fun loadChartItemCard(aExpenseItem : Expense?) {
        aExpenseItem ?: return

        mivChartItemReorder?.visibility = ImageView.GONE
        mtvChartItemDate?.visibility = TextView.GONE

        if(aExpenseItem.msValue != null) mtvChartItemValue?.text = MathService.formatFloatToCurrency(aExpenseItem.msValue!!)

//        if(aExpenseItem.metType != null) {
//            mtvChartItemExpenseType?.text = aExpenseItem.metType.description(context)
//            mtvChartItemValue?.setTextColor(aExpenseItem.metType.backgroundColor(context))
//            mvwChartItemExpenseType?.setBackgroundColor(aExpenseItem.metType.backgroundColor(context))
//            mivChartItemReorder?.visibility = ImageView.GONE
//            mivExpenseType?.setImageResource(aExpenseItem.metType.imageIconId())
//        }

        mrlChartItem?.visibility = RelativeLayout.VISIBLE
        mrlWalletPanel?.visibility = RelativeLayout.GONE
    }

}