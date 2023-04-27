package com.example.appartamenty

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.appartamenty.data.Tenant
import com.example.appartamenty.data.Utility
import com.example.appartamenty.ui.theme.AppartamentyTheme
import com.google.firebase.firestore.FirebaseFirestore

class LandlordEditTenantActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val landlordId = intent.getStringExtra("landlordId")
        val property = intent.extras?.get("property") as Property
        val destination = intent.getStringExtra("destination")
        val tenant = intent.extras?.get("tenant") as Tenant

        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContent {
            AppartamentyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LandlordEditTenantMainScreen(
                        landlordId = landlordId!!,
                        existingTenant = tenant,
                        destination = destination!!,
                        property = property
                    )
                }
            }
        }
    }
}

@Composable
fun LandlordEditTenantMainScreen(
    landlordId: String,
    existingTenant: Tenant,
    destination: String,
    property: Property
) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TenantEditForm(
            existingTenant = existingTenant,
            landlordId = landlordId,
            property = property,
            destination = destination
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantEditForm(
    existingTenant: Tenant,
    landlordId: String,
    property: Property,
    destination: String
) {

    val database = FirebaseFirestore.getInstance()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    var firstName by remember { mutableStateOf(existingTenant.firstName!!) }
    var lastName by remember { mutableStateOf(existingTenant.lastName!!) }
    var rent by remember { mutableStateOf(existingTenant.rent!!.toString()) }

    var updatedTenant: Tenant

    fun editTenant(
        firstName: String,
        lastName: String,
        rent: Double
    ) {
        updatedTenant = Tenant(
            firstName = firstName,
            lastName = lastName,
            rent = rent,
            tenantId = existingTenant.tenantId,
            propertyId = existingTenant.propertyId,
            email = existingTenant.email
        )
        database.collection("tenants").document(existingTenant.tenantId!!)
            .set(updatedTenant)
            .addOnSuccessListener { doc ->
                Log.d(
                    MainActivity::class.java.simpleName,
                    "Updating tenant successful"
                )
                val intent = Intent(context, LandlordListTenantsActivity::class.java)
                intent.putExtra("landlordId", landlordId)
                    .putExtra("destination", destination)
                    .putExtra("property", property)
                context.startActivity(intent)
            }
    }

    fun deleteTenant(
    ) {
        database.collection("tenants").document(existingTenant.tenantId!!).delete()
            .addOnSuccessListener { doc ->
                Log.d(
                    MainActivity::class.java.simpleName,
                    "Updating tenant successful"
                )
                val intent = Intent(context, LandlordListTenantsActivity::class.java)
                intent.putExtra("landlordId", landlordId)
                    .putExtra("destination", destination)
                    .putExtra("property", property)
                context.startActivity(intent)
            }
    }


    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = stringResource(R.string.edit_tenant),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.Start)
        )
    }
    Column(
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text(text = stringResource(R.string.firstname)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
        )
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text(text = stringResource(R.string.lastname)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
        )
        OutlinedTextField(
            value = rent,
            onValueChange = { rent = it },
            label = { Text(text = stringResource(R.string.rent)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
        )
        Button(
            modifier = Modifier
                .padding(top = 16.dp),
            onClick = {
                if (firstName.isNotBlank() && lastName.isNotBlank()) {
                    editTenant(firstName, lastName, rent.toDouble())
                }
            }, shape = RoundedCornerShape(20.dp)
        )

        {
            Text(text = stringResource(R.string.confirm))
        }
        Button(
            modifier = Modifier
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
            onClick = {
                deleteTenant()
            }, shape = RoundedCornerShape(20.dp)
        )

        {
            Text(text = stringResource(R.string.delete_tenant))
        }
    }

}


@Preview(showBackground = true)
@Composable
fun LandlordEditTenantPreview() {

    val existingTenant = Tenant(
        firstName = "Jan",
        lastName = "Kowalski",
        email = "zjb82099@omeie.com",
        rent = 2000.0,
        propertyId = "70xF0AwhddGHRuSGYT7L",
        tenantId = "GqXmdTRxynWHhnnzJcSm5M6ei6b2"
    )

    val property = Property(
        "70xF0AwhddGHRuSGYT7L",
        "Jozefa Rostafinskiego",
        "16",
        "17",
        "50-247",
        "Wroclaw",
        "Pth5PB4PlYSxtb6vYX4MVZRmen52"
    )

    AppartamentyTheme {
        LandlordEditTenantMainScreen(
            "Pth5PB4PlYSxtb6vYX4MVZRmen52",
            existingTenant,
            "list_properties",
            property
        )
    }
}