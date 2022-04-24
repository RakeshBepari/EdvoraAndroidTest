package com.example.mobiletestrakesh.util.filter

import android.util.Log
import com.example.mobiletestrakesh.domain.model.RidesItem
import com.example.mobiletestrakesh.presentation.RidesFilter
import com.example.mobiletestrakesh.presentation.TAG
import java.util.*
import kotlin.math.abs


/** Filters the raw list from the api to nearest, upcoming and past */
object ListFilter {

    fun pastFilter(list: List<RidesItem>):List<RidesItem> {

        val pastRidesList  = mutableListOf<RidesItem>()

        list.forEach { ridesItem ->
            val rideDate = FormatDateTime.getOnlyFormattedDate(ridesItem.date)
            val currentDate = FormatDateTime.getTodaysDate()
            if (rideDate.month < currentDate.month) {
                pastRidesList.add(ridesItem)
            } else if (rideDate.month == currentDate.month) {
                if (rideDate.date < currentDate.date) {
                    pastRidesList.add(ridesItem)
                }
            }
        }

        return pastRidesList
    }

    fun upcomingFilter(list: List<RidesItem>):List<RidesItem> {

        val upcomingRidesList = mutableListOf<RidesItem>()

        list.forEach { ridesItem ->
            val rideDate = FormatDateTime.getOnlyFormattedDate(ridesItem.date)
            val currentDate = FormatDateTime.getTodaysDate()
            if (rideDate.month > currentDate.month) {
                upcomingRidesList.add(ridesItem)
            } else if (rideDate.month == currentDate.month) {
                if (rideDate.date > currentDate.date) {
                    upcomingRidesList.add(ridesItem)
                }
            }
        }

        return upcomingRidesList

    }


    fun nearestFilter(list: List<RidesItem>, userStationCode:Int):List<RidesItem> {

        val nearestRidesList = mutableListOf<RidesItem>()

        list.map { ridesItem ->
            val distanceList = ridesItem.stationPath.map {
                abs(it - userStationCode)
            }
            val distance = Collections.min(distanceList)
            nearestRidesList.add(ridesItem.copy(distance = distance))
            Log.d(TAG, nearestRidesList.toString())

        }
        nearestRidesList.sortBy { it.distance }

        return nearestRidesList

    }


}