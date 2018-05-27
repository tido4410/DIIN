package com.example.gabrielbronzattimoro.diiin.util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.widget.Toast

object MessageDialog {

    fun showMessageDialog(actxContext : Context, astrTextInfo : String,
                          alstPositive : DialogInterface.OnClickListener,
                          alstNegative : DialogInterface.OnClickListener) {

        val builder = AlertDialog.Builder(actxContext)
        builder.setMessage(astrTextInfo)
        builder.setPositiveButton(android.R.string.yes, alstPositive)
        builder.setNegativeButton(android.R.string.no, alstNegative)
        builder.show()
    }

    fun showToastMessage(actxContext : Context, astrTextInfo : String) {
        Toast.makeText(actxContext, astrTextInfo, Toast.LENGTH_SHORT).show()
    }
}