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

    var uiState by mutableStateOf(RidesUserState())


    var ridesList: MutableList<RidesItem> = mutableListOf()

    //Rides
    var nearestRidesList: MutableList<RidesItem> = mutableListOf()
    var upcomingRidesList: MutableList<RidesItem> = mutableListOf()
    var pastRidesList: MutableList<RidesItem> = mutableListOf()

    //User
    var user: User = user()

    //State City
    var stateList: MutableList<String> = mutableListOf()
    var cityList: MutableList<String> = mutableListOf()
    var stateCityList: MutableMap<String, List<String>> = hashMapOf()

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

    private fun makeStateCityList() {

        stateList.add(DUMMY_STATE_FILTER)
        cityList.add(DUMMY_CITY_FILTER)

        nearestRidesList.forEach { ridesItem ->

            stateList.add(ridesItem.state)


            cityList.add(ridesItem.city)

            Log.d("CityStateLists", stateList.toString())


            if (stateCityList.containsKey(ridesItem.state)) {

                val mutableCityList = mutableSetOf<String>(ridesItem.city)

                stateCityList[ridesItem.state]?.let { mutableCityList.addAll(it) }

                Log.d("mCityStateLists", mutableCityList.toString())


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
        Log.d("CityStateLists", cityList.toString())
        Log.d("CityStateLists", stateCityList.toString())


    }

    fun onEvent(event: RidesListsEvent) {
        when (event) {
            is RidesListsEvent.Refresh -> {

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
            is RidesListsEvent.SelectedRideFilter -> {
                when (event.ridesFilter) {

                    RidesFilter.NEAREST -> {
                        uiState = uiState.copy(selectedRidesFilter = RidesFilter.NEAREST)
                        uiState = uiState.copy(ridesList = nearestRidesList)
                        //todo change the state state = state.copy( selectedStateCity = TO DEFAULT )
                    }

                    RidesFilter.UPCOMING -> {
                        uiState = uiState.copy(selectedRidesFilter = RidesFilter.UPCOMING)
                        uiState = uiState.copy(ridesList = upcomingRidesList)
                        upcomingFilter()
                        //todo change the state state = state.copy( selectedStateCity = TO DEFAULT )
                    }

                    RidesFilter.PAST -> {
                        uiState = uiState.copy(selectedRidesFilter = RidesFilter.PAST)
                        uiState = uiState.copy(ridesList = pastRidesList)
                        pastFilter()
                        //todo change the state state = state.copy( selectedStateCity = TO DEFAULT )
                    }

                }
            }
            is RidesListsEvent.FilterStateCity -> {
                Log.d(TAG+"Filter", event.selectedStateCity.toString())
                filterWithStateCity(event.selectedStateCity)

            }
        }
    }

    private fun filterWithStateCity(selectedStateCity: SelectedStateCity) {
        uiState = uiState.copy(selectedStateCity = selectedStateCity)  // Retaining the selected city and state string to show in ui after the dialog is dismissed and poped back up

        val isStateDummy = selectedStateCity.selectedState == DUMMY_STATE_FILTER
        val isCityDummy = selectedStateCity.selectedCity == DUMMY_CITY_FILTER

        if (isStateDummy && isCityDummy) {
            return
        }

        if (!isStateDummy) {
            filterState( selectedState =  selectedStateCity.selectedState)
            Log.d("MainViewModelFilter",selectedStateCity.toString())

        }

        if (!isCityDummy) {
            filterCity(selectedCity = selectedStateCity.selectedCity)
            Log.d("MainViewModelFilter",selectedStateCity.toString())

        }

    }

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

    private fun filterCity(selectedCity:String) {


        when(uiState.selectedRidesFilter){
            RidesFilter.NEAREST -> {

                val cityFilteredRides = nearestRidesList.filter { it.city == selectedCity }
                Log.d("stateFilteredRides",selectedCity)
                Log.d("stateFilteredRides",cityFilteredRides.toString())
                uiState = uiState.copy(ridesList = cityFilteredRides)

            }
            RidesFilter.UPCOMING -> {

                val cityFilteredRides = upcomingRidesList.filter { it.city == selectedCity }
                Log.d("stateFilteredRides",selectedCity)
                Log.d("stateFilteredRides",cityFilteredRides.toString())
                uiState = uiState.copy(ridesList = cityFilteredRides)

            }
            RidesFilter.PAST -> {

                val cityFilteredRides = pastRidesList.filter { it.city == selectedCity }
                Log.d("stateFilteredRides",selectedCity)
                Log.d("stateFilteredRides",cityFilteredRides.toString())
                uiState = uiState.copy(ridesList = cityFilteredRides)

            }
        }



    }

    private suspend fun getRides() {
        repository.getRides()
            .collect { result ->
                when (result) {
                    is Resource.Error -> TODO("showsnackbar for rides")
                    is Resource.Loading -> uiState = uiState.copy(isLoading = true)
                    is Resource.Success -> result.data?.let { ridesItemList ->
                        Log.d("RidesItemList", ridesItemList.toString())
                        uiState = uiState.copy(isLoading = false)
                        uiState = uiState.copy(ridesList = ridesItemList)
                        ridesList.addAll(ridesItemList)
                        Log.d("RidesItemList", ridesList.toString())
                    }
                }

            }
    }


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

    fun calculateNearestList(userStationCode: Int, rides: List<RidesItem>) {
        rides.map { ridesItem ->
            val distanceList = ridesItem.stationPath.map {
                abs(it - userStationCode)
            }
            val distance = Collections.min(distanceList)
            nearestRidesList.add(ridesItem.copy(distance = distance))
            Log.d(TAG, nearestRidesList.toString())

        }
        nearestRidesList.sortBy { it.distance }

        uiState = uiState.copy(ridesList = nearestRidesList)

        Log.d("RidesListINState", uiState.ridesList.toString())
        Log.d("RidesListINState", uiState.user.toString())
    }

    private fun upcomingFilter() {

        nearestRidesList.forEach { ridesItem ->
            val rideDate = FormatDateTime.getOnlyFormattedDate(ridesItem.date)
            val currentDate = FormatDateTime.getTodaysDate()
            if (rideDate.month > currentDate.month) {
                upcomingRidesList.add(ridesItem)
            } else if (rideDate.month == currentDate.month) {
                if (rideDate.date > currentDate.date) {
                    upcomingRidesList.add(ridesItem)
                }
            }
        }

        uiState = uiState.copy(ridesList = upcomingRidesList)
    }

    private fun pastFilter() {

        nearestRidesList.forEach { ridesItem ->
            val rideDate = FormatDateTime.getOnlyFormattedDate(ridesItem.date)
            val currentDate = FormatDateTime.getTodaysDate()
            if (rideDate.month < currentDate.month) {
                pastRidesList.add(ridesItem)
            } else if (rideDate.month == currentDate.month) {
                if (rideDate.date < currentDate.date) {
                    pastRidesList.add(ridesItem)
                }
            }
        }
        uiState = uiState.copy(ridesList = pastRidesList)
    }

}