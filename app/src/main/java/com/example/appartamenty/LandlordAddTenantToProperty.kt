package com.example.appartamenty

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PermIdentity
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appartamenty.composables.CustomOutlinedTextField
import com.example.appartamenty.data.Landlord
import com.example.appartamenty.data.Tenant
import com.example.appartamenty.ui.theme.AppartamentyTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class LandlordAddTenantToProperty : ComponentActivity() {

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
                    AddTenantForm(auth, database)
                }
            }
        }
    }
}

@Composable
fun AddTenantForm(auth: FirebaseAuth, database: FirebaseDatabase) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()


    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var rent by remember { mutableStateOf("") }

    var validateEmail by remember { mutableStateOf(true) }
    var validateFirstName by remember { mutableStateOf(true) }
    var validateLastName by remember { mutableStateOf(true) }
    var validateRent by remember { mutableStateOf(true) }

    var validateEmailError = "The email address format is invalid."
    var validateRentError =
        "Rent cannot be blank."

    var database = FirebaseFirestore.getInstance()

    fun validateData(
        firstName: String,
        lastName: String,
        email: String,
        rent: String
    ): Boolean {

        validateFirstName = firstName.isNotBlank()
        validateLastName = lastName.isNotBlank()
        validateEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        validateRent = rent.isNotBlank()

        return validateFirstName && validateLastName && validateEmail && validateRent
    }

    fun addTenant(
        firstName: String,
        lastName: String,
        email: String,
        rent: Number,
    ) {
        if (validateData(firstName, lastName, email, rent.toString())) {
            auth.createUserWithEmailAndPassword(email, "not-set")
            // get landlord ID
            var landlordId = auth.currentUser?.uid

            // find property belonging to landlord
            var property =
                database.collection("properties").whereEqualTo("landlordId", landlordId).get()
            property.addOnSuccessListener { matchedProperties ->
                for (matchedProperty in matchedProperties) {
                    // get propertyId
                    var propertyId = matchedProperty.id
                    val tenant = Tenant(firstName, lastName, email, rent, propertyId)
                            val handle = database.collection("tenants").add(tenant)
                            handle.addOnSuccessListener {
                                Log.d(
                                    MainActivity::class.java.simpleName,
                                    "Adding tenant to database successful"
                                )
                            handle.addOnFailureListener {
                                Log.d(
                                    MainActivity::class.java.simpleName,
                                    "Adding tenant to database failed"
                                )
                            }
                        }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.add_tenant_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(bottom = 24.dp)
                .align(Alignment.Start)
        )
        CustomOutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = stringResource(R.string.emailaddress),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            showError = !validateEmail,
            errorMessage = validateEmailError,
            leadingIconImageVector = Icons.Default.Email,
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        CustomOutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = stringResource(R.string.firstname),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            showError = !validateFirstName,
            leadingIconImageVector = Icons.Default.PermIdentity,
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        CustomOutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = stringResource(R.string.lastname),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            showError = !validateLastName,
            leadingIconImageVector = Icons.Default.PermIdentity,
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        CustomOutlinedTextField(
            value = rent,
            onValueChange = { rent = it },
            label = stringResource(R.string.rent),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            showError = !validateRent,
            leadingIconImageVector = Icons.Default.Payments,
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        Button(
            onClick = {
                addTenant(firstName, lastName, email, rent.toFloat())
            },
            modifier = Modifier
                .padding(top = 24.dp)
                .width(intrinsicSize = IntrinsicSize.Max),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 10.dp),
                text = stringResource(R.string.add_tenant),
                textAlign = TextAlign.Center
            )

        }
    }
}


@Preview(showBackground = true, showSystemUi = true, locale = "pl")
@Composable
fun AddTenantsPreview() {
    AppartamentyTheme {
        AddTenantForm(Firebase.auth, FirebaseDatabase.getInstance())
    }
}