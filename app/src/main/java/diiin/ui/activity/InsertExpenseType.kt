package diiin.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import br.com.gbmoro.diiin.R
import android.graphics.Color
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import diiin.StaticCollections
import diiin.model.ExpenseType


class InsertExpenseType : AppCompatActivity() {

    private var mbtnChangeColor : Button? = null
    private var mvwCurrentColor : View? = null
    private var mbtnSaveExpenseType : Button? = null
    private var metEditText : EditText? = null
    private var mstrColorSelected : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_expense_type)

        mbtnChangeColor = findViewById(R.id.btnChangeColor)
        mvwCurrentColor = findViewById(R.id.vwCurrentColor)
        mbtnSaveExpenseType = findViewById(R.id.btnSaveExpenseType)
        metEditText = findViewById(R.id.etExpenseTypeDescription)

        mbtnChangeColor?.setOnClickListener {
            ColorPickerDialogBuilder
                    .with(this)
                    .setTitle("Escolha a cor")
                    .initialColor(Color.WHITE)
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setOnColorSelectedListener { selectedColor -> mstrColorSelected = "#${Integer.toHexString(selectedColor)}" }//mstrColorSelected = Color.parseColor(Integer.toHexString(selectedColor)/*Toast("onColorSelected: 0x" + Integer.toHexString(selectedColor))*/ }
                    .setPositiveButton("ok") { dialog, selectedColor, allColors -> mvwCurrentColor?.setBackgroundColor(selectedColor) }
                    .setNegativeButton("cancel") { dialog, which -> }
                    .build()
                    .show()
        }

        mbtnSaveExpenseType?.setOnClickListener {
            val strDescriptionType : String? = metEditText?.text?.toString()
            if(mstrColorSelected!=null && strDescriptionType != null) {
                StaticCollections.mappDataBuilder?.expenseTypeDao()?.add(ExpenseType(null, strDescriptionType, mstrColorSelected!!))
            }
        }
    }
}
