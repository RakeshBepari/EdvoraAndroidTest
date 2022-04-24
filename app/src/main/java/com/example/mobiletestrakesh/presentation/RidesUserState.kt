package com.example.mobiletestrakesh.presentation

import com.example.mobiletestrakesh.domain.model.RidesItem
import com.example.mobiletestrakesh.domain.model.User
import com.example.mobiletestrakesh.domain.model.user
import com.example.mobiletestrakesh.other.Constants.DUMMY_CITY_FILTER
import com.example.mobiletestrakesh.other.Constants.DUMMY_STATE_FILTER

data class RidesUserState(
    val ridesList: List<RidesItem> = emptyList(),
    val user: User = user(),
    val isLoading: Boolean = false,
    val isRefreshing:Boolean =false,
    val selectedRidesFilter :RidesFilter = RidesFilter.NEAREST,
    val allStateFilter:List<String> = listOf(),
    val allCityFilter: List<String> = listOf(),
    val stateCityFilter: Map<String,List<String>> = hashMapOf(),
    val selectedStateCity: SelectedStateCity = SelectedStateCity()
)

enum class RidesFilter{
    NEAREST,
    UPCOMING,
    PAST
}
