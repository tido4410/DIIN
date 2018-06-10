package diiin.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class ChartTesting : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(PieChart(this))
    }
}

class PieChart(actxContext : Context) : View(actxContext) {

    init {
        isFocusable = true
    }

    override fun onDraw(canvas: Canvas) {
        //super.onDraw(canvas)

        val pBlue = Paint()
        pBlue.color = Color.BLUE
        val pRed = Paint()
        pRed.color = Color.RED

        val pGreen = Paint()
        pGreen.color = Color.GREEN

        val pYellow = Paint()
        pYellow.color = Color.YELLOW


        val leftCoord = 15f
        val rightCoord = resources.displayMetrics.widthPixels - leftCoord
        val topCoord = 200f
        val bottomCoord = resources.displayMetrics.heightPixels - topCoord

        val rect = RectF(leftCoord, topCoord, rightCoord, bottomCoord)

        canvas.drawArc(rect, 0f, 90f, true, pBlue)
        canvas.drawArc(rect, 90f, 90f, true, pRed)
        canvas.drawArc(rect, 180f, 90f, true, pGreen)
        canvas.drawArc(rect, 270f, 90f, true, pYellow)
    }

}