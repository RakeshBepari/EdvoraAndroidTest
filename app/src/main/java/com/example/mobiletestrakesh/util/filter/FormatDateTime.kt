package com.example.mobiletestrakesh.util.filter

import java.text.SimpleDateFormat
import java.util.*


/**
 * A class used for formatting date time of a ride item
 * */
object FormatDateTime {
    fun getFormattedDateTime(dateTime: String): String {

        val lastTwo = dateTime.substring(dateTime.length - 2)

        val simpleDateFormat = SimpleDateFormat("MM/dd/yyyy HH:mm a", Locale.getDefault())
        val date = SimpleDateFormat("dd MMM yyyy HH:mm ", Locale.ENGLISH).format(
            simpleDateFormat.parse(dateTime)!!
        )

        return date + lastTwo
    }

    fun getOnlyFormattedDate(dateTime: String): MonthAndDate {

        val simpleDateFormat = SimpleDateFormat("dd MMM yyyy HH:mm a", Locale.getDefault())
        val month =
            SimpleDateFormat("MM", Locale.ENGLISH).format(simpleDateFormat.parse(dateTime)!!)
        val date = SimpleDateFormat("dd", Locale.ENGLISH).format(simpleDateFormat.parse(dateTime)!!)


        return MonthAndDate(date = date.toInt(), month = month.toInt())
    }

    fun getTodaysDate(): MonthAndDate {
        val mformat = SimpleDateFormat("MM")
        val month = mformat.format(Date())

        val dformat = SimpleDateFormat("dd")
        val date = dformat.format(Date())

        return MonthAndDate(date = date.toInt(), month = month.toInt())
    }

}


data class MonthAndDate(val date: Int, val month: Int)