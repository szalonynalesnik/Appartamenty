package com.example.appartamenty

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appartamenty.data.Property
import com.example.appartamenty.ui.theme.AppartamentyTheme
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

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

//suspend fun getDataFromFirestore(propertyId: String): Property{
//
//    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
//    var property = Property()
//
//    try{
//        db.collection("properties").whereEqualTo(FieldPath.documentId(), propertyId).get().await().map{
//        val result = it.toObject(Property::class.java)
//            property = result
//        }
//    }
//    catch(e: FirebaseFirestoreException){
//
//    }
//    return property
//}


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ShowProperty(property: Property) {

    Log.d(LandlordPropertyDetails::class.java.simpleName, "Object: ${property.street}")

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
            text = "Apartment no: ${property.houseNo}",
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
    }
}


//@Preview(showBackground = true)
//@Composable
//fun ShowPropertyDetailsPreview() {
//    AppartamentyTheme {
//        ShowProperty(property)
//    }
//}