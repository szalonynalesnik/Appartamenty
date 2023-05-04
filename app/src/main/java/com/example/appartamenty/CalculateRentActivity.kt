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
import com.example.appartamenty.data.TotalRentForLandlord
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
                    } else if (intent.extras?.get("property") != null) {
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
    var numberOfTenants: Int = 1
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
            db.collection("tenants").whereEqualTo("propertyId", propertyId).get()
                .addOnSuccessListener { tenants ->
                    for (tenant in tenants) {
                        numberOfTenants += 1
                    }
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
                                totalRent += (calculation.total) / numberOfTenants
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
                                        totalRent += (constantUtility.price) / numberOfTenants
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
        }

    ShowLazyListOfCalculations(calculationsList)


}

@SuppressLint("UnrememberedMutableState")
@Composable
fun SetRentDataForLandlord(property: Property) {

    val rentsForLandlord = mutableStateListOf<TotalRentForLandlord?>()
    var totalRentFromTenants: Double = 0.0
    var totalRentFromUsage: Double = 0.0
    var numberOfTenants: Int = 0
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    val propertyId = property.propertyId
    // retrieving utilities assigned to property

    db.collection("tenants").whereEqualTo("propertyId", propertyId).get()
        .addOnSuccessListener { tenants ->
            for (tenant in tenants) {
                numberOfTenants += 1
                val t = tenant.toObject(Tenant::class.java)
                totalRentFromTenants += t.rent!!
            }
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
                        totalRentFromUsage += calculation.total
                    }

                    db.collection("utilities").whereEqualTo("propertyId", propertyId)
                        .whereEqualTo("constant", true).get()
                        .addOnSuccessListener { constantUtilities ->
                            for (utility in constantUtilities) {
                                val constantUtility = utility.toObject(Utility::class.java)
                                totalRentFromUsage += constantUtility.price
                            }
                            var newTotalRentForLandlord = TotalRentForLandlord(
                                totalRentFromTenants = totalRentFromTenants,
                                totalRentFromUsage = totalRentFromUsage,
                                numberOfTenants = numberOfTenants,
                                totalRent = totalRentFromTenants + totalRentFromUsage,
                                timestamp = System.currentTimeMillis(),
                                propertyId = propertyId.toString()
                            )
                            rentsForLandlord.add(newTotalRentForLandlord)
                            db.collection("total_rents").add(newTotalRentForLandlord)
                        }.addOnFailureListener {

                        }

                }
        }


    ShowLazyListForLandlord(rentsForLandlord)


}

@Composable
fun ShowLazyListForLandlord(rentsForLandlord: SnapshotStateList<TotalRentForLandlord?>) {
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
                itemsIndexed(rentsForLandlord) { index, rent ->
                    if (rent != null) {
                        RentItem(rent)
                    }
                }
            }
        }
    }
}

@Composable
fun ShowLazyListOfCalculations(
    calculationsList: SnapshotStateList<UtilityPrice?>
) {
    
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

@Composable
fun RentItem(rent: TotalRentForLandlord) {
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
                text = stringResource(R.string.rent_from_usage) + String.format("%.2f", rent.totalRentFromUsage)
                        + "\n" + stringResource(R.string.rent_from_tenants) + String.format("%.2f", rent.totalRentFromTenants)
                        + "\n" + stringResource(R.string.total_rent_for_landlord)  + String.format("%.2f", rent.totalRent)
                        + "\n" + stringResource(R.string.number_of_tenants) + "${rent.numberOfTenants}",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal
            )

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculationCardItem(calculation: UtilityPrice) {

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
        val property = Property(
            "70xF0AwhddGHRuSGYT7L",
            "Jozefa Rostafinskiego",
            "16",
            "17",
            "50-247",
            "Wroclaw",
            "Pth5PB4PlYSxtb6vYX4MVZRmen52"
        )
        //SetRentDataForTenant("GqXmdTRxynWHhnnzJcSm5M6ei6b2")
        SetRentDataForLandlord(property)
    }
}