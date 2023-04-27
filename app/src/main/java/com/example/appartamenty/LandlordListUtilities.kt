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

class LandlordListUtilities : ComponentActivity() {
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
                    SetUtilityData(landlordId!!, property, destination!!)
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun SetUtilityData(landlordId: String, property: Property, destination: String) {
    val utilityList = mutableStateListOf<Utility?>()
    // on below line creating variable for freebase database
    // and database reference.
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // on below line getting data from our database
    db.collection("utilities").whereEqualTo("propertyId", property.propertyId).orderBy("name").get()
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
                    val c: Utility? = d.toObject(Utility::class.java)
                    if (c != null) {
                        c.utilityId = d.id
                    }
                    // and we will pass this object class inside
                    // our arraylist which we have created for list view.
                    utilityList.add(c)
                }
                // on below line we are calling method to display UI


            }

        }
    ShowLazyListOfUtilities(
        utilities = utilityList,
        landlordId = landlordId,
        destination = destination,
        property
    )
}

@Composable
fun ShowLazyListOfUtilities(
    utilities: SnapshotStateList<Utility?>,
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
            text = stringResource(R.string.utilities),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.Start)
        )
        LazyColumn {
            itemsIndexed(utilities) { index, item ->
                if (item != null) {
                    UtilityCardItem(item, landlordId, destination, property)
                }
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
            Text(
                text = stringResource(R.string.back_to_property_list),
                textAlign = TextAlign.Center
            )
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UtilityCardItem(utility: Utility, landlordId: String, destination: String, property: Property) {
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
                when (utility.name) {
                    "Gas - constant" -> {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                            text = stringResource(R.string.gas_constant) + "\n" + String.format(
                                "%.3f",
                                utility.price
                            ) + " PLN",
                            textAlign = TextAlign.Center
                        )
                    }

                    "Electricity - constant" -> {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                            text = stringResource(R.string.electricity_constant) + "\n" + String.format(
                                "%.3f",
                                utility.price
                            ) + " PLN ",
                            textAlign = TextAlign.Center
                        )
                    }

                    "Internet" -> {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                            text = utility.name + "\n" + String.format(
                                "%.3f",
                                utility.price
                            ) + " PLN ",
                            textAlign = TextAlign.Center
                        )
                    }

                    "Gas" -> {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                            text = stringResource(R.string.gas) + "\n" + String.format(
                                "%.3f",
                                utility.price
                            ),
                            textAlign = TextAlign.Center
                        )
                    }

                    "Water" -> {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                            text = stringResource(R.string.water) + "\n" + String.format(
                                "%.3f",
                                utility.price
                            ),
                            textAlign = TextAlign.Center
                        )
                    }

                    "Electricity" -> {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                            text = stringResource(R.string.electricity) + "\n" + String.format(
                                "%.3f",
                                utility.price
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                IconButton(
                    onClick = {
                        val intent = Intent(context, LandlordEditUtility::class.java)
                        intent.putExtra("utility", utility).putExtra("landlordId", landlordId)
                            .putExtra("destination", destination).putExtra("property", property)
                        context.startActivity(intent)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit utility"
                    )

                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LandlordListUtilitiesPreview() {
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
        SetUtilityData("Pth5PB4PlYSxtb6vYX4MVZRmen52", property, "list_properties")
    }
}