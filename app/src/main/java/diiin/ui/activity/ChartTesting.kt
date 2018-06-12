package diiin.ui.activity

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import br.com.gbmoro.diiin.R
import diiin.model.Expense
import diiin.model.ExpenseType
import diiin.util.MathService
import java.util.*

class ChartTesting : AppCompatActivity() {

    private var mllBaseContent : LinearLayout? = null
    private var fpcFInancialPieChart : FinancialPieChart? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart_testing)

        mllBaseContent = findViewById(R.id.llBaseContent)

        val expenseLst = ArrayList<Expense>()
        expenseLst.add(Expense(0,120f, "", Date(), ExpenseType.TRANSPORT))
        expenseLst.add(Expense(0,40f, "", Date(), ExpenseType.FOOD))
        expenseLst.add(Expense(0,100f, "", Date(), ExpenseType.FUN))
        expenseLst.add(Expense(0,85f, "", Date(), ExpenseType.OTHERS))
        expenseLst.add(Expense(0,11f, "", Date(), ExpenseType.EDUCATION))
        expenseLst.add(Expense(0,15f, "", Date(), ExpenseType.TRAVEL))
        expenseLst.add(Expense(0,12f, "", Date(), ExpenseType.PETS))

        fpcFInancialPieChart = FinancialPieChart(this, expenseLst)
        mllBaseContent?.addView(fpcFInancialPieChart)
    }
}

class FinancialPieChart(actxContext : Context, alstExpenseList : ArrayList<Expense>) : View(actxContext) {

    private var mexpenseList : ArrayList<Expense> = alstExpenseList
    private val mleftCoord : Float = 15f
    private val mrightCoord : Float = resources.displayMetrics.widthPixels.toFloat() - mleftCoord
    private val mtopCoord : Float = 20f
    private val mbottomCoord : Float = resources.displayMetrics.widthPixels.toFloat() * 0.85f
    private val mBaseRect = RectF(mleftCoord, mtopCoord, mrightCoord, mbottomCoord)
    private var msTotalValue : Float = 0f
    private val msTotalDegree : Float = 360f
    private val mpTempPaintObject : Paint = Paint()
    private val msMiddleScreen : Float = resources.displayMetrics.widthPixels.toFloat() / 2f

    init {
        isFocusable = true

        updateTotalValue()
    }

    private fun updateTotalValue() {
        msTotalValue = 0f
        mexpenseList.forEach {
            if(it.msValue!=null)
                msTotalValue += it.msValue!!
        }
    }

    override fun onDraw(canvas: Canvas) {

        var sCurrentPoint = 0f

        var nTopOfRect = mbottomCoord + 20f
        var bIsItLeft : Boolean = true

        mexpenseList.forEach {
            if(it.msValue != null && it.metType != null) {
                mpTempPaintObject.color = it.metType.backgroundColor(context)
                val sSweepAngle = ((it.msValue!! * msTotalDegree) / msTotalValue)
                canvas.drawArc(mBaseRect, sCurrentPoint, sSweepAngle, true, mpTempPaintObject)
                sCurrentPoint += sSweepAngle

                mpTempPaintObject.textSize = 22f
                val pPercentToShow = (sSweepAngle / msTotalDegree) * 100
                if(bIsItLeft) {
                    canvas.drawRect(mleftCoord, nTopOfRect, mleftCoord + 20f, nTopOfRect + 20f, mpTempPaintObject)
                    canvas.drawText(" ${MathService.formatDoubleTwoPlacesAfterComma(pPercentToShow)}% ${it.metType.description(context)}",
                            mleftCoord + 20f, nTopOfRect + 20f, mpTempPaintObject)
                    bIsItLeft = false
                } else {
                    canvas.drawRect(msMiddleScreen, nTopOfRect, msMiddleScreen + 20f, nTopOfRect + 20f, mpTempPaintObject)
                    canvas.drawText(" ${MathService.formatDoubleTwoPlacesAfterComma(pPercentToShow)}% ${it.metType.description(context)}",
                            msMiddleScreen + 20f, nTopOfRect + 20f, mpTempPaintObject)
                    nTopOfRect += 30f
                    bIsItLeft = true
                }
            }
        }
    }

}