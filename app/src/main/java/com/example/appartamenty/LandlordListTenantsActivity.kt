package com.example.appartamenty

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.appartamenty.data.Utility
import com.example.appartamenty.ui.theme.AppartamentyTheme
import com.google.firebase.firestore.FirebaseFirestore

class LandlordListTenantsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val landlordId = intent.getStringExtra("landlordId")
        val destination = intent.getStringExtra("destination")
        val property = intent.extras?.get("property") as Property

        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContent {
            AppartamentyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SetTenantData(landlordId!!, property, destination!!)
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun SetTenantData(landlordId: String, property: Property, destination: String) {
    val tenantList = mutableStateListOf<Tenant?>()

    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    db.collection("tenants").whereEqualTo("propertyId", property.propertyId).get()
        .addOnSuccessListener { queryDocumentSnapshots ->

            if (!queryDocumentSnapshots.isEmpty) {

                val list = queryDocumentSnapshots.documents
                for (d in list) {

                    val c: Tenant? = d.toObject(Tenant::class.java)
                    if (c != null) {
                        c.tenantId = d.id
                    }

                    tenantList.add(c)
                }

            }

        }
    ShowLazyListOfTenants(
        tenants = tenantList,
        landlordId = landlordId,
        destination = destination,
        property = property
    )
}

@Composable
fun ShowLazyListOfTenants(
    tenants: SnapshotStateList<Tenant?>,
    landlordId: String,
    destination: String,
    property: Property
) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.tenants),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.Start)
        )
        LazyColumn {
            itemsIndexed(tenants) { index, item ->
                if (item != null) {
                    TenantCardItem(item, landlordId, destination, property)
                }
            }
        }

        Column(
            modifier = Modifier.padding(top = 10.dp)
        )
        {
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val intent = Intent(context, LandlordAddTenantToProperty::class.java)
                    intent.putExtra("property", property)
                    intent.putExtra("destination", destination)
                    intent.putExtra("landlordId", landlordId)
                    context.startActivity(intent)
                }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add tenant")
                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                Text(text = stringResource(R.string.add_tenant))
            }
        }

        OutlinedButton(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 16.dp),
            onClick = {
                val intent = Intent(context, LandlordEditProperty::class.java)
                intent.putExtra("landlordId", landlordId)
                intent.putExtra("destination", destination)
                intent.putExtra("property", property)
                context.startActivity(intent)
            }) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go back")
            Spacer(
                modifier = Modifier
                    .width(8.dp)
            )
            Text(text = stringResource(R.string.back_to_property_list), textAlign = TextAlign.Center)
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantCardItem(tenant: Tenant, landlordId: String, destination: String, property: Property) {
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
                    text = tenant.firstName + " " + tenant.lastName + "\n" +
                            stringResource(R.string.rent) + ": " + tenant.rent,
                    textAlign = TextAlign.Center
                )

            }
            IconButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    val intent = Intent(context, LandlordEditTenantActivity::class.java)
                    intent.putExtra("tenant", tenant).putExtra("landlordId", landlordId)
                        .putExtra("destination", destination).putExtra("property", property)
                    context.startActivity(intent)
                }
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit tenant")
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun LandlordListTenantsPreview() {
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
        SetTenantData("Pth5PB4PlYSxtb6vYX4MVZRmen52", property, "list_properties")
    }
}