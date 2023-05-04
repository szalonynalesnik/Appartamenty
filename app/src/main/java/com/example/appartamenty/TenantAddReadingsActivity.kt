package com.example.appartamenty

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appartamenty.composables.CustomUtilityField
import com.example.appartamenty.data.MeterReading
import com.example.appartamenty.data.Utility
import com.example.appartamenty.data.UtilityPrice
import com.example.appartamenty.ui.theme.AppartamentyTheme
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*

class TenantAddReadingsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        val tenantId = intent.getStringExtra("tenantId")

        setContent {
            AppartamentyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (tenantId != null) {
                        TenantAddReadingsScreen(tenantId)
                    }
                }
            }
        }
    }
}

@Composable
fun TenantAddReadingsScreen(tenantId: String) {
    Column(
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.meter_readings),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.Start)
        )

        ReadingForm(tenantId)

    }
}

@Composable
fun ReadingForm(tenantId: String) {

    val context = LocalContext.current
    val database = FirebaseFirestore.getInstance()

    fun addReading(date: String, utilityName: String, value: Number) {
        Log.d(TenantAddReadingsActivity::class.java.simpleName, "Starting to add reading")
        // find tenant in database
        var tenant = database.collection("tenants").document(tenantId).get()

        tenant.addOnSuccessListener { doc ->
            if (doc.exists()) {
                Log.d(TenantAddReadingsActivity::class.java.simpleName, "Tenant retrieved")
                // find ID of property that tenant is assigned to
                var propertyId = doc.get("propertyId")
                // find utility object belonging to property
                database.collection("utilities").whereEqualTo("propertyId", propertyId)
                    .whereEqualTo("name", utilityName).get()
                    .addOnSuccessListener { matchingUtilities ->
                        Log.d(TenantAddReadingsActivity::class.java.simpleName, "Utility retrieved")
                        for (matchingUtility in matchingUtilities) {
                            var utilityId = matchingUtility.id
                            val meterReading = MeterReading(
                                date, utilityName,
                                value as Double, utilityId
                            )
                            // add new meter reading to database
                            var newMeterReading =
                                database.collection("meter_readings").add(meterReading)
                            newMeterReading.addOnSuccessListener {
                                Log.d(
                                    TenantAddReadingsActivity::class.java.simpleName,
                                    "Adding meter reading to database successful"
                                )
                            }
                                .addOnFailureListener {
                                    Log.d(
                                        TenantAddReadingsActivity::class.java.simpleName,
                                        "Could not add meter reading to database"
                                    )
                                }
                        }
                    }.addOnFailureListener {
                        Log.d(
                            TenantAddReadingsActivity::class.java.simpleName,
                            "Could not find utility"
                        )
                    }
            }
        }.addOnFailureListener {
            Log.d(
                TenantAddReadingsActivity::class.java.simpleName,
                "Could not find tenant"
            )
        }

    }

    fun addCalculation(datePicked: String, utilityName: String, newValue: Double, timestamp: Long) {
        var calculation = UtilityPrice()
        Log.d(TenantAddReadingsActivity::class.java.simpleName, "Starting to calculate usage")
        var tenant = database.collection("tenants").document(tenantId).get()
        tenant.addOnSuccessListener { doc ->
            if (doc.exists()) {
                Log.d(TenantAddReadingsActivity::class.java.simpleName, "Tenant retrieved")
                var propertyId = doc.get("propertyId")
                database.collection("utilities").whereEqualTo("propertyId", propertyId).whereEqualTo("name", utilityName).get()
                    .addOnSuccessListener { matchingUtilities ->
                        for (matchingUtility in matchingUtilities) {
                            var utilityId = matchingUtility.id
                            database.collection("meter_readings").whereEqualTo("utilityId", utilityId)
                                .orderBy("date", Query.Direction.DESCENDING).limit(1).get()
                                .addOnSuccessListener { documents ->
                                    for (document in documents) {
                                        val pastReading =
                                            document.toObject(MeterReading::class.java)
                                        database.collection("utilities").whereEqualTo("name", utilityName).get()
                                            .addOnSuccessListener { documents ->
                                                for (document in documents) {
                                                    val utility = document.toObject(Utility::class.java)
                                                    calculation = UtilityPrice(
                                                        utilityName,
                                                        newValue,
                                                        pastReading.value,
                                                        utility.price,
                                                        (newValue - pastReading.value) * utility.price,
                                                        timestamp,
                                                        propertyId.toString()
                                                    )
                                                    database.collection("monthly_calculations")
                                                        .add(calculation)
                                                        .addOnSuccessListener {
                                                            Log.d(
                                                                "SUCCESS",
                                                                "Calculation added successfully"
                                                            )
                                                        }
                                                        .addOnFailureListener {
                                                            Log.d("ERROR", "Calculation not added")
                                                        }
                                                }

                                            }.addOnFailureListener {
                                                Log.d("ERROR", "Could not find utilities")
                                            }
                                    }
                                }.addOnFailureListener {
                                    Log.d("ERROR", "Could not find meter readings")
                                }

                        }
                    }
            }
        }
    }


        val year: Int
        val month: Int
        val day: Int
        val focusManager = LocalFocusManager.current

        var datePicked by remember { mutableStateOf("None") }
        var electricityReading by rememberSaveable { mutableStateOf("") }
        var gasReading by rememberSaveable { mutableStateOf("") }
        var waterReading by rememberSaveable { mutableStateOf("") }


        val calendar = Calendar.getInstance()

        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)

        calendar.time = Date()

        val datePickerDialog = DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, day: Int ->
                datePicked =
                        //    String.format("%02d", day) + "/" + String.format("%02d", month + 1) + "/$year"
                    String.format(
                        "$year/" + String.format(
                            "%02d",
                            month + 1
                        ) + "/" + String.format("%02d", day)
                    )
            }, year, month, day
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(text = stringResource(R.string.readings_for_date) + "${datePicked}")
            Spacer(modifier = Modifier.size(16.dp))
            OutlinedButton(
                modifier = Modifier.padding(bottom = 24.dp),
                onClick = {
                    datePickerDialog.show()
                }) {
                Text(text = stringResource(R.string.choose_date))
            }
            CustomUtilityField(
                value = electricityReading,
                onValueChange = { electricityReading = it },
                label = stringResource(R.string.electricity_reading_label),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                showError = false,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
            CustomUtilityField(
                value = gasReading,
                onValueChange = { gasReading = it },
                label = stringResource(R.string.gas_reading_label),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                showError = false,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
            CustomUtilityField(
                value = waterReading,
                onValueChange = { waterReading = it },
                label = stringResource(R.string.water_reading_label),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                showError = false,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
            Button(
                modifier = Modifier.padding(top = 24.dp),
                onClick = {
                    val timestamp = System.currentTimeMillis()
                    if (electricityReading.toDouble() > 0) {
                        Log.d(
                            TenantAddReadingsActivity::class.java.simpleName,
                            "Adding electricity"
                        )

                        addReading(datePicked, "Electricity", electricityReading.toDouble())
                        addCalculation(datePicked, "Electricity", electricityReading.toDouble(), timestamp)

                    }
                    if (gasReading.toDouble() > 0) {
                        Log.d(TenantAddReadingsActivity::class.java.simpleName, "Adding gas")
                        addReading(datePicked, "Gas", gasReading.toDouble())
                        addCalculation(datePicked, "Gas", gasReading.toDouble(), timestamp)


                    }
                    if (waterReading.toDouble() > 0) {
                        Log.d(TenantAddReadingsActivity::class.java.simpleName, "Adding water")
                        addReading(datePicked, "Water", waterReading.toDouble())
                        addCalculation(datePicked, "Water", waterReading.toDouble(), timestamp)


                    }
                    val intent = Intent(context, MainScreenTenantActivity::class.java)
                    intent.putExtra("tenantId", tenantId)
                    context.startActivity(intent)
                }, shape = RoundedCornerShape(20.dp)
            ) {
                Text(text = stringResource(R.string.confirm))
            }

        }
    }

    @Preview(showBackground = true, showSystemUi = true, locale = "pl")
    @Composable
    fun TenantAddReadingsPreview() {

        AppartamentyTheme {
            TenantAddReadingsScreen("GqXmdTRxynWHhnnzJcSm5M6ei6b2")
        }
    }