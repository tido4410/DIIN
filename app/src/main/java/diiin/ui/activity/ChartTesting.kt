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
    private val mbottomCoord : Float = resources.displayMetrics.widthPixels.toFloat() - mtopCoord
    private val mBaseRect = RectF(mleftCoord, mtopCoord, mrightCoord, mbottomCoord)
    private var msTotalValue : Float = 0f
    private val msTotalDegree : Float = 360f
    private val mpTempPaintObject : Paint = Paint()

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

//    400 <-> 360
//
//    12 <-> x
//
//
//    400x = 360 * 12
//    x = 360 * 12 / 400

    override fun onDraw(canvas: Canvas) {
        //super.onDraw(canvas)

        var sCurrentPoint = 0f

        var nTopOfRect = mbottomCoord + 20f

        mexpenseList.forEach {
            if(it.msValue != null && it.metType != null) {
                mpTempPaintObject.color = it.metType.backgroundColor(context)
                val sSweepAngle = ((it.msValue!! * msTotalDegree) / msTotalDegree)
                canvas.drawArc(mBaseRect, sCurrentPoint, sSweepAngle, true, mpTempPaintObject)
                sCurrentPoint += sSweepAngle
                canvas.drawRect(mleftCoord, nTopOfRect, mleftCoord + 20f, nTopOfRect + 20f, mpTempPaintObject)
                mpTempPaintObject.textSize = 30f
                val pPercentToShow = (sSweepAngle / msTotalDegree) * 100
                canvas.drawText(" $pPercentToShow %: ${it.metType.description(context)}",
                        mleftCoord + 20f, nTopOfRect + 20f, mpTempPaintObject)
                nTopOfRect += 30f
            }
        }

        //RectF(mleftCoord, mtopCoord, mrightCoord, mbottomCoord)




        /*val pBlue = Paint()
        pBlue.color = Color.BLUE
        val pRed = Paint()
        pRed.color = Color.RED

        val pGreen = Paint()
        pGreen.color = Color.GREEN

        val pYellow = Paint()
        pYellow.color = Color.YELLOW

        canvas.drawArc(mBaseRect, 0f, 90f, true, pBlue)
        canvas.drawArc(mBaseRect, 90f, 90f, true, pRed)
        canvas.drawArc(mBaseRect, 180f, 90f, true, pGreen)
        canvas.drawArc(mBaseRect, 270f, 90f, true, pYellow)*/
    }

}