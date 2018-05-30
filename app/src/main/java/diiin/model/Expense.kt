package br.com.gbmoro.diiin.model

import java.util.*

/**
 * Define the model of expense in the system.
 *
 * @author Gabriel Moro
 *
 * @param anId is the expense identifier
 * @param asValue is the expense price
 * @param asrDescription is something that user want to register
 * @param adtDate is the date of expense0
 * @param aetType is the type of expense
 */
class Expense(anId : Int?, asValue : Float?, asrDescription : String, adtDate : Date?, aetType : ExpenseType?) {

    var mnId : Int? = anId
    var msValue : Float? = asValue
    var msrDescription : String = asrDescription
    var mdtDate : Date? = adtDate
    val metType : ExpenseType? = aetType
    var mbSelected : Boolean = false

}