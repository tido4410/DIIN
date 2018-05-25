package com.example.gabrielbronzattimoro.diiin.ui

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.example.gabrielbronzattimoro.diiin.util.MathService


class TWEditPrice(aetPrice : EditText) : TextWatcher {

    private val metPrice: EditText = aetPrice
    private var bIgnoreOnTextChange: Boolean = false

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
//        Log.d("TW", "onTextChanged-> CharSequence: $p0")
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


