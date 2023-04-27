package com.example.appartamenty

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appartamenty.data.Utility
import com.example.appartamenty.ui.theme.AppartamentyTheme
import com.google.firebase.firestore.FirebaseFirestore

class LandlordEditUtility : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val landlordId = intent.getStringExtra("landlordId")
        val propertyId = intent.getStringExtra("propertyId")
        val destination = intent.getStringExtra("destination")
        val utility = intent.extras?.get("utility") as Utility
        super.onCreate(savedInstanceState)
        setContent {
            AppartamentyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LandlordEditUtilityMainScreen(
                        landlordId = landlordId!!,
                        existingUtility = utility,
                        destination = destination!!,
                        propertyId = propertyId!!
                    )
                }
            }
        }
    }
}

@Composable
fun LandlordEditUtilityMainScreen(
    landlordId: String,
    existingUtility: Utility,
    destination: String,
    propertyId: String
) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current
        UtilityEditForm(
            existingUtility = existingUtility,
            landlordId = landlordId,
            propertyId = propertyId,
            destination = destination
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UtilityEditForm(
    existingUtility: Utility,
    landlordId: String,
    propertyId: String,
    destination: String
) {

    val database = FirebaseFirestore.getInstance()

    val context = LocalContext.current

    var price by remember { mutableStateOf(existingUtility.price!!.toString()) }

    fun editUtility(
        price: Double,
    ) {
        database.collection("utilities").document(existingUtility.utilityId!!)
            .update("price", price)
            .addOnSuccessListener { doc ->
                Log.d(
                    MainActivity::class.java.simpleName,
                    "Updating utility successful"
                )
                val intent = Intent(context, LandlordListUtilities::class.java)
                intent.putExtra("landlordId", landlordId)
                    .putExtra("destination", destination)
                    .putExtra("propertyId", propertyId)
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
            text = stringResource(R.string.edit_utility),
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
    ) {
        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text(text = stringResource(R.string.value)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
        )
        Button(
            modifier = Modifier
                .padding(top = 16.dp),
            onClick = {
                if (price.toDouble() > 0) {
                    editUtility(price.toDouble())
                }
            }, shape = RoundedCornerShape(20.dp))

        {
        Text(text = stringResource(R.string.confirm))
        }
    }

}


@Preview(showBackground = true)
@Composable
fun LandlordEditUtilityPreview() {

    val existingUtility = Utility(
        constant = false,
        name = "Gas",
        price = 2.7344000339508057,
        propertyId = "70xF0AwhddGHRuSGYT7L",
        utilityId = "KGk8HljHzLw4VVztMeh6"
    )
    AppartamentyTheme {
        LandlordEditUtilityMainScreen(
            "Pth5PB4PlYSxtb6vYX4MVZRmen52",
            existingUtility,
            "list_properties",
            "70xF0AwhddGHRuSGYT7L"
        )
    }
}