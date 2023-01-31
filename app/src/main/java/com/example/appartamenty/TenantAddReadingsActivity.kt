package com.example.appartamenty

import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appartamenty.ui.theme.AppartamentyTheme
import java.util.*

class TenantAddReadingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppartamentyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TenantAddReadingsScreen(applicationContext)
                }
            }
        }
    }
}

@Composable
fun TenantAddReadingsScreen(context: Context) {
    Column(){
        MeterReadings(context)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeterReadings(context: Context){
    Column(
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Meter readings",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.Start)
        )

        DatePicker(context = context)

        UtilityField(label = "Electricity", placeholder = "Enter meter value")
        UtilityField(label = "Gas", placeholder = "Enter meter value")
        UtilityField(label = "Water", placeholder = "Enter meter value")
        UtilityField(label = "Internet", placeholder = "Enter meter value")
        }

    }

@Composable
fun DatePicker(context: Context){

    val year: Int
    val month: Int
    val day: Int

    var datePicked by remember { mutableStateOf("None") }

    val calendar = Calendar.getInstance()

    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)

    calendar.time = Date()

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, day: Int ->
            datePicked = String.format("%02d", day) + "/" + String.format("%02d", month+1) + "/$year"
        }, year, month, day
    )



    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Readings for date: ${datePicked}")
        Spacer(modifier = Modifier.size(16.dp))
        Button(onClick = {
            datePickerDialog.show()
        }) {
            Text(text = "Choose date")
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UtilityField(label: String, placeholder: String){
    var text by remember { mutableStateOf(TextFieldValue("")) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
        Spacer(
            modifier = Modifier
                .weight(0.1f)
        )
        TextField(
            value = text,
            // label = { Text(text = "Enter value from the meter") },
            placeholder = { Text(text = placeholder) },
            onValueChange = { text = it },
            modifier = Modifier
                .padding(start = 16.dp)
                .width(250.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TenantAddReadingsPreview() {
    val context = LocalContext.current

    AppartamentyTheme {
        MeterReadings(context)
    }
}