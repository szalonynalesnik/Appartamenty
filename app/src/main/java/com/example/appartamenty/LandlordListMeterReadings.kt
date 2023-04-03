package com.example.appartamenty

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
        setContent {
            AppartamentyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun SetReadingsData(property: Property, landlordId: String) {

    val readingsList = mutableStateListOf<MeterReading?>()
    // on below line creating variable for freebase database
    // and database reference.
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // on below line getting data from our database
    db.collection("utilities").whereEqualTo("propertyId", property.propertyId).get()
        .addOnSuccessListener { utilities ->
            Log.d(
                LandlordListMeterReadings::class.java.simpleName,
                "Utilities retrieved successfully"
            )
            for (utility in utilities) {
                val utilityId = utility.id
                db.collection("meter_readings").whereEqualTo("utilityId", utilityId)
                    .orderBy("date", Query.Direction.ASCENDING).get()
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
                            // if the snapshot is not empty we are
                            // hiding our progress bar and adding
                            // our data in a list.
                            val list = queryDocumentSnapshots.documents
                            for (reading in list) {
                                // after getting this list we are passing that
                                // list to our object class.
                                val c: MeterReading? = reading.toObject(MeterReading::class.java)
                                // and we will pass this object class inside
                                // our arraylist which we have created for list view.
                                readingsList.add(c)
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
    ShowLazyListOfReadings(readingsList, landlordId)

}

@Composable
fun ShowLazyListOfReadings(readingsList: SnapshotStateList<MeterReading?>, landlordId: String) {

    val context = LocalContext.current
    readingsList.sortByDescending { it?.date }
    readingsList.sortBy { it?.utilityName }


    // var latestReadingsList = mutableListOf(readingsList[0], readingsList[1], readingsList[2])
    // var previousReadingsList = mutableListOf(readingsList[3], readingsList[4], readingsList[5])

    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Text(
                text = "Past meter readings",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.Start)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2)
            ) {
                items(readingsList.size) { index ->
                    ReadingCardItem(readingsList[index]!!, landlordId)
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingCardItem(reading: MeterReading, landlordId: String) {
    val context = LocalContext.current
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
//            val intent = Intent(context, LandlordPropertyDetails::class.java)
//            intent.putExtra("property", property).putExtra("landlordId", landlordId)
//            context.startActivity(intent)
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
                text = "${reading.date} \n ${reading.utilityName} \n Value: ${reading.value}",
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
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