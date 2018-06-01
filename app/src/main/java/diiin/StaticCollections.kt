package diiin

import diiin.model.Expense
import diiin.model.MonthType
import diiin.model.Salary

object StaticCollections {

    var mastExpenses : ArrayList<Expense>? = null
    var mastSalary : ArrayList<Salary>? = null
    var mmtMonthSelected : MonthType? = null
    var mnYearSelected : Int = 2018
    var mbEditMode : Boolean = false
}