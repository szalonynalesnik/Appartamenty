package com.example.appartamenty

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.example.appartamenty.ui.theme.AppartamentyTheme
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*

class LandlordPropertyDetails : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val property = intent.extras?.get("property") as Property
        val landlordId = intent.extras?.getString("landlordId")
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
                        SetTenantData(property, landlordId)
                    }
                }
            }
        }
    }
}

//@SuppressLint("CoroutineCreationDuringComposition")
//@Composable
//fun ShowProperty(property: Property, landlordId: String) {
//
//    Log.d(LandlordPropertyDetails::class.java.simpleName, "Object: ${property.street}")
//
//    val context = LocalContext.current
//    Column(
//        modifier = Modifier
//            .padding(vertical = 16.dp, horizontal = 16.dp)
//            .fillMaxWidth(),
//        horizontalAlignment = Alignment.Start,
//    ) {
//        Text(
//            text = stringResource(R.string.street) + ": ${property.street}",
//            modifier = Modifier.padding(vertical = 10.dp)
//        )
//        Text(
//            text = stringResource(R.string.streetNo) + ": ${property.streetNo}",
//            modifier = Modifier.padding(vertical = 10.dp)
//        )
//        Text(
//            text = stringResource(R.string.aptno) + ": ${property.apartmentNo}",
//            modifier = Modifier.padding(vertical = 10.dp)
//        )
//        Text(
//            text = stringResource(R.string.postcode) + ": ${property.postalCode}",
//            modifier = Modifier.padding(vertical = 10.dp)
//        )
//        Text(
//            text = stringResource(R.string.city) + ": ${property.city}",
//            modifier = Modifier.padding(vertical = 10.dp)
//        )
//        Column(
//            modifier = Modifier
//                .padding(vertical = 16.dp),
//        ) {
//            Text(
//                text = stringResource(R.string.tenants),
//                style = MaterialTheme.typography.titleLarge,
//                color = MaterialTheme.colorScheme.onBackground,
//                fontWeight = FontWeight.Normal,
//                modifier = Modifier
//                    .padding(bottom = 8.dp)
//            )
//            OutlinedButton(
//                modifier = Modifier.padding(top = 16.dp),
//                onClick = {
//                    val intent = Intent(context, LandlordAddTenantToProperty::class.java)
//                    intent.putExtra("propertyId", property.propertyId)
//                    intent.putExtra("landlordId", landlordId)
//                    context.startActivity(intent)
//                }) {
//                Icon(imageVector = Icons.Default.Add, contentDescription = "Add tenant")
//                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
//                Text(text = stringResource(R.string.add_tenant))
//            }
//        }
//    }
//}

@SuppressLint("UnrememberedMutableState")
@Composable
fun SetTenantData(property: Property, landlordId: String) {

    val tenantList = mutableStateListOf<Tenant?>()
    // on below line creating variable for freebase database
    // and database reference.
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // on below line getting data from our database
    db.collection("tenants").whereEqualTo("propertyId", property.propertyId).get()
        .addOnSuccessListener { queryDocumentSnapshots ->
            // after getting the data we are calling
            // on success method
            // and inside this method we are checking
            // if the received query snapshot is empty or not.
            if (!queryDocumentSnapshots.isEmpty) {
                // if the snapshot is not empty we are
                // hiding our progress bar and adding
                // our data in a list.
                val list = queryDocumentSnapshots.documents
                for (d in list) {
                    // after getting this list we are passing that
                    // list to our object class.
                    val c: Tenant? = d.toObject(Tenant::class.java)
                    // and we will pass this object class inside
                    // our arraylist which we have created for list view.
                    tenantList.add(c)

                }
            }
        }.addOnFailureListener {
            Log.d("FAIL", "Could not find tenant for this property")
        }

    // on below line we are calling method to display UI
    ShowLazyListOfTenants(tenantList, property, landlordId)

}

@Composable
fun ShowLazyListOfTenants(
    tenantList: SnapshotStateList<Tenant?>,
    property: Property,
    landlordId: String
) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 10.dp),

        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = stringResource(R.string.your_property),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Text(
            text = stringResource(R.string.street) + ": ${property.street}",
            modifier = Modifier.padding(vertical = 5.dp)
        )
        Text(
            text = stringResource(R.string.streetNo) + ": ${property.streetNo}",
            modifier = Modifier.padding(vertical = 5.dp)
        )
        Text(
            text = stringResource(R.string.aptno) + ": ${property.apartmentNo}",
            modifier = Modifier.padding(vertical = 5.dp)
        )
        Text(
            text = stringResource(R.string.postcode) + ": ${property.postalCode}",
            modifier = Modifier.padding(vertical = 5.dp)
        )
        Text(
            text = stringResource(R.string.city) + ": ${property.city}",
            modifier = Modifier.padding(vertical = 5.dp)
        )
        Text(
            text = stringResource(R.string.tenants),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(top = 20.dp, bottom = 10.dp)
        )

        LazyColumn {
            itemsIndexed(tenantList) { index, item ->
                if (item != null) {
                    TenantCardItem(item)
                }
            }
        }

        OutlinedButton(
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
                val intent = Intent(context, LandlordAddTenantToProperty::class.java)
                intent.putExtra("propertyId", property.propertyId)
                intent.putExtra("landlordId", landlordId)
                context.startActivity(intent)
            }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add tenant")
            Spacer(modifier = Modifier.padding(horizontal = 5.dp))
            Text(text = stringResource(R.string.add_tenant))
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantCardItem(tenant: Tenant) {
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
                text = tenant.firstName + " " + tenant.lastName + "\n" + stringResource(R.string.rent) + ": ${tenant.rent}",
                textAlign = TextAlign.Center
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ShowPropertyDetailsPreview() {
    AppartamentyTheme {
        SetTenantData(
            property = Property(
                "70xF0AwhddGHRuSGYT7L",
                "Jozefa Rostafinskiego",
                "16",
                "17",
                "50-247",
                "Wroclaw",
                "Pth5PB4PlYSxtb6vYX4MVZRmen52"
            ), landlordId = "Pth5PB4PlYSxtb6vYX4MVZRmen52"
        )
    }
}