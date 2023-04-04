package com.example.appartamenty

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.example.appartamenty.data.Utility
import com.example.appartamenty.data.UtilityPrice
import com.example.appartamenty.ui.theme.AppartamentyTheme
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class CalculateRentActivity : ComponentActivity() {
    private val viewModel: UtilitiesViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {

        val tenantId = intent.getStringExtra("tenantId")

        super.onCreate(savedInstanceState)
        setContent {

            AppartamentyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (tenantId != null) {
                        SetRentData(tenantId)
                    }
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun SetRentData(tenantId: String) {

    val utilitiesList = mutableStateListOf<Utility?>()
    val readingsList = mutableStateListOf<MeterReading?>()
    val joinedList = mutableStateListOf<UtilityPrice?>()

    val database: FirebaseFirestore = FirebaseFirestore.getInstance()

    var utilityPrice = UtilityPrice()


    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    //find tenant's data
    db.collection("tenants").document(tenantId).get()
        .addOnSuccessListener { document ->
            val propertyId = document.get("propertyId")
            // retrieving utilities assigned to property
            db.collection("utilities").whereEqualTo("propertyId", propertyId).get()
                .addOnSuccessListener { utilities ->
                    Log.d(
                        LandlordListMeterReadings::class.java.simpleName,
                        "Utilities retrieved successfully"
                    )
                    for (utility in utilities) {
                        val utilityId = utility.id
                        val utility = utility.toObject(Utility::class.java)
                        utilitiesList.add(utility)
                        // retrieving meter readings for each utility
                        db.collection("meter_readings").whereEqualTo("utilityId", utilityId)
                            .orderBy("date", Query.Direction.DESCENDING).limit(2).get()
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
                                        val c: MeterReading? =
                                            reading.toObject(MeterReading::class.java)
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
        }


    ShowLazyListOfCalculations(utilitiesList, readingsList)


}

@Composable
fun ShowLazyListOfCalculations(
    utilitiesList: SnapshotStateList<Utility?>,
    readingsList: SnapshotStateList<MeterReading?>
) {

    // val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Rent",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.Start)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
//            LazyColumn {
//                itemsIndexed(utilitiesList) { index, utility ->
//                    if (utility != null) {
//                        UtilityCardItem(utility)
//                    }
//                }
//            }
            LazyColumn {
                itemsIndexed(readingsList) { index, reading ->
                    if (reading != null) {
                        ReadingCardItem(reading)
                    }
                }
            }
        }
        OutlinedButton(
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
                //TODO
            }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add new property")
            Spacer(modifier = Modifier.padding(horizontal = 5.dp))
            Text(text = stringResource(R.string.add_property))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UtilityCardItem(utility: Utility) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onSecondaryContainer),
        onClick = {
            //TODO
        }
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (utility.constant) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 10.dp),
                    text = "Utility name: " + utility.name.toString() + "\nPrice per month: " + utility.price.toString(),
                    textAlign = TextAlign.Center
                )
            } else {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 10.dp),
                    text = "Utility name: " + utility.name.toString() + "\nPrice per unit: " + utility.price.toString(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingCardItem(meterReading: MeterReading?) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onSecondaryContainer),
        onClick = {
            //TODO
        }
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (meterReading != null) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 10.dp),
                    text = "Date: ${meterReading.date} \n Value: ${meterReading.value}",
                            textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalculateRentPreview() {
    AppartamentyTheme {
        SetRentData("GqXmdTRxynWHhnnzJcSm5M6ei6b2")
    }
}