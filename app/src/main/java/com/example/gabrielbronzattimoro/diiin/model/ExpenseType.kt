package com.example.gabrielbronzattimoro.diiin.model

import android.content.Context
import android.support.v4.content.ContextCompat
import com.example.gabrielbronzattimoro.diiin.R


enum class ExpenseType(val idExpense : Int){
    FOOD(1),
    TRANSPORT(2),
    PHONE(3),
    PETS(4),
    EDUCATION(5),
    HEALTH(6),
    FUN(7),
    RENT(8),
    TRAVEL(9),
    OTHERS(110);

    companion object {
        private val map = ExpenseType.values().associateBy(ExpenseType::idExpense)
        fun fromInt(type : Int) = map[type]

        fun gettingIdFromDescription(actxContext: Context, astrValue : String) : Int? {
            return when(astrValue) {
                FOOD.description(actxContext) -> FOOD.idExpense
                TRANSPORT.description(actxContext) -> TRANSPORT.idExpense
                PHONE.description(actxContext) -> PHONE.idExpense
                EDUCATION.description(actxContext) -> EDUCATION.idExpense
                HEALTH.description(actxContext) -> HEALTH.idExpense
                RENT.description(actxContext) -> RENT.idExpense
                TRAVEL.description(actxContext) -> TRAVEL.idExpense
                OTHERS.description(actxContext) -> OTHERS.idExpense
                else -> null
            }
        }
    }

    fun description(actxContext : Context) : String {
        return when(this) {
            FOOD -> actxContext.resources.getString(R.string.food)
            TRANSPORT -> actxContext.resources.getString(R.string.transport)
            PHONE -> actxContext.resources.getString(R.string.phone)
            PETS -> actxContext.resources.getString(R.string.pets)
            EDUCATION -> actxContext.resources.getString(R.string.education)
            HEALTH -> actxContext.resources.getString(R.string.health)
            FUN -> actxContext.resources.getString(R.string.funn)
            RENT -> actxContext.resources.getString(R.string.rent)
            TRAVEL -> actxContext.resources.getString(R.string.travel)
            OTHERS -> actxContext.resources.getString(R.string.other)
        }
    }

    fun fontColor(actxContext : Context) : Int {
        return when(this) {
            FOOD -> ContextCompat.getColor(actxContext, R.color.foodfont)
            TRANSPORT -> ContextCompat.getColor(actxContext, R.color.transportfont)
            PHONE -> ContextCompat.getColor(actxContext, R.color.phonefont)
            PETS -> ContextCompat.getColor(actxContext, R.color.petsfont)
            EDUCATION -> ContextCompat.getColor(actxContext, R.color.educationfont)
            HEALTH -> ContextCompat.getColor(actxContext, R.color.healthfont)
            FUN -> ContextCompat.getColor(actxContext, R.color.funnfont)
            RENT -> ContextCompat.getColor(actxContext, R.color.rentfont)
            TRAVEL -> ContextCompat.getColor(actxContext, R.color.travelfont)
            OTHERS -> ContextCompat.getColor(actxContext, R.color.othersfont)
        }
    }

    fun backgroundColor(actxContext : Context) : Int {
        return when(this) {
            FOOD -> ContextCompat.getColor(actxContext, R.color.foodbkg)
            TRANSPORT -> ContextCompat.getColor(actxContext, R.color.transportbkg)
            PHONE -> ContextCompat.getColor(actxContext, R.color.phonebkg)
            PETS -> ContextCompat.getColor(actxContext, R.color.petsbkg)
            EDUCATION -> ContextCompat.getColor(actxContext, R.color.educationbkg)
            HEALTH -> ContextCompat.getColor(actxContext, R.color.healthbkg)
            FUN -> ContextCompat.getColor(actxContext, R.color.funnbkg)
            RENT -> ContextCompat.getColor(actxContext, R.color.rentbkg)
            TRAVEL -> ContextCompat.getColor(actxContext, R.color.travelbkg)
            OTHERS -> ContextCompat.getColor(actxContext, R.color.othersbkg)
        }
    }

}