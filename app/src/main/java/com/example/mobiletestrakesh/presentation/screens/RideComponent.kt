package com.example.mobiletestrakesh.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mobiletestrakesh.domain.model.RidesItem
import com.example.mobiletestrakesh.ui.theme.FieldsTextColor
import com.example.mobiletestrakesh.ui.theme.MobileTestRakeshTheme


// TODO: Dynamically load the data and images
@Composable
fun SingleRideCard(rideItem:RidesItem) {
    Card(
        modifier = Modifier
            .wrapContentSize(),
        elevation = 10.dp,
        shape = RoundedCornerShape(10.dp),
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(horizontal = 26.dp, vertical = 22.dp)
        ) {

            AsyncImage(
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.FillBounds,
                model = rideItem.mapUrl,
                contentDescription = "map"
            )


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CityAndState(name = rideItem.city)
                CityAndState(name = rideItem.state)
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ContentRow(titleText = "Ride Id : ", desText = rideItem.id.toString())
                ContentRow(titleText = "Origin Station : ", desText = rideItem.originStation.toString())
                ContentRow(titleText = "Station Path : ", desText = rideItem.stationPath.toString())
                ContentRow(titleText = "Date : ", desText = rideItem.date)
                ContentRow(titleText = "Distance : ", desText = rideItem.distance.toString())
            }
        }

    }
}

@Composable
fun ContentRow(titleText: String,desText: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = titleText,
            style = MaterialTheme.typography.body1,
            color = FieldsTextColor
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = desText,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface
        )
    }
}

@Composable
fun CityAndState(name: String) {
    Text(
        modifier = Modifier
            .background(
                color = MaterialTheme.colors.secondary,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp),
        text = "$name",
        style = MaterialTheme.typography.body2,
        color = MaterialTheme.colors.onSurface
    )
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewSingleRide() {
    MobileTestRakeshTheme {
        SingleRideCard(
            rideItem = RidesItem(
                city = "dargling",
                date = "15 Feb 2022 04:05 PM",
                id = 20,
                mapUrl = "",
                originStation = 45,
                state = "Meghalaya",
                stationPath = listOf(25,54,23,13,69),
                distance = 2
            )
        )
    }
}