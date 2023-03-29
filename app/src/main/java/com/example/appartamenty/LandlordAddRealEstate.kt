package com.example.appartamenty

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.appartamenty.R.string.streetNo
import com.example.appartamenty.composables.CustomOutlinedTextField
import com.example.appartamenty.data.Property
import com.example.appartamenty.data.Utility
import com.example.appartamenty.ui.theme.ui.theme.AppartamentyTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.Objects

class LandlordAddRealEstate : ComponentActivity() {
    private val auth by lazy {
        Firebase.auth
    }
    private val database by lazy {
        FirebaseFirestore.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val landlordId = intent.getStringExtra("landlordId")
        super.onCreate(savedInstanceState)
        setContent {
            AppartamentyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (landlordId != null) {
                        LandlordAddRealEstateMainScreen(auth, landlordId)
                    }
                }
            }
        }
    }
}

@Composable
fun LandlordAddRealEstateMainScreen(auth: FirebaseAuth, landlordId: String) {

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AddressForm(auth, landlordId)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressForm(auth: FirebaseAuth, landlordId: String) {

    val database = FirebaseFirestore.getInstance()
    val focusManager = LocalFocusManager.current

    var street by remember { mutableStateOf("") }
    var streetNo by remember { mutableStateOf("") }
    var apartmentNo by remember { mutableStateOf("") }
    var postalCode by rememberSaveable { mutableStateOf("") }
    var city by rememberSaveable { mutableStateOf("") }

    var electricityPrice by rememberSaveable { mutableStateOf("") }
    var gasPrice by rememberSaveable { mutableStateOf("") }
    var waterPrice by rememberSaveable { mutableStateOf("") }
    var internetPrice by rememberSaveable { mutableStateOf("") }

    var property: Property

    fun addProperty(
        street: String,
        streetNo: String,
        apartmentNo: String,
        postalCode: String,
        city: String,
    ) {
        property = Property(street = street, streetNo = streetNo, apartmentNo = apartmentNo, postalCode = postalCode, city = city, landlordId = landlordId)
            // add new property belonging to landlord to the database
            var newProperty = database.collection("properties").add(property)
            newProperty.addOnSuccessListener { doc ->
                Log.d(
                    MainActivity::class.java.simpleName,
                    "Adding property to database successful"
                )
            }
        }



    fun addUtility(
        constant: Boolean,
        name: String,
        price: Number,
        street: String,
        streetNo: String,
        apartmentNo: String,
        postalCode: String,
    ) {
        // find property belonging to landlord
        var queriedProperty =
            database.collection("properties").whereEqualTo("landlordId", landlordId).whereEqualTo("street", street).whereEqualTo("streetNo",streetNo).whereEqualTo("apartmentNo", apartmentNo).whereEqualTo("postalCode",postalCode).get()
        queriedProperty.addOnSuccessListener { documents ->
            for (document in documents) {
                // get propertyId
                var propertyId = document.id
                val utility = Utility(constant, name, price, propertyId)
                // add new utility to database
                var newUtility = database.collection("utilities").add(utility)
                newUtility.addOnSuccessListener {
                    Log.d(
                        MainActivity::class.java.simpleName,
                        "Adding utility to database successful"
                    )
                }
            }

        }
    }
    Column(
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = stringResource(R.string.address),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.Start)
        )
        Text(
            text = "Address of the property",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.Start)
        )

        OutlinedTextField(
            value = street,
            label = { Text(text = stringResource(R.string.street)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            onValueChange = { street = it },
            modifier = Modifier
                .fillMaxWidth(),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = streetNo,
                label = { Text(text = stringResource(R.string.streetNo)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                onValueChange = { streetNo = it },
                modifier = Modifier
                    .weight(1f)
            )
            Spacer(
                modifier = Modifier
                    .weight(0.1f)
            )
            OutlinedTextField(
                value = apartmentNo,
                label = { Text(text = stringResource(R.string.aptno)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                onValueChange = { apartmentNo = it },
                modifier = Modifier
                    .weight(1f)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = postalCode,
                label = { Text(text = stringResource(R.string.postcode)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                onValueChange = { postalCode = it },
                modifier = Modifier
                    .weight(1f)
            )
            Spacer(
                modifier = Modifier
                    .weight(0.1f)
            )
            OutlinedTextField(
                value = city,
                label = { Text(text = stringResource(R.string.city)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                onValueChange = { city = it },
                modifier = Modifier
                    .weight(1f)
            )
        }

    }
    Column(
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        Text(
            text = "Utilities",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.Start)
        )
        Text(
            text = "Utilities available in the property",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.Start)
        )
        CustomOutlinedTextField(
            value = electricityPrice,
            onValueChange = { electricityPrice = it },
            label = stringResource(R.string.electricity),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            leadingIconImageVector = Icons.Default.ElectricBolt,
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        CustomOutlinedTextField(
            value = gasPrice,
            onValueChange = { gasPrice = it },
            label = stringResource(R.string.gas),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            leadingIconImageVector = Icons.Default.PropaneTank,
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        CustomOutlinedTextField(
            value = waterPrice,
            onValueChange = { waterPrice = it },
            label = stringResource(R.string.water),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            leadingIconImageVector = Icons.Default.WaterDrop,
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        CustomOutlinedTextField(
            value = internetPrice,
            onValueChange = { internetPrice = it },
            label = stringResource(R.string.internet),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            leadingIconImageVector = Icons.Default.Wifi,
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )


    }

    Button(onClick = {
        addProperty(street = street, streetNo = streetNo, apartmentNo = apartmentNo, postalCode = postalCode, city = city)
        if (electricityPrice.toFloat() > 0) {
            addUtility(constant = false, name = "Electricity", price = electricityPrice.toFloat(), street = street, streetNo = streetNo, apartmentNo = apartmentNo, postalCode = postalCode)
        }
        if (gasPrice.toFloat() > 0) {
            addUtility(constant = false, name = "Gas", price = gasPrice.toFloat(), street = street, streetNo = streetNo, apartmentNo = apartmentNo, postalCode = postalCode)
        }
        if (waterPrice.toFloat() > 0) {
            addUtility(constant = false, name = "Water", price = waterPrice.toFloat(), street = street, streetNo = streetNo, apartmentNo = apartmentNo, postalCode = postalCode)
        }
        if (internetPrice.toFloat() > 0) {
            addUtility(constant = true, name = "Internet", price = internetPrice.toFloat(), street = street, streetNo = streetNo, apartmentNo = apartmentNo, postalCode = postalCode)
        }
    }, shape = RoundedCornerShape(20.dp)) {
        Text(text = stringResource(R.string.confirm))
    }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LandlordAddRealEstatePreview() {
    com.example.appartamenty.ui.theme.AppartamentyTheme {
        LandlordAddRealEstateMainScreen(Firebase.auth, landlordId = "XJWXUFoiAEV0efxdGpPrdDNVS3M2")
    }
}