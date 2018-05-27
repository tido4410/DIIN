package com.example.gabrielbronzattimoro.diiin.model

import java.util.*

/**
 * Define the model of salary in the system.
 *
 * @author Gabriel Moro
 *
 * @param astSource is the origin of the payment
 * @param asValue is the payment total value
 * @param adtDate is payment date
 */
class Salary(astSource : String?, asValue : Float?, adtDate : Date?) {

    var mstSource : String? = astSource
    var msValue : Float? = asValue
    var mdtDate : Date? = adtDate

}