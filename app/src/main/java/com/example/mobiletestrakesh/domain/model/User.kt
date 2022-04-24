package com.example.mobiletestrakesh.domain.model


/**
 * User item used for business logic
 * */
data class User(
    val name: String,
    val stationCode: Int,
    val url: String
)

/***/
fun user()=
    User(name = "", stationCode = 0, url = "")
