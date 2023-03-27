package com.example.appartamenty

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appartamenty.data.Property
import com.example.appartamenty.ui.theme.AppartamentyTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class LandlordListProperties : ComponentActivity() {
    private val auth by lazy {
        Firebase.auth
    }
    private val database by lazy {
        FirebaseFirestore.getInstance()
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
                    SetData()
                }
            }
        }
    }
}

//suspend fun retrievePropertiesSnapshot(): List<Property> {
//
//    var properties: List<Property> = mutableListOf()
//    val landlordId = "XJWXUFoiAEV0efxdGpPrdDNVS3M2" //auth.currentUser?.uid.toString()
//
//    database.collection("properties").whereEqualTo("landlordId", landlordId).get().await().map {
//        val result = it.toObject(Property::class.java)
//        properties += result
//    }
//
//    return properties
//}

@SuppressLint("UnrememberedMutableState")
@Composable
fun SetData() {
    val landlordId = "XJWXUFoiAEV0efxdGpPrdDNVS3M2" //auth.currentUser?.uid.toString()

    var propertyList = mutableStateListOf<Property?>()
    // on below line creating variable for freebase database
    // and database reference.
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // on below line getting data from our database
    db.collection("properties").whereEqualTo("landlordId", landlordId).get()
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
                    val c: Property? = d.toObject(Property::class.java)
                    // and we will pass this object class inside
                    // our arraylist which we have created for list view.
                    propertyList.add(c)

                }
            }
        }

    // on below line we are calling method to display UI
    ShowLazyList(propertyList)


}

@Composable
fun ShowLazyList(propertyList: SnapshotStateList<Property?>) {
    Column(
        modifier = Modifier
            .fillMaxSize().padding(vertical = 16.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
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
                CardItem(item)
            }
        }
    }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardItem(property: Property) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onSecondaryContainer),
        onClick = {

        }
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
                text = property.street.toString() + " " + property.streetNo.toString() + "/" + property.houseNo.toString() + ", " + property.city.toString(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, locale = "pl")
@Composable
fun DefaultPreview() {
    AppartamentyTheme {
        SetData()
    }
}