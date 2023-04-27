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
import com.example.appartamenty.data.Property
import com.example.appartamenty.data.Utility
import com.example.appartamenty.ui.theme.AppartamentyTheme
import com.google.firebase.firestore.FirebaseFirestore

class LandlordEditUtility : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val landlordId = intent.getStringExtra("landlordId")
        val property = intent.extras?.get("property") as Property
        val destination = intent.getStringExtra("destination")
        val utility = intent.extras?.get("utility") as Utility

        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

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
                        property = property
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
    property: Property
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
            property = property,
            destination = destination
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UtilityEditForm(
    existingUtility: Utility,
    landlordId: String,
    property: Property,
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
        when (existingUtility.name) {
            "Gas" -> Text(
                text = stringResource(R.string.gas),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Start)
            )

            "Gas - constant" -> Text(
                text = stringResource(R.string.gas_constant),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Start)
            )

            "Internet" -> Text(
                text = stringResource(R.string.internet),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Start)
            )
            "Electricity" -> Text(
                text = stringResource(R.string.electricity),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Start)
            )
            "Electricity - constant" -> Text(
                text = stringResource(R.string.electricity_constant),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Start)
            )
            "Water" -> Text(
                text = stringResource(R.string.water),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Start)
            )
        }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
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
            }, shape = RoundedCornerShape(20.dp)
        )

        {
            Text(text = stringResource(R.string.confirm))
        }
    }

}


@Preview(showBackground = true)
@Composable
fun LandlordEditUtilityPreview() {
    val property = Property(
        "70xF0AwhddGHRuSGYT7L",
        "Jozefa Rostafinskiego",
        "16",
        "17",
        "50-247",
        "Wroclaw",
        "Pth5PB4PlYSxtb6vYX4MVZRmen52"
    )

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
            property
        )
    }
}