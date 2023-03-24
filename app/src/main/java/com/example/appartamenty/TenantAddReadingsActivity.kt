package com.example.appartamenty

import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appartamenty.composables.CustomOutlinedTextField
import com.example.appartamenty.composables.CustomUtilityField
import com.example.appartamenty.data.MeterReading
import com.example.appartamenty.data.Property
import com.example.appartamenty.data.Utility
import com.example.appartamenty.ui.theme.AppartamentyTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*

class TenantAddReadingsActivity : ComponentActivity() {
    private val auth by lazy {
        Firebase.auth
    }
    private val database by lazy {
        FirebaseFirestore.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppartamentyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TenantAddReadingsScreen(auth, database)
                }
            }
        }
    }
}

@Composable
fun TenantAddReadingsScreen(auth: FirebaseAuth, database: FirebaseFirestore) {
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

        ReadingForm(auth, database)

    }
}

@Composable
fun ReadingForm(auth: FirebaseAuth, database: FirebaseFirestore) {

    val context = LocalContext.current

    fun addReading(date: String, utilityName: String, value: Number) {
        val landlordId = auth.currentUser?.uid.toString()
        // find property assigned to landlord
        var properties =
            database.collection("properties").whereEqualTo("landlordId", landlordId).get()
        properties.addOnSuccessListener { matchingProperties ->
            for (matchingProperty in matchingProperties) {
                var propertyId = matchingProperty.id
                // find matching utility for the property
                var utilities =
                    database.collection("utilities").whereEqualTo("propertyId", propertyId)
                        .whereEqualTo("name", utilityName).get()
                utilities.addOnSuccessListener { matchingUtilities ->
                    for (matchingUtility in matchingUtilities) {
                        var utilityId = matchingUtility.id
                        val meterReading = MeterReading(date, utilityName, value, utilityId)
                        // add new meter reading to database
                        var newMeterReading =
                            database.collection("meter_readings").add(meterReading)
                        newMeterReading.addOnSuccessListener {
                            Log.d(
                                MainActivity::class.java.simpleName,
                                "Adding meter reading to database successful"
                            )
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
                String.format("%02d", day) + "/" + String.format("%02d", month + 1) + "/$year"
        }, year, month, day
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(text = "Readings for date: ${datePicked}")
        Spacer(modifier = Modifier.size(16.dp))
        OutlinedButton(
            modifier = Modifier.padding(bottom = 16.dp),
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
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
            if (electricityReading.toFloat() > 0) {
                addReading(datePicked, "Electricity", electricityReading.toFloat())
            }
            if (gasReading.toFloat() > 0) {
                addReading(datePicked, "Gas", gasReading.toFloat())
            }
            if (waterReading.toFloat() > 0) {
                addReading(datePicked, "Water", waterReading.toFloat())
            }
        }, shape = RoundedCornerShape(20.dp)) {
            Text(text = stringResource(R.string.confirm))
        }

    }
}

@Preview(showBackground = true, showSystemUi = true, locale = "pl")
@Composable
fun TenantAddReadingsPreview() {

    AppartamentyTheme {
        TenantAddReadingsScreen(Firebase.auth, FirebaseFirestore.getInstance())
    }
}