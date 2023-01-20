package com.example.appartamenty.ui.theme

import android.content.Context
import android.os.Bundle
import android.widget.ToggleButton
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
        ConfirmButton()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UtilityChooser() {

    var text by remember { mutableStateOf(TextFieldValue("")) }

    val electricityCheckedState = remember { mutableStateOf(false) }
    val gasCheckedState = remember { mutableStateOf(false) }
    val waterCheckedState = remember { mutableStateOf(false) }
    val internetCheckedState = remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 16.dp),
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
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .width(intrinsicSize = IntrinsicSize.Max),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Switch(
                    checked = electricityCheckedState.value,
                    onCheckedChange = { electricityCheckedState.value = it },
                    modifier = Modifier

                )
                Text(
                    text = "Electricity",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                )
            }
            if (electricityCheckedState.value == true) {
                ValueField(label = "Price [PLN/kWh]", placeholder = "Enter value")
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Switch(
                    checked = gasCheckedState.value,
                    onCheckedChange = { gasCheckedState.value = it },
                    modifier = Modifier
                )
                Text(
                    text = "Gas",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier,
                )
            }

            if (gasCheckedState.value == true) {
                ValueField(label = "Price [PLN/m3]", placeholder = "Enter value")
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Switch(
                    checked = waterCheckedState.value,
                    onCheckedChange = { waterCheckedState.value = it }
                )
                Text(
                    text = "Water",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .padding(start = 8.dp),
                )
            }

            if (waterCheckedState.value == true) {
                ValueField(label = "Price [PLN/m3]", placeholder = "Enter value")
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Switch(
                    checked = internetCheckedState.value,
                    onCheckedChange = { internetCheckedState.value = it }
                )
                Text(
                    text = "Internet",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .padding(start = 8.dp),
                )
            }
            if (internetCheckedState.value == true) {
                ValueField(label = "Price [monthly]", placeholder = "Enter value")
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValueField(label: String, placeholder: String) {

    var text by remember { mutableStateOf(TextFieldValue("")) }

    TextField(
        value = text,
        onValueChange = { text = it },
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        modifier = Modifier
            .padding(end = 0.dp),
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressForm() {

    var text by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = "Address",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.Start)
        )

        OutlinedTextField(
            value = text,
            label = {Text(text = "Street")},
            onValueChange = { text = it },
            modifier = Modifier
                .fillMaxWidth(),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            OutlinedTextField(
                value = text,
                label = {Text(text = "House no.")},
                onValueChange = { text = it },
                modifier = Modifier
                    .weight(1f)
            )
            Spacer(
                modifier = Modifier
                .weight(0.1f)
            )
            OutlinedTextField(
                value = text,
                label = {Text(text = "Apartment no.")},
                onValueChange = { text = it },
                modifier = Modifier
                    .weight(1f)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            OutlinedTextField(
                value = text,
                label = {Text(text = "Postal code")},
                onValueChange = { text = it },
                modifier = Modifier
                    .weight(1f)
            )
            Spacer(
                modifier = Modifier
                    .weight(0.1f)
            )
            OutlinedTextField(
                value = text,
                label = {Text(text = "City")},
                onValueChange = { text = it },
                modifier = Modifier
                    .weight(1f)
            )
        }

    }
}

@Composable
fun ConfirmButton(){
    Button(onClick = {}, shape = RoundedCornerShape(20.dp)) {
        Text(text = "Confirm and save")
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