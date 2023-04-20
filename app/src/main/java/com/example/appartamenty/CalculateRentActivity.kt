package com.example.appartamenty

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.example.appartamenty.data.Property
import com.example.appartamenty.data.Tenant
import com.example.appartamenty.data.Utility
import com.example.appartamenty.data.UtilityPrice
import com.example.appartamenty.ui.theme.AppartamentyTheme
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.Date

@ExperimentalCoroutinesApi
class CalculateRentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val tenantId = intent.getStringExtra("tenantId")

        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContent {

            AppartamentyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (tenantId != null) {
                        SetRentDataForTenant(tenantId)
                    }
                    else if (intent.extras?.get("property") != null){
                        val property = intent.extras?.get("property") as Property
                        SetRentDataForLandlord(property)
                    }
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun SetRentDataForTenant(tenantId: String) {

    val calculationsList = mutableStateListOf<UtilityPrice?>()
    var totalRent: Double = 0.0

    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    //find tenant's data
    db.collection("tenants").document(tenantId).get()
        .addOnSuccessListener { document ->
            val tenant = document.toObject(Tenant::class.java)
            if (tenant != null) {
                totalRent += tenant.rent!!
            }
            val propertyId = document.get("propertyId")
            // retrieving utilities assigned to property
            db.collection("monthly_calculations").whereEqualTo("propertyId", propertyId)
                .orderBy("timestamp", Query.Direction.DESCENDING).limit(3)
                .get()
                .addOnSuccessListener { calculations ->
                    Log.d(
                        "SUCCESS"::class.java.simpleName,
                        "Calculations retrieved successfully"
                    )
                    for (calculation in calculations) {
                        val calculation = calculation.toObject(UtilityPrice::class.java)
                        calculationsList.add(calculation)
                        totalRent += calculation.total
                        // retrieving meter readings for each utility
                    }

                    db.collection("utilities").whereEqualTo("propertyId", propertyId)
                        .whereEqualTo("constant", true).get()
                        .addOnSuccessListener { constantUtilities ->
                            for (utility in constantUtilities) {
                                val constantUtility = utility.toObject(Utility::class.java)
                                calculationsList.add(
                                    UtilityPrice(
                                        constantUtility.name!!,
                                        1.0,
                                        0.0,
                                        constantUtility.price,
                                        constantUtility.price,
                                        System.currentTimeMillis(),
                                        propertyId.toString()
                                    )
                                )
                                totalRent += constantUtility.price
                            }
                            calculationsList.sortedBy { "name" }
                            calculationsList.add(
                                UtilityPrice(
                                    "Total rent",
                                    0.0,
                                    0.0,
                                    0.0,
                                    totalRent,
                                    System.currentTimeMillis(),
                                    propertyId.toString()
                                )
                            )
                        }
                }.addOnFailureListener { }
        }

    ShowLazyListOfCalculations(calculationsList)


}

@SuppressLint("UnrememberedMutableState")
@Composable
fun SetRentDataForLandlord(property: Property) {

    val calculationsList = mutableStateListOf<UtilityPrice?>()
    var totalRent: Double = 0.0

    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    val propertyId = property.propertyId
    // retrieving utilities assigned to property

    db.collection("monthly_calculations").whereEqualTo("propertyId", propertyId)
        .orderBy("timestamp", Query.Direction.DESCENDING).limit(3)
        .get()
        .addOnSuccessListener { calculations ->
            Log.d(
                "SUCCESS"::class.java.simpleName,
                "Calculations retrieved successfully"
            )
            for (calculation in calculations) {
                val calculation = calculation.toObject(UtilityPrice::class.java)
                calculationsList.add(calculation)
                totalRent += calculation.total
                // retrieving meter readings for each utility
            }

            db.collection("utilities").whereEqualTo("propertyId", propertyId)
                .whereEqualTo("constant", true).get()
                .addOnSuccessListener { constantUtilities ->
                    for (utility in constantUtilities) {
                        val constantUtility = utility.toObject(Utility::class.java)
                        calculationsList.add(
                            UtilityPrice(
                                constantUtility.name!!,
                                1.0,
                                0.0,
                                constantUtility.price,
                                constantUtility.price,
                                System.currentTimeMillis(),
                                propertyId.toString()
                            )
                        )
                        totalRent += constantUtility.price
                    }

                    db.collection("tenants").whereEqualTo("propertyId", propertyId).get()
                        .addOnSuccessListener{ documents ->
                            for (document in documents) {
                                val tenant = document.toObject(Tenant::class.java)
                                totalRent += tenant.rent!!
                            }
                            calculationsList.sortedBy { "name" }
                            calculationsList.add(
                                UtilityPrice(
                                    "Total rent",
                                    0.0,
                                    0.0,
                                    0.0,
                                    totalRent,
                                    System.currentTimeMillis(),
                                    propertyId.toString()
                                )
                            )
                        }.addOnFailureListener {
                            calculationsList.sortedBy { "name" }
                            calculationsList.add(
                                UtilityPrice(
                                    "Total rent",
                                    0.0,
                                    0.0,
                                    0.0,
                                    totalRent,
                                    System.currentTimeMillis(),
                                    propertyId.toString()
                                )
                            )
                        }


                }

        }

    ShowLazyListOfCalculations(calculationsList)


}

@Composable
fun ShowLazyListOfCalculations(
    calculationsList: SnapshotStateList<UtilityPrice?>
) {

    // val context = LocalContext.current

    val sdf = SimpleDateFormat("dd/MM/yyyy")
    val currentDate = sdf.format(Date())

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.rent_for_date) + currentDate,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.Start)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        )
        {
            LazyColumn {
                itemsIndexed(calculationsList) { index, calculation ->
                    if (calculation != null) {
                        CalculationCardItem(calculation)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculationCardItem(calculation: UtilityPrice) {
    val context = LocalContext.current

    if (calculation.name == "Total rent") {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .width(IntrinsicSize.Max)
                .padding(vertical = 10.dp, horizontal = 10.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            ),
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
                    style = MaterialTheme.typography.bodyMedium,
                    text = stringResource(R.string.total_rent) + "\n" + String.format(
                        "%.2f",
                        calculation.total
                    ),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )

            }
        }
    } else if (calculation.name.endsWith("constant") || calculation.name == "Internet") {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .width(IntrinsicSize.Max)
                .padding(vertical = 10.dp, horizontal = 10.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
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
                    text = calculation.name + "\n" + stringResource(R.string.total) + String.format(
                        ": %.2f",
                        calculation.total
                    ),
                    textAlign = TextAlign.Center,
                )

            }
        }
    } else {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .width(IntrinsicSize.Max)
                .padding(vertical = 10.dp, horizontal = 10.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            ),
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
                    text = calculation.name + String.format(
                        "\n" + stringResource(R.string.usage) + ": %.3f",
                        (calculation.lastReading - calculation.previousReading)
                    ) + "\n" + stringResource(R.string.total) + String.format(
                        ": %.2f",
                        calculation.total
                    ),
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
        SetRentDataForTenant("GqXmdTRxynWHhnnzJcSm5M6ei6b2")
    }
}