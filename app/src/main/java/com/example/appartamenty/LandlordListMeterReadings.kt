package com.example.appartamenty

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appartamenty.data.MeterReading
import com.example.appartamenty.data.Property
import com.example.appartamenty.ui.theme.AppartamentyTheme
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class LandlordListMeterReadings : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val property = intent.extras?.get("property") as Property
        val landlordId = intent.extras?.getString("landlordId")
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContent {
            AppartamentyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (landlordId != null) {
                        SetReadingsData(property = property, landlordId = landlordId)
                    }
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun SetReadingsData(property: Property, landlordId: String) {

    val waterList = mutableStateListOf<MeterReading?>()
    val gasList = mutableStateListOf<MeterReading?>()
    val electricityList = mutableStateListOf<MeterReading?>()
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    db.collection("utilities").whereEqualTo("propertyId", property.propertyId).get()
        .addOnSuccessListener { utilities ->
            Log.d(
                LandlordListMeterReadings::class.java.simpleName,
                "Utilities retrieved successfully"
            )
            for (utility in utilities) {
                val utilityId = utility.id
                db.collection("meter_readings").whereEqualTo("utilityId", utilityId)
                    .orderBy("date", Query.Direction.DESCENDING).get()
                    .addOnSuccessListener { queryDocumentSnapshots ->
                        Log.d(
                            LandlordListMeterReadings::class.java.simpleName,
                            "Meter readings retrieved successfully"
                        )

                        if (!queryDocumentSnapshots.isEmpty) {
                            Log.d(
                                LandlordListMeterReadings::class.java.simpleName,
                                "Meter readings not empty"
                            )
                            val list = queryDocumentSnapshots.documents
                            for (reading in list) {
                                val c: MeterReading? = reading.toObject(MeterReading::class.java)
                                if (c!!.utilityName == "Water") {
                                    waterList.add(c)
                                } else if (c!!.utilityName == "Gas") {
                                    gasList.add(c)
                                }
                                if (c!!.utilityName == "Electricity") {
                                    electricityList.add(c)
                                }
                                Log.d(
                                    LandlordListMeterReadings::class.java.simpleName,
                                    "Added utility to list"
                                )
                            }
                        } else {
                            Log.d(
                                LandlordListMeterReadings::class.java.simpleName,
                                "No meter readings found"
                            )
                        }
                    }.addOnFailureListener {
                        Log.d(
                            LandlordListMeterReadings::class.java.simpleName,
                            "Could not retrieve meter readings"
                        )
                    }
            }
        }


    // on below line we are calling method to display UI
    ShowLazyListOfReadings(waterList, gasList, electricityList, landlordId)

}

@Composable
fun ShowLazyListOfReadings(
    waterList: SnapshotStateList<MeterReading?>,
    gasList: SnapshotStateList<MeterReading?>,
    electricityList: SnapshotStateList<MeterReading?>,
    landlordId: String
) {

    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp, horizontal = 16.dp),
//                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Text(
                text = stringResource(R.string.past_meter_readings),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Start)
            )
            Text(
                text = stringResource(R.string.water),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .align(Alignment.Start)
            )
            LazyColumn() {
                items(waterList.size) { index ->
                    ReadingCardItem(waterList[index]!!, landlordId)
                }
            }
            Text(
                text = stringResource(R.string.gas),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .align(Alignment.Start)
            )
            LazyColumn() {
                items(gasList.size) { index ->
                    ReadingCardItem(gasList[index]!!, landlordId)
                }
            }
            Text(
                text = stringResource(R.string.electricity),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .align(Alignment.Start)
            )
            LazyColumn() {
                items(electricityList.size) { index ->
                    ReadingCardItem(electricityList[index]!!, landlordId)
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingCardItem(reading: MeterReading, landlordId: String) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 10.dp, horizontal = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onSecondaryContainer),
        onClick = {
        }
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 10.dp),
                text = "${reading.date}",
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 10.dp),
                text = stringResource(R.string.value) + ": ${reading.value}",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LandlordListMeterReadingsPreview() {
    AppartamentyTheme {
        SetReadingsData(
            property = Property(
                "70xF0AwhddGHRuSGYT7L",
                "Jozefa Rostafinskiego",
                "16",
                "17",
                "50-247",
                "Wroclaw",
                "Pth5PB4PlYSxtb6vYX4MVZRmen52"
            ), landlordId = "Pth5PB4PlYSxtb6vYX4MVZRmen52"
        )
    }
}