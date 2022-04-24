package com.example.mobiletestrakesh.domain.model

data class User(
    val name: String,
    val stationCode: Int,
    val url: String
)

fun user()=
    User(name = "", stationCode = 0, url = "")
