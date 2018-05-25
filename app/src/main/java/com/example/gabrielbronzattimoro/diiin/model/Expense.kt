package com.example.gabrielbronzattimoro.diiin.model

import java.util.*

class Expense(anId : Int?, asValue : Float?, astrDescription : String, adtDate : Date?, aexpenseType : ExpenseType?) {

    var mnId : Int? = anId
    var msValue : Float? = asValue
    var mstrDescription : String = astrDescription
    var mdtDate : Date? = adtDate
    val mexpenseType : ExpenseType? = aexpenseType

}