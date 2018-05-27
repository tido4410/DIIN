package com.example.gabrielbronzattimoro.diiin.ui

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.example.gabrielbronzattimoro.diiin.util.MathService

/**
 * This file is used to register the components created to the app
 */


/**
 * TWEditPrice is the component used as textwatcher in currency edit texts,
 * this component autoadjust the integer numbers to currency value.
 *
 * @author Gabriel Moro
 *
 * @param aetPrice is the edittext component
 */
class TWEditPrice(aetPrice : EditText) : TextWatcher {

    private val metPrice: EditText = aetPrice
    private var bIgnoreOnTextChange: Boolean = false

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    /**
     * Each user interaction this method will be called, to put some currency markers in
     * the text.
     *
     * @param p0 is the current string
     */
    override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
        if (!bIgnoreOnTextChange) {
            val strText = p0.toString()
                    .replace(",", "")
                    .replace(".", "")
                    .replace("R$","")
            val nNumber = strText.toIntOrNull() ?: return
            val strMoney = MathService.formatIntToCurrencyValue(nNumber)
            bIgnoreOnTextChange = true
            metPrice.setText(strMoney)
            metPrice.setSelection(strMoney.length)
            bIgnoreOnTextChange = false
        }
    }

    override fun afterTextChanged(p0: Editable) { }
}


