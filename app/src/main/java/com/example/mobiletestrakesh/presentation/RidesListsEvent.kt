package com.example.mobiletestrakesh.presentation

import com.example.mobiletestrakesh.other.Constants

sealed class RidesListsEvent {
    object Refresh : RidesListsEvent()
    data class SelectedRideFilter(val ridesFilter: RidesFilter) : RidesListsEvent()
    data class FilterStateCity(val selectedStateCity: SelectedStateCity) : RidesListsEvent()
}

data class SelectedStateCity(
    val selectedState: String = Constants.DUMMY_STATE_FILTER,
    val selectedCity: String = Constants.DUMMY_CITY_FILTER
) : RidesListsEvent()
