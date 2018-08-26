package diiin.ui.activity

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import br.com.gbmoro.diiin.R
import android.graphics.Color
import android.view.View
import android.widget.EditText
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import diiin.StaticCollections
import diiin.model.ExpenseType
import diiin.util.MessageDialog


class InsertExpenseType : AppCompatActivity() {

    private var mbtnChangeColor : Button? = null
    private var mvwCurrentColor : View? = null
    private var mbtnSaveExpenseType : Button? = null
    private var metEditText : EditText? = null
    private var mstrColorSelected : String? = null
    private var mnExpenseTypeId : Long? = null

    companion object {
        const val INTENT_KEY_EXPENSETYPEID : String = "IdOfExpenseTypeToEdit"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_expense_type)

        mbtnChangeColor = findViewById(R.id.btnChangeColor)
        mvwCurrentColor = findViewById(R.id.vwCurrentColor)
        mbtnSaveExpenseType = findViewById(R.id.btnSaveExpenseType)
        metEditText = findViewById(R.id.etExpenseTypeDescription)

        if(intent.hasExtra(INTENT_KEY_EXPENSETYPEID)) {
            mnExpenseTypeId = intent.extras.getLong(INTENT_KEY_EXPENSETYPEID)
            title = resources.getString(R.string.title_editexpensetype)
            val expenseTypeTarget = StaticCollections.mappDataBuilder?.expenseTypeDao()?.all()?.filter { it.mnExpenseTypeID == mnExpenseTypeId }?.first()
            mstrColorSelected = expenseTypeTarget?.mstrColor
            metEditText?.setText(expenseTypeTarget?.mstrDescription)
            mvwCurrentColor?.setBackgroundColor(Color.parseColor(expenseTypeTarget?.mstrColor))
        }

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
            MessageDialog.showMessageDialog(this,
                    resources.getString(R.string.msgAreYouSure),
                    DialogInterface.OnClickListener { adialog, _ ->
                        val strDescriptionType : String? = metEditText?.text?.toString()
                        if (mstrColorSelected != null && strDescriptionType != null) {
                            if(mnExpenseTypeId == null) {
                                StaticCollections.mappDataBuilder?.expenseTypeDao()?.add(ExpenseType(null, strDescriptionType, mstrColorSelected!!))
                            } else {
                                StaticCollections.mappDataBuilder?.expenseTypeDao()?.update(ExpenseType(mnExpenseTypeId, strDescriptionType, mstrColorSelected!!))
                            }
                        }
                        adialog.dismiss()
                        finish()
                    },
                    DialogInterface.OnClickListener { adialog, _ ->
                        adialog.dismiss()
                    })
        }
    }
}
