package com.example.mobiletestrakesh.domain.model


/**
 * A Ride item which is used in the business layer for business logic
 * */
data class RidesItem(
    val city: String,
    val date: String,
    val id: Int,
    val mapUrl: String,
    val originStation: Int,
    val state: String,
    val stationPath: List<Int>,
    val distance : Int = 0
)
