package com.example.mobiletestrakesh.presentation.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.mobiletestrakesh.R
import com.example.mobiletestrakesh.other.Constants
import com.example.mobiletestrakesh.other.Constants.DUMMY_CITY_FILTER
import com.example.mobiletestrakesh.other.Constants.DUMMY_STATE_FILTER
import com.example.mobiletestrakesh.presentation.SelectedStateCity
import com.example.mobiletestrakesh.ui.theme.DialogFilter
import com.example.mobiletestrakesh.ui.theme.DropDownColor
import com.example.mobiletestrakesh.ui.theme.HorizontalDivider
import com.example.mobiletestrakesh.ui.theme.MobileTestRakeshTheme


@Composable
fun FilterDialog(
    allStateList: Set<String>,
    allCityList: Set<String>,
    stateCityList: Map<String, Set<String>>,
    selectedStateCity: SelectedStateCity,
    filterSelectedStateCity: (SelectedStateCity) -> Unit,
    onDismiss: () -> Unit
) {
    var localSelectedStateCity by rememberSaveable {
        mutableStateOf(SelectedStateCity())
    }

    localSelectedStateCity = selectedStateCity


    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier.padding(vertical = 24.dp, horizontal = 30.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Filters",
                style = MaterialTheme.typography.subtitle1
            )

            Divider(
                color = HorizontalDivider,
                modifier = Modifier.fillMaxWidth(),
                thickness = 2.dp
            )

            Spacer(modifier = Modifier.padding(1.dp))


            /** State DropDown*/
            StateDropDown(allStateList.toList(), stateSelected = "", selectedDropdown = { selState ->
                localSelectedStateCity = localSelectedStateCity.copy(selectedState = selState)
            })


            /**City Dropdown*/
            if (localSelectedStateCity.selectedState.lowercase()
                    .trim() == DUMMY_STATE_FILTER.lowercase()
                    .trim()
            ) {
                CityDropDown(
                    list = allCityList.toList(),
                    selectedDropdown = { selCity ->
                        localSelectedStateCity = localSelectedStateCity.copy(selectedCity = selCity)
                    }
                )

            } else {
                val list = stateCityList[localSelectedStateCity.selectedState]?.toList()
                Log.d("UIFilterCheck", list.toString())
                CityDropDown(
                    list = list!!,
                    selectedDropdown = { selCity ->
                        localSelectedStateCity = localSelectedStateCity.copy(selectedCity = selCity)
                    }
                )
            }

            Button(
                onClick = {
                    filterSelectedStateCity(localSelectedStateCity)
                    onDismiss()
                }
            ) {
                Text(text = "Apply")
            }


        }
    }
}

@Composable
fun DemoFilterDialog(
    allStateList: List<String>,
    allCityList: List<String>,
    stateCityList: Map<String, List<String>>,
    filterSelectedStateCity: (SelectedStateCity) -> Unit,
//    selectedStateCity: SelectedStateCity,// todo use this instead of local remembers
    onDismiss: () -> Unit
) {

    var stateSelected by rememberSaveable {
        mutableStateOf(DUMMY_STATE_FILTER)
    }
    var citySelected by rememberSaveable {
        mutableStateOf(DUMMY_CITY_FILTER)
    }

    Log.d("chSELECTEDSTATE",stateSelected)
    Log.d("chSELECTEDSTATE",citySelected)


    var cityList by rememberSaveable {
        mutableStateOf(allCityList)
    }

    Log.d("chSELECTEDSTATE",cityList.toString())



    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier.padding(vertical = 24.dp, horizontal = 30.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Filters",
                style = MaterialTheme.typography.subtitle1
            )

            Divider(
                color = HorizontalDivider,
                modifier = Modifier.fillMaxWidth(),
                thickness = 2.dp
            )

            Spacer(modifier = Modifier.padding(1.dp))


            /**State DropDown*/
            StateDropDown(allStateList,stateSelected= stateSelected, selectedDropdown = { selState ->
                stateSelected = selState
            })

            LaunchedEffect(key1 = stateSelected){
                if (stateSelected == DUMMY_STATE_FILTER) {
                    cityList = allCityList
                } else {
                    val cityListWithDummy = mutableListOf(DUMMY_CITY_FILTER)
                    cityListWithDummy.addAll(stateCityList[stateSelected] ?: allCityList)
                    cityList = cityListWithDummy
                    Log.d("SELECTEDSTATE",stateSelected)
                    Log.d("SELECTEDSTATE", cityList.toString())

                }
            }

            /**City DropDown*/
            CityDropDown(cityList, selectedDropdown = { selCity ->
                citySelected = selCity
            })

            Button(
                onClick = {
                    val selectedStateCity = SelectedStateCity(selectedState = stateSelected, selectedCity = citySelected)
                    filterSelectedStateCity(selectedStateCity)
                    onDismiss()
                }
            ) {
                Text(text = "Apply", color = MaterialTheme.colors.onSurface)
            }
        }
    }
}

@Composable
fun StateDropDown(list: List<String>,stateSelected : String, selectedDropdown: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }
    Column(
        modifier = Modifier
//            .fillMaxSize()
//            .wrapContentSize(Alignment.TopStart)
            .wrapContentSize()
            .background(
                color = DropDownColor,
                shape = RoundedCornerShape(5.dp)
            )
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DropDownColor)
                .clickable(onClick = { expanded = true })
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = stateSelected,
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .background(DropDownColor),
                color = MaterialTheme.colors.onSurface
            )


            Icon(
                painter = painterResource(
                    if (expanded)
                        R.drawable.ic_up_arrow
                    else
                        R.drawable.ic_down_arrow
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(12.dp, 12.dp),
                tint = DialogFilter
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(300.dp)
                .background(
                    DropDownColor.copy(0.5f)
                )
        ) {
            list.forEachIndexed { index, text ->
                DropdownMenuItem(onClick = {
                    selectedDropdown(text)
                    selectedIndex = index
                    expanded = false
                }) {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.subtitle2,
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }
        }
    }
}


@Composable
fun CityDropDown(list: List<String>, selectedDropdown: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }
    Column(
        modifier = Modifier
//            .fillMaxSize()
//            .wrapContentSize(Alignment.TopStart)
            .wrapContentSize()
            .background(
                color = DropDownColor,
                shape = RoundedCornerShape(5.dp)
            )
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DropDownColor)
                .clickable(onClick = { expanded = true })
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = list[selectedIndex],
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .background(DropDownColor),
                color = MaterialTheme.colors.onSurface
            )


            Icon(
                painter = painterResource(
                    if (expanded)
                        R.drawable.ic_up_arrow
                    else
                        R.drawable.ic_down_arrow
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(12.dp, 12.dp),
                tint = DialogFilter
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(300.dp)
                .background(
                    DropDownColor.copy(0.5f)
                )
        ) {
            list.forEachIndexed { index, text ->
                DropdownMenuItem(onClick = {
                    selectedDropdown(text)
                    selectedIndex = index
                    expanded = false
                }) {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.subtitle2,
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun ShowDropDown() {
    MobileTestRakeshTheme {
        StateDropDown(
            listOf("Billing Period", "Annual", "Monthly", "One-Time Payment"),
            stateSelected = "",
            ){

        }
    }
}
