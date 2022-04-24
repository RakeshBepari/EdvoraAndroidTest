package com.example.mobiletestrakesh.presentation

import com.example.mobiletestrakesh.other.Constants

/**
 * User action UI events used for encapsulating the
 * business logic in the ViewModel according to a user
 * action
 * */
sealed class RidesListsEvent {
    object Refresh : RidesListsEvent()
    data class SelectedRideFilter(val ridesFilter: RidesFilter) : RidesListsEvent()
    data class FilterStateCity(val selectedStateCity: SelectedStateCity) : RidesListsEvent()
}


/**
 * A data class which holds the current selected state and city in the filter
 * */
data class SelectedStateCity(
    val selectedState: String = Constants.DUMMY_STATE_FILTER,
    val selectedCity: String = Constants.DUMMY_CITY_FILTER
) : RidesListsEvent()
