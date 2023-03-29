package com.example.appartamenty

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appartamenty.data.Property
import com.example.appartamenty.ui.theme.AppartamentyTheme
import kotlinx.coroutines.*

class LandlordPropertyDetails : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val property = intent.extras?.get("property") as Property
            AppartamentyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShowProperty(property)
                }
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ShowProperty(property: Property) {

    Log.d(LandlordPropertyDetails::class.java.simpleName, "Object: ${property.street}")

    val context = LocalContext.current
    Column(
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = "Street: ${property.street}",
            modifier = Modifier.padding(vertical = 10.dp)
        )
        Text(
            text = "Street no: ${property.streetNo}",
            modifier = Modifier.padding(vertical = 10.dp)
        )
        Text(
            text = "Apartment no: ${property.apartmentNo}",
            modifier = Modifier.padding(vertical = 10.dp)
        )
        Text(
            text = "Postcode: ${property.postalCode}",
            modifier = Modifier.padding(vertical = 10.dp)
        )
        Text(
            text = "City: ${property.city}",
            modifier = Modifier.padding(vertical = 10.dp)
        )
        Column(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 16.dp),
        ) {
            Text(
                text = "Tenants",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(bottom = 8.dp)
            )
            OutlinedButton(
                modifier = Modifier.padding(top = 16.dp),
                onClick = {
                    val intent = Intent(context, LandlordAddTenantToProperty::class.java)
                    intent.putExtra("propertyId", property.propertyId)
                    context.startActivity(intent)
                }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add tenant")
                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                Text(text = stringResource(R.string.add_tenant))
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun ShowPropertyDetailsPreview() {
//    AppartamentyTheme {
//        ShowProperty(property)
//    }
//}