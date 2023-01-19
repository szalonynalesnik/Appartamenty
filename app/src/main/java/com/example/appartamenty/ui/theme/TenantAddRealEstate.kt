package com.example.appartamenty.ui.theme

import android.content.Context
import android.os.Bundle
import android.widget.ToggleButton
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appartamenty.R
import com.example.appartamenty.ui.theme.ui.theme.AppartamentyTheme

class TenantAddRealEstate : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppartamentyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TenantAddRealEstateMainScreen(applicationContext)
                }
            }
        }
    }
}

@Composable
fun TenantAddRealEstateMainScreen(context: Context) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AddressForm()
        UtilityChooser()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UtilityChooser(){

    var text by remember { mutableStateOf(TextFieldValue("")) }
    val mCheckedState = remember{ mutableStateOf(false)}
    Column(
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Utilities",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.Start)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Switch(
                checked = mCheckedState.value,
                onCheckedChange = { mCheckedState.value = it },
                modifier = Modifier

            )
            Text(
                text = "Electricity",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(start = 8.dp)
            )
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(text = "Price [PLN/kWh]") },
                placeholder = { Text(text = "Enter value") },
                modifier = Modifier
                    .padding(start = 24.dp)
                    .width(200.dp)

            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Switch(
                checked = mCheckedState.value,
                onCheckedChange = { mCheckedState.value = it },
                modifier = Modifier
            )
            Text(
                text = "Gas",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(start = 8.dp),
            )
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(text = "Price [PLN/m3]") },
                placeholder = { Text(text = "Enter value") },
                modifier = Modifier
                    .padding(start = 24.dp)
                    .width(200.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Switch(
                checked = mCheckedState.value,
                onCheckedChange = { mCheckedState.value = it }
            )
            Text(
                text = "Water",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(start = 8.dp),
            )
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(text = "Price [PLN/m3]") },
                placeholder = { Text(text = "Enter value") },
                modifier = Modifier
                    .padding(start = 24.dp)
                    .width(200.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Switch(
                checked = mCheckedState.value,
                onCheckedChange = { mCheckedState.value = it }
            )
            Text(
                text = "Internet",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(start = 8.dp),
            )
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(text = "Price [monthly]") },
                placeholder = { Text(text = "Enter value") },
                modifier = Modifier
                    .width(200.dp)
                    .padding(start = 0.dp)
            )
        }
    }
}

@Composable
fun AddressForm() {
    Column(
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Utilities",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.Start)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TenantAddRealEstatePreview() {
    val context = LocalContext.current
    com.example.appartamenty.ui.theme.AppartamentyTheme {
        TenantAddRealEstateMainScreen(context)
    }
}