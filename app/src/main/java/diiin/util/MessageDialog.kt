package diiin.util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.widget.Toast

/**
 * This class defines all user alerts and toasts
 */
object MessageDialog {

    /**
     * Define a alert dialog
     *
     * @param actxContext is the context
     * @param astrTextInfo is the message
     * @param alstPositive defines the positive action listener
     * @param alstNegative defines the negative action listener
     */
    fun showMessageDialog(actxContext : Context, astrTextInfo : String,
                          alstPositive : DialogInterface.OnClickListener,
                          alstNegative : DialogInterface.OnClickListener) {

        val builder = AlertDialog.Builder(actxContext)
        builder.setMessage(astrTextInfo)
        builder.setPositiveButton(android.R.string.yes, alstPositive)
        builder.setNegativeButton(android.R.string.no, alstNegative)
        builder.show()
    }

    /**
     * Define toast message
     *
     * @param actxContext is the context
     * @param astrTextInfo is the message
     */
    fun showToastMessage(actxContext : Context, astrTextInfo : String) {
        Toast.makeText(actxContext, astrTextInfo, Toast.LENGTH_SHORT).show()
    }
}