package com.example.mobiletestrakesh.domain.model

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
