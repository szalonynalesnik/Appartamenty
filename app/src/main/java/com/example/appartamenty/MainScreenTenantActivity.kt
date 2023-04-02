package com.example.appartamenty

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appartamenty.ui.theme.ui.theme.AppartamentyTheme
import com.example.appartamenty.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainScreenTenantActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tenantId = Firebase.auth.currentUser?.uid
        setContent {
            AppartamentyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (tenantId != null) {
                        TenantMainScreen(applicationContext, tenantId)
                    }
                }
            }
        }
    }
}

@Composable
fun TenantMainScreen(context: Context, tenantId: String) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppLogo()
        TenantChooser(context, tenantId)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantChooser(context: Context, tenantId: String) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .height(intrinsicSize = IntrinsicSize.Max)
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 16.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxSize(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                onClick = {
                    var intent = Intent(context, TenantAddReadingsActivity::class.java)
                    intent.putExtra("tenantId", tenantId)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Calculate,
                        contentDescription = stringResource(R.string.utility_meter_readings),
                        modifier = Modifier
                            .size(48.dp)
                    )
                    Text(
                        modifier = Modifier
                            .padding(vertical = 10.dp, horizontal = 10.dp),
                        text = stringResource(R.string.utility_meter_readings),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Card(
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxSize(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.CalendarMonth,
                        contentDescription = "calendar",
                        modifier = Modifier
                            .size(48.dp),
                    )
                    Text(
                        modifier = Modifier
                            .padding(vertical = 10.dp, horizontal = 10.dp)
                            .fillMaxWidth(),
                        text = stringResource(R.string.calendar),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .height(intrinsicSize = IntrinsicSize.Max)
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 16.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxSize(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                ),

                ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Build,
                        contentDescription = "report a fault",
                        modifier = Modifier
                            .size(48.dp)
                    )
                    Text(
                        modifier = Modifier
                            .padding(vertical = 10.dp, horizontal = 10.dp),
                        text = stringResource(R.string.report_fault),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Card(
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxSize(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Chat,
                        contentDescription = "contact landlord",
                        modifier = Modifier
                            .size(48.dp),
                    )
                    Text(
                        modifier = Modifier
                            .padding(vertical = 10.dp, horizontal = 10.dp)
                            .fillMaxWidth(),
                        text = stringResource(R.string.contact_landlord),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

}

@Preview(showBackground = true, showSystemUi = true, locale = "pl")
@Composable
fun MainScreenTenantPreview() {
    val context = LocalContext.current
    com.example.appartamenty.ui.theme.AppartamentyTheme {
        TenantMainScreen(context, "GqXmdTRxynWHhnnzJcSm5M6ei6b2")
    }
}