package diiin.ui.main_screen.report_tab

import com.github.mikephil.charting.data.PieEntry
import diiin.model.ExpenseType
import diiin.model.MonthType
import java.util.ArrayList

/**
 * Define a contract between view and presenter.
 * MVP pattern.
 * @author Gabriel Moro
 * @since 24/08/2018
 * @version 1.0.9
 */
interface FinancialReportContract {
    /**
     * Define all view operations.
     */
    interface View {
        fun drawPieChart(alstPieEntries: ArrayList<PieEntry>, alstColors: ArrayList<Int>)
        fun showChartItemCard(aetExpenseType: ExpenseType, asValue: Float)
        fun setSalaryTotal(astrValue: String)
        fun setExpenseTotal(astrValue: String)
        fun setWalletTotal(astrValue: String)
        fun hideChartItem()
    }

    /**
     * Define all operations connected to model layer
     */
    interface Presenter {
        fun dispareChartConstructor(amnMonthSelected: MonthType, anYearSelected: Int)
        fun onClickInPiePiece(peEntry: PieEntry)
        fun initPieEntry(asFloatPercent: Float, astrString: String): PieEntry
        fun loadDemonstrativePanel(amnMonthSelected: MonthType, anYearSelected: Int)
    }
}