package diiin.ui.insert_expense_type_screen

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import br.com.gbmoro.diiin.R
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import diiin.util.MessageDialog

/**
 * Define InsertExpenseTypeActivity view.
 * @author Gabriel Moro
 * @since 11/09/2018
 * @version 1.0.9
 */
class InsertExpenseTypeActivity : AppCompatActivity(), InsertExpenseTypeContract.View {

    private var presenter: InsertExpenseTypeContract.Presenter? = null
    private var mbtnChangeColor: Button? = null
    private var mvwCurrentColor: View? = null
    private var mbtnSaveExpenseType: Button? = null
    private var metEditText: EditText? = null
    private var mnColorSelected: Int? = null
    private var mnExpenseTypeId: Long? = null

    companion object {
        const val INTENT_KEY_EXPENSETYPEID: String = "IdOfExpenseTypeToEdit"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_expense_type)

        mbtnChangeColor = findViewById(R.id.btnChangeColor)
        mvwCurrentColor = findViewById(R.id.vwCurrentColor)
        mbtnSaveExpenseType = findViewById(R.id.btnSaveExpenseType)
        metEditText = findViewById(R.id.etExpenseTypeDescription)

        if (intent.hasExtra(INTENT_KEY_EXPENSETYPEID)) {
            mnExpenseTypeId = intent.extras.getLong(INTENT_KEY_EXPENSETYPEID)
            title = resources.getString(R.string.title_editexpensetype)
        }

        presenter = InsertExpenseTypePresenter(this)
        presenter?.loadExpenseTypeValues(mnExpenseTypeId)

        mbtnChangeColor?.setOnClickListener {
            ColorPickerDialogBuilder
                    .with(this)
                    .setTitle(resources.getString(R.string.chooseColor))
                    .initialColor(Color.WHITE)
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setOnColorSelectedListener { selectedColor -> mnColorSelected = selectedColor }
                    .setPositiveButton(resources.getString(R.string.ok)) { _, selectedColor, _ -> mvwCurrentColor?.setBackgroundColor(selectedColor) }
                    .setNegativeButton(resources.getString(R.string.cancel)) { _, _ -> }
                    .build()
                    .show()
        }
        mbtnSaveExpenseType?.setOnClickListener {
            if(mnColorSelected!=null)
                presenter?.saveExpenseType(mnExpenseTypeId, metEditText?.text.toString(), mnColorSelected!!)
        }
    }

    /**
     * Show the sucess message when user insert new expense type or some update.
     */
    override fun showSucessMessage() {
        MessageDialog.showToastMessage(this, resources.getString(R.string.sucessaction))
    }

    /**
     * Show the unsucess message when user insert new expense type or some update.
     */
    override fun showUnsucessMessage() {
        MessageDialog.showToastMessage(this, resources.getString(R.string.fillAreFields))
    }

    /**
     * Change the color value.
     */
    override fun setColor(anColorValue: Int) {
        mvwCurrentColor?.setBackgroundColor(anColorValue)
        mnColorSelected = anColorValue
    }

    /**
     * Change the description text value.
     */
    override fun setDecription(astrDescription: String) {
        metEditText?.setText(astrDescription)
    }
}
