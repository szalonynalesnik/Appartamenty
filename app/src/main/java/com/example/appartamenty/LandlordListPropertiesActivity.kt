package com.example.appartamenty

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
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
import com.example.appartamenty.ui.theme.AppartamentyTheme
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class LandlordListPropertiesActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val landlordId = intent.getStringExtra("landlordId")
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
                    if (landlordId != null && destination != null) {
                        SetPropertyData(landlordId, destination)
                    }
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun SetPropertyData(landlordId: String, destination: String) {

    val propertyList = mutableStateListOf<Property?>()
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    db.collection("properties").whereEqualTo("landlordId", landlordId).get()
        .addOnSuccessListener { queryDocumentSnapshots ->

            if (!queryDocumentSnapshots.isEmpty) {
                val list = queryDocumentSnapshots.documents
                for (d in list) {

                    val c: Property? = d.toObject(Property::class.java)
                    if (c != null) {
                        c.propertyId = d.id
                    }
                    propertyList.add(c)
                }
            }
        }
    ShowLazyList(propertyList, landlordId, destination)
}

@Composable
fun ShowLazyList(
    propertyList: SnapshotStateList<Property?>,
    landlordId: String,
    destination: String
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.properties),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.Start)
        )
        LazyColumn {
            itemsIndexed(propertyList) { index, item ->
                if (item != null) {
                    CardItem(item, landlordId, destination)
                }
            }
        }
        if (destination == "list_properties") {
            OutlinedButton(
                modifier = Modifier.padding(top = 16.dp),
                onClick = {
                    val intent = Intent(context, LandlordAddRealEstate::class.java)
                    intent.putExtra("landlordId", landlordId)
                    context.startActivity(intent)
                }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add new property")
                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                Text(text = stringResource(R.string.add_property))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardItem(property: Property, landlordId: String, destination: String) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onSecondaryContainer),
        onClick = {
            if (destination == "list_properties") {
                val intent = Intent(context, LandlordPropertyDetails::class.java)
                intent.putExtra("property", property).putExtra("landlordId", landlordId)
                context.startActivity(intent)
            } else if (destination == "meter_readings") {
                val intent = Intent(context, LandlordListMeterReadings::class.java)
                intent.putExtra("property", property).putExtra("landlordId", landlordId)
                context.startActivity(intent)
            } else if (destination == "calculate_rent") {
                val intent = Intent(context, CalculateRentActivity::class.java)
                intent.putExtra("property", property).putExtra("landlordId", landlordId)
                context.startActivity(intent)
            }
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(4f)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 10.dp),
                    text = property.street.toString() + " " + property.streetNo.toString() + "/" + property.apartmentNo.toString() + ", " + property.city.toString(),
                    textAlign = TextAlign.Center
                )
            }

            if (destination == "list_properties") {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    IconButton(
                        onClick = {
                            val intent = Intent(context, LandlordEditProperty::class.java)
                            intent.putExtra("property", property).putExtra("landlordId", landlordId)
                                .putExtra("destination", destination)
                            context.startActivity(intent)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Add new property"
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, locale = "pl")
@Composable
fun LandlordListPropertiesPreview() {
    AppartamentyTheme {
        SetPropertyData(landlordId = "Pth5PB4PlYSxtb6vYX4MVZRmen52", "list_properties")
    }
}