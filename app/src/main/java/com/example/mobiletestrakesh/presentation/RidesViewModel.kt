package com.example.mobiletestrakesh.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobiletestrakesh.domain.model.RidesItem
import com.example.mobiletestrakesh.domain.model.User
import com.example.mobiletestrakesh.domain.model.user
import com.example.mobiletestrakesh.domain.repository.UserRidesRepository
import com.example.mobiletestrakesh.other.Constants.DUMMY_CITY_FILTER
import com.example.mobiletestrakesh.other.Constants.DUMMY_STATE_FILTER
import com.example.mobiletestrakesh.util.Resource
import com.example.mobiletestrakesh.util.filter.FormatDateTime
import com.example.mobiletestrakesh.util.filter.ListFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.math.abs

const val TAG = "MainViewModel"

@HiltViewModel
class RidesViewModel @Inject constructor(
    private val repository: UserRidesRepository
) : ViewModel() {

    var uiState by mutableStateOf(RidesUserState())  /** Holds all the UI states */


    private var ridesList: MutableList<RidesItem> = mutableListOf() /** Initial Rides list after a getting data from repository */

    // Rides
    /** Different type of rides which are used for filtering according to state city and clearing the
     * filter. Have different list so that we don't have to filter the raw list again and again*/
    private var nearestRidesList: MutableList<RidesItem> = mutableListOf()
    private var upcomingRidesList: MutableList<RidesItem> = mutableListOf()
    private var pastRidesList: MutableList<RidesItem> = mutableListOf()

    //User
    private var user: User = user()

    //State City
    /**Lists which are used for populating the state and city filter */
    private var stateList: MutableList<String> = mutableListOf()
    private var cityList: MutableList<String> = mutableListOf()
    private var stateCityList: MutableMap<String, List<String>> = hashMapOf()


    init {
        initialSetup()
    }

    private fun initialSetup(){
        viewModelScope.launch {

            val asyncRides = async { getRides() }
            val asyncUser = async { getUser() }

            asyncRides.await()
            asyncUser.await()
            Log.d("ridesListUSEr", ridesList.toString())
            Log.d("ridesListUSEr", user.toString())

            val nearListCalculation = async {
                calculateNearestList(
                    userStationCode = user.stationCode,
                    rides = ridesList
                )
            }

            nearListCalculation.await()

            makeStateCityList()
        }
    }

    fun onEvent(event: RidesListsEvent) {
        when (event) {
            is RidesListsEvent.Refresh -> {

                /** Refreshing the list Resets all the lists for the new rides lists and user */
                ridesList = mutableListOf()
                nearestRidesList = mutableListOf()
                upcomingRidesList = mutableListOf()
                pastRidesList = mutableListOf()

                user = user()

                stateList = mutableListOf()
                cityList = mutableListOf()
                stateCityList = hashMapOf()


                initialSetup()
            }
            is RidesListsEvent.SelectedRideFilter -> { /** Updates the the rides list according the selected ride filter*/
                when (event.ridesFilter) {

                    RidesFilter.NEAREST -> {
                        uiState = uiState.copy(selectedRidesFilter = RidesFilter.NEAREST)
                        uiState = uiState.copy(ridesList = nearestRidesList)
                        //can use state = state.copy( selectedStateCity = TO DEFAULT )
                    }

                    RidesFilter.UPCOMING -> {
                        uiState = uiState.copy(selectedRidesFilter = RidesFilter.UPCOMING)
//                        uiState = uiState.copy(ridesList = upcomingRidesList)
                        upcomingFilter()
                        //can use change the state state = state.copy( selectedStateCity = TO DEFAULT )
                    }

                    RidesFilter.PAST -> {
                        uiState = uiState.copy(selectedRidesFilter = RidesFilter.PAST)
//                        uiState = uiState.copy(ridesList = pastRidesList)
                        pastFilter()
                        //can use change the state state = state.copy( selectedStateCity = TO DEFAULT )
                    }

                }
            }
            is RidesListsEvent.FilterStateCity -> {
                /** Filters the current rides list whether it be nearest, upcoming or past
                 according to the combination of state and city*/
                filterWithStateCity(event.selectedStateCity)

            }
        }
    }


    /** Get rides list from the repository */
    private suspend fun getRides() {
        repository.getRides()
            .collect { result ->
                when (result) {
                    is Resource.Error -> Unit
                    is Resource.Loading -> uiState = uiState.copy(isLoading = true)
                    is Resource.Success -> result.data?.let { ridesItemList ->
                        uiState = uiState.copy(isLoading = false)
                        ridesList.addAll(ridesItemList)
                    }
                }

            }
    }

    /** Get user object form the repository*/
    private suspend fun getUser() {
        val userResource = repository.getUser()
        when (userResource) {
            is Resource.Error -> Unit
            is Resource.Loading -> Unit
            is Resource.Success -> userResource.data?.let {
                Log.d(TAG, it.toString())
                uiState = uiState.copy(user = it)
                user = it
            }
        }
    }

    /** Filter the Rides list form the api according to the distance*/
    private fun calculateNearestList(userStationCode: Int, rides: List<RidesItem>) {

        nearestRidesList.addAll(ListFilter.nearestFilter(rides,userStationCode))
        uiState = uiState.copy(ridesList = nearestRidesList)

    }

    /** Filters the list according to date from future and sorts it by distance*/
    private fun upcomingFilter() {

        upcomingRidesList.addAll(ListFilter.upcomingFilter(nearestRidesList))
        uiState = uiState.copy(ridesList = upcomingRidesList)
    }

    /** Filters the list according to date from past and sorts it by distance*/
    private fun pastFilter() {

        pastRidesList.addAll(ListFilter.pastFilter(nearestRidesList))
        uiState = uiState.copy(ridesList = pastRidesList)
    }

    /** Makes a hash map mapping the cities to their corresponding states*/
    private fun makeStateCityList() {

        stateList.add(DUMMY_STATE_FILTER)
        cityList.add(DUMMY_CITY_FILTER)

        nearestRidesList.forEach { ridesItem ->

            stateList.add(ridesItem.state)
            cityList.add(ridesItem.city)

            if (stateCityList.containsKey(ridesItem.state)) {
                val mutableCityList = mutableSetOf<String>(ridesItem.city)
                stateCityList[ridesItem.state]?.let { mutableCityList.addAll(it) }
                stateCityList[ridesItem.state] = mutableCityList.toList()
            } else {
                val newCityList = setOf<String>(ridesItem.city)
                stateCityList[ridesItem.state] = newCityList.toList()
            }
        }
        uiState = uiState.copy(
            allStateFilter = stateList.distinct().toList(),
            allCityFilter = cityList.distinct().toList(),
            stateCityFilter = stateCityList
        )

    }


    /** Filter the current list with combinations of the state and city filter selected*/
    private fun filterWithStateCity(selectedStateCity: SelectedStateCity) {
        uiState = uiState.copy(selectedStateCity = selectedStateCity)  // Retaining the selected city and state string to show in ui after the dialog is dismissed and poped back up

        val isStateDummy = selectedStateCity.selectedState == DUMMY_STATE_FILTER
        val isCityDummy = selectedStateCity.selectedCity == DUMMY_CITY_FILTER

        if (isStateDummy && isCityDummy) {
            /**If nothing is selected give the default list
            according to the ride selected*/
            when (uiState.selectedRidesFilter) {
                RidesFilter.NEAREST -> uiState = uiState.copy(ridesList = nearestRidesList)
                RidesFilter.UPCOMING -> uiState = uiState.copy(ridesList = upcomingRidesList)
                RidesFilter.PAST -> uiState = uiState.copy(ridesList = pastRidesList)
            }
            return
        }

        if (!isStateDummy) {
            filterState( selectedState =  selectedStateCity.selectedState)

        }

        if (!isCityDummy) {
            filterCity(selectedCity = selectedStateCity.selectedCity)

        }

    }

    /** Filters current ride list according to the current state selected*/
    private fun filterState(selectedState:String) {

        when(uiState.selectedRidesFilter){
            RidesFilter.NEAREST -> {

                val stateFilteredRides = nearestRidesList.filter { it.state == selectedState }
                Log.d("stateFilteredRides",selectedState)
                Log.d("stateFilteredRides",stateFilteredRides.toString())
                uiState = uiState.copy(ridesList = stateFilteredRides)

            }
            RidesFilter.UPCOMING -> {

                val stateFilteredRides = upcomingRidesList.filter { it.state == selectedState }
                Log.d("stateFilteredRides",selectedState)
                Log.d("stateFilteredRides",stateFilteredRides.toString())
                uiState = uiState.copy(ridesList = stateFilteredRides)

            }
            RidesFilter.PAST -> {

                val stateFilteredRides = pastRidesList.filter { it.state == selectedState }
                Log.d("stateFilteredRides",selectedState)
                Log.d("stateFilteredRides",stateFilteredRides.toString())
                uiState = uiState.copy(ridesList = stateFilteredRides)

            }
        }

    }
    /**Filters current rides list  according to the current selected city filter*/
    private fun filterCity(selectedCity:String) {
        when(uiState.selectedRidesFilter){
            RidesFilter.NEAREST -> {

                val cityFilteredRides = nearestRidesList.filter { it.city == selectedCity }
                uiState = uiState.copy(ridesList = cityFilteredRides)

            }
            RidesFilter.UPCOMING -> {

                val cityFilteredRides = upcomingRidesList.filter { it.city == selectedCity }
                uiState = uiState.copy(ridesList = cityFilteredRides)

            }
            RidesFilter.PAST -> {

                val cityFilteredRides = pastRidesList.filter { it.city == selectedCity }
                uiState = uiState.copy(ridesList = cityFilteredRides)

            }
        }
    }

}