package com.example.appartamenty

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import com.example.appartamenty.data.Property

import com.example.appartamenty.ui.theme.AppartamentyTheme
import com.google.firebase.firestore.FirebaseFirestore


class LandlordEditProperty : ComponentActivity() {

    private val database by lazy {
        FirebaseFirestore.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val landlordId = intent.getStringExtra("landlordId")
        val existingProperty = intent.extras?.get("property") as Property
        val destination = intent.getStringExtra("destination")
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
                        if (destination != null) {
                            LandlordEditPropertyMainScreen(
                                landlordId,
                                existingProperty,
                                destination
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun LandlordEditPropertyMainScreen(
    landlordId: String,
    existingProperty: Property,
    destination: String
) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current
        AddressEditForm(context, landlordId, existingProperty, destination)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressEditForm(
    context: Context,
    landlordId: String,
    existingProperty: Property,
    destination: String
) {

    val database = FirebaseFirestore.getInstance()
    val focusManager = LocalFocusManager.current

    var street by remember { mutableStateOf(existingProperty.street!!) }
    var streetNo by remember { mutableStateOf(existingProperty.streetNo!!) }
    var apartmentNo by remember { mutableStateOf(existingProperty.apartmentNo!!) }
    var postalCode by rememberSaveable { mutableStateOf(existingProperty.postalCode!!) }
    var city by rememberSaveable { mutableStateOf(existingProperty.city!!) }


    var updatedProperty: Property

    fun editProperty(
        street: String,
        streetNo: String,
        apartmentNo: String,
        postalCode: String,
        city: String,
        propertyId: String,
    ) {
        updatedProperty = Property(
            street = street,
            streetNo = streetNo,
            apartmentNo = apartmentNo,
            postalCode = postalCode,
            city = city,
            landlordId = landlordId,
            propertyId = propertyId
        )
        // add new property belonging to landlord to the database
        database.collection("properties").document(propertyId).set(updatedProperty)
            .addOnSuccessListener { doc ->
                Log.d(
                    MainActivity::class.java.simpleName,
                    "Adding property to database successful"
                )
                val intent = Intent(context, LandlordListPropertiesActivity::class.java)
                intent.putExtra("property", updatedProperty).putExtra("landlordId", landlordId)
                    .putExtra("destination", "list_properties")
                context.startActivity(intent)
            }
    }


    Column(
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = stringResource(R.string.edit_address),
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
        OutlinedButton(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth(),
            onClick = {
                val intent = Intent(context, LandlordListUtilities::class.java)
                intent.putExtra("landlordId", landlordId)
                intent.putExtra("destination", destination)
                intent.putExtra("property", existingProperty)
                context.startActivity(intent)
            }) {
            Text(text = stringResource(R.string.edit_utilities))
            Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "Show error icon")
        }
        OutlinedButton(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth(),
            onClick = {
                val intent = Intent(context, LandlordListUtilities::class.java)
                intent.putExtra("landlordId", landlordId)
                intent.putExtra("destination", destination)
                intent.putExtra("property", existingProperty)
                context.startActivity(intent)
            }) {
            Text(text = stringResource(R.string.manage_tenants))
            Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "Show error icon")
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                editProperty(
                    street = street,
                    streetNo = streetNo,
                    apartmentNo = apartmentNo,
                    postalCode = postalCode,
                    city = city,
                    propertyId = existingProperty.propertyId!!
                )
            }, shape = RoundedCornerShape(20.dp)
        ) {
            Text(text = stringResource(R.string.confirm))
        }

    }

}


@Preview(showBackground = true, showSystemUi = true, locale = "pl")
@Composable
fun LandlordEditPropertyPreview() {
    val existingProperty = Property(
        "70xF0AwhddGHRuSGYT7L",
        "Jozefa Rostafinskiego",
        "16",
        "17",
        "50-247",
        "Wroclaw",
        "Pth5PB4PlYSxtb6vYX4MVZRmen52"
    )
    com.example.appartamenty.ui.theme.AppartamentyTheme {
        LandlordEditPropertyMainScreen(
            "Pth5PB4PlYSxtb6vYX4MVZRmen52",
            existingProperty,
            "list_properties"
        )
    }
}