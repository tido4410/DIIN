package diiin

import diiin.dao.DataBaseFactory
import diiin.model.Expense
import diiin.model.MonthType
import diiin.model.Salary

/**
 * This singleton class is created to store in runtime
 * the user preferences and shared preferences loaded
 * according the users interaction.
 *
 * @author Gabriel Moro
 */
object StaticCollections {

    var mappDataBuilder : DataBaseFactory? = null
    /**
     * This list stores all expenses created by user
     */
    var mastExpenses : ArrayList<Expense>? = null
    /**
     * This list stores all incomes created by user
     */
    var mastSalary : ArrayList<Salary>? = null
    /**
     * Represents the month selected in the filter by user
     */
    var mmtMonthSelected : MonthType? = null
    /**
     * Represent the current year.
     */
    var mnYearSelected : Int = 2018
    /**
     * Represent the user preference,
     * if edit mode is enabled, the user can delete expenses
     * or incomes and sort them.
     */
    var mbEditMode : Boolean = false

}