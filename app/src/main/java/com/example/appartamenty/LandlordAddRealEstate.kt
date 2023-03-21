package com.example.appartamenty

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
import com.example.appartamenty.composables.CustomOutlinedTextField
import com.example.appartamenty.data.Property
import com.example.appartamenty.ui.theme.ui.theme.AppartamentyTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class LandlordAddRealEstate : ComponentActivity() {
    private val auth by lazy {
        Firebase.auth
    }
    private val database by lazy {
        Firebase.database
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
                    LandlordAddRealEstateMainScreen(auth, database)
                }
            }
        }
    }
}

@Composable
fun LandlordAddRealEstateMainScreen(auth: FirebaseAuth, database: FirebaseDatabase) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AddressForm(auth, database)
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun UtilityChooser() {
//
//    var text by remember { mutableStateOf(TextFieldValue("")) }
//
//    val electricityCheckedState = remember { mutableStateOf(false) }
//    val gasCheckedState = remember { mutableStateOf(false) }
//    val waterCheckedState = remember { mutableStateOf(false) }
//    val internetCheckedState = remember { mutableStateOf(false) }
//
//
//    Column(
//        modifier = Modifier
//            .padding(vertical = 16.dp, horizontal = 16.dp),
//    ) {
//        Text(
//            text = "Utilities",
//            style = MaterialTheme.typography.titleLarge,
//            color = MaterialTheme.colorScheme.onBackground,
//            fontWeight = FontWeight.Normal,
//            modifier = Modifier
//                .padding(bottom = 8.dp)
//                .align(Alignment.Start)
//        )
//        Row(
//            modifier = Modifier
//                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Column(
//                modifier = Modifier
//                    .padding(vertical = 8.dp)
//                    .width(intrinsicSize = IntrinsicSize.Max),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally,
//            ) {
//                Switch(
//                    checked = electricityCheckedState.value,
//                    onCheckedChange = { electricityCheckedState.value = it },
//                    modifier = Modifier
//
//                )
//                Text(
//                    text = "Electricity",
//                    style = MaterialTheme.typography.labelLarge,
//                    color = MaterialTheme.colorScheme.onBackground,
//                    fontWeight = FontWeight.Normal,
//                    modifier = Modifier
//                )
//            }
//            if (electricityCheckedState.value == true) {
//                ValueField(label = "Price [PLN/kWh]", placeholder = "Enter value")
//            }
//        }
//        Row(
//            modifier = Modifier
//                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Column(
//                modifier = Modifier
//                    .padding(vertical = 8.dp),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally,
//            ) {
//                Switch(
//                    checked = gasCheckedState.value,
//                    onCheckedChange = { gasCheckedState.value = it },
//                    modifier = Modifier
//                )
//                Text(
//                    text = "Gas",
//                    style = MaterialTheme.typography.labelLarge,
//                    color = MaterialTheme.colorScheme.onBackground,
//                    fontWeight = FontWeight.Normal,
//                    modifier = Modifier,
//                )
//            }
//
//            if (gasCheckedState.value) {
//                ValueField(label = "Price [PLN/m3]", placeholder = "Enter value")
//            }
//        }
//        Row(
//            modifier = Modifier
//                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Column(
//                modifier = Modifier
//                    .padding(vertical = 8.dp),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally,
//            ) {
//                Switch(
//                    checked = waterCheckedState.value,
//                    onCheckedChange = { waterCheckedState.value = it }
//                )
//                Text(
//                    text = "Water",
//                    style = MaterialTheme.typography.labelLarge,
//                    color = MaterialTheme.colorScheme.onBackground,
//                    fontWeight = FontWeight.Normal,
//                    modifier = Modifier
//                        .padding(start = 8.dp),
//                )
//            }
//
//            if (waterCheckedState.value) {
//                ValueField(label = "Price [PLN/m3]", placeholder = "Enter value")
//            }
//        }
//        Row(
//            modifier = Modifier
//                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Column(
//                modifier = Modifier
//                    .padding(vertical = 8.dp),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally,
//            ) {
//                Switch(
//                    checked = internetCheckedState.value,
//                    onCheckedChange = { internetCheckedState.value = it }
//                )
//                Text(
//                    text = "Internet",
//                    style = MaterialTheme.typography.labelLarge,
//                    color = MaterialTheme.colorScheme.onBackground,
//                    fontWeight = FontWeight.Normal,
//                    modifier = Modifier
//                        .padding(start = 8.dp),
//                )
//            }
//            if (internetCheckedState.value) {
//                ValueField(label = "Price [monthly]", placeholder = "Enter value")
//            }
//        }
//    }
//}


//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ValueField(label: String, placeholder: String) {
//
//    var text by remember { mutableStateOf(TextFieldValue("")) }
//
//    TextField(
//        value = text,
//        onValueChange = { text = it },
//        label = { Text(label) },
//        placeholder = { Text(placeholder) },
//        modifier = Modifier
//            .padding(start = 16.dp),
//    )
//}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressForm(auth: FirebaseAuth, database: FirebaseDatabase) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    var database = FirebaseFirestore.getInstance()

    var street by remember { mutableStateOf("") }
    var houseNo by remember { mutableStateOf("") }
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
        houseNo: String,
        postalCode: String,
        city: String,
    ) {
        var landlordId = auth.currentUser?.uid
        property = Property(street, streetNo, houseNo, postalCode, city, landlordId.toString())
        if (landlordId != null) {
            val handle = database.collection("properties").document().set(property)
            handle.addOnSuccessListener {
                Log.d(
                    MainActivity::class.java.simpleName,
                    "Adding to database successful"
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .verticalScroll(scrollState),
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
                value = houseNo,
                label = { Text(text = stringResource(R.string.houseno)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                onValueChange = { houseNo = it },
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
            .padding(vertical = 16.dp, horizontal = 16.dp),
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
                     addProperty(street, houseNo, apartmentNo, postalCode, city)
    }, shape = RoundedCornerShape(20.dp)) {
        Text(text = stringResource(R.string.confirm))
    }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LandlordAddRealEstatePreview() {
    com.example.appartamenty.ui.theme.AppartamentyTheme {
        LandlordAddRealEstateMainScreen(Firebase.auth, Firebase.database)
    }
}