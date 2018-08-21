package diiin.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import br.com.gbmoro.diiin.R
import android.graphics.Color
import android.view.View
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder



class InsertExpenseType : AppCompatActivity() {

    private var mbtnChangeColor : Button? = null
    private var mvwCurrentColor : View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_expense_type)

        mbtnChangeColor = findViewById(R.id.btnChangeColor)
        mvwCurrentColor = findViewById(R.id.vwCurrentColor)

        mbtnChangeColor?.setOnClickListener {
            ColorPickerDialogBuilder
                    .with(this)
                    .setTitle("Choose color")
                    .initialColor(Color.WHITE)
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    //.setOnColorSelectedListener { selectedColor -> /*Toast("onColorSelected: 0x" + Integer.toHexString(selectedColor))*/ }
                    .setPositiveButton("ok") { dialog, selectedColor, allColors -> mvwCurrentColor?.setBackgroundColor(selectedColor) }
                    .setNegativeButton("cancel") { dialog, which -> }
                    .build()
                    .show()
        }
    }
}
