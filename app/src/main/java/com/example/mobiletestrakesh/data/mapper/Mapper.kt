package com.example.mobiletestrakesh.data.mapper

import com.example.mobiletestrakesh.data.remote.responses.RidesItemDto
import com.example.mobiletestrakesh.data.remote.responses.UserDto
import com.example.mobiletestrakesh.domain.model.RidesItem
import com.example.mobiletestrakesh.domain.model.User
import com.example.mobiletestrakesh.util.filter.FormatDateTime

fun RidesItemDto.toRidesItem(): RidesItem {
    return RidesItem(
        city = city,
        date = FormatDateTime.getFormattedDateTime(date),
        id = id,
        mapUrl = map_url,
        originStation = origin_station_code,
        state = state,
        stationPath = station_path,
    )
}

fun UserDto.toUser(): User {
    return User(
        name = name,
        stationCode = station_code,
        url = url
    )
}