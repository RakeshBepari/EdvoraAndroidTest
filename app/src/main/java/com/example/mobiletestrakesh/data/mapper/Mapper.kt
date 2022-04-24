package com.example.mobiletestrakesh.data.mapper

import com.example.mobiletestrakesh.data.remote.responses.RidesItemDto
import com.example.mobiletestrakesh.data.remote.responses.UserDto
import com.example.mobiletestrakesh.domain.model.RidesItem
import com.example.mobiletestrakesh.domain.model.User
import com.example.mobiletestrakesh.util.filter.FormatDateTime


/**
 * toRidesItem is a extension function of RidesItemDto
 * which is used to convert Dto object (i.e. UserDto and RidesItemDto) to domain object (i.e. Rides)
 *
 * The purpose of having a domain model is to decouple the remote model
 * from the model which is used for business logic, cause if we change the dto
 * the business logic remains unaffected
 * */
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