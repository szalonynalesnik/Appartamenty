package com.example.appartamenty

import android.annotation.SuppressLint
import android.content.Intent
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
import androidx.compose.runtime.mutableStateMapOf
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
import com.example.appartamenty.ui.theme.AppartamentyTheme
import com.google.firebase.firestore.FirebaseFirestore
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
    val joinedList = mutableStateMapOf<Number, Number>()
    // on below line creating variable for freebase database
    // and database reference.
    val database: FirebaseFirestore = FirebaseFirestore.getInstance()

    var tenant = database.collection("tenants").document(tenantId).get()
    tenant.addOnSuccessListener { doc ->
        if (!doc.exists()) {
            var propertyId = doc.get("propertyId")
            database.collection("utilities").whereEqualTo("propertyId", propertyId).orderBy("name")
                .get()
                .addOnSuccessListener { matchingUtilities ->
                    for (matchingUtility in matchingUtilities) {
                        val utility = matchingUtility.toObject(Utility::class.java)
                        utilitiesList.add(utility)
                        database.collection("meter_readings")
                            .whereEqualTo("utilityName", matchingUtility.get("name")).whereEqualTo("constant", true).get()
                            .addOnSuccessListener { matchingReadings ->
                                for (matchingReading in matchingReadings) {
                                    val meterReading =
                                        matchingReading.toObject(MeterReading::class.java)
                                    readingsList.add(meterReading)
                                    joinedList.set(utility.price,
                                        meterReading.value
                                    )
                                }
                            }
                        // add new meter reading to database
                    }
                }.addOnFailureListener {
                    Log.d(
                        MainActivity::class.java.simpleName,
                        "Could not find utility"
                    )
                }
        }
    }.addOnFailureListener {
        Log.d(
            MainActivity::class.java.simpleName,
            "Could not find tenant"
        )
    }

    // on below line getting data from our database

    // on below line we are calling method to display UI
    ShowLazyListOfCalculations(utilitiesList,readingsList)

}

@Composable
fun ShowLazyListOfCalculations(utilitiesList: SnapshotStateList<Utility?>, readingsList: SnapshotStateList<MeterReading?>) {

    // val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.properties),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.Start)
        )
        LazyColumn {
            itemsIndexed(utilitiesList) { index, utility ->
                if (utility != null) {
                    LazyColumn{
                    itemsIndexed(readingsList) { index, reading ->
                        if (reading != null) {
                            UtilityCardItem(utility, reading)
                        }
                    }
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
fun UtilityCardItem(utility: Utility, meterReading: MeterReading) {
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
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 10.dp),
                text = "Utility name: "+ utility.name.toString() + "\nPrice per unit: " + utility.price.toString() + "\nLast value: " + meterReading.value.toString(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    AppartamentyTheme {
       SetRentData("")
    }
}