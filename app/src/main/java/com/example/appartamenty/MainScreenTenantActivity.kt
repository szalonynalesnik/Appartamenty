package com.example.appartamenty

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appartamenty.ui.theme.AppartamentyTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainScreenTenantActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

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
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                onClick = {
                    val intent = Intent(context, TenantAddReadingsActivity::class.java)
                    intent.putExtra("tenantId", tenantId)
                    Log.d(MainScreenTenantActivity::class.java.simpleName, "Tenant ID passed on: $tenantId")
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
                        Icons.Default.GasMeter,
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
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                onClick = {
                    val intent = Intent(context, CalculateRentActivity::class.java)
                    intent.putExtra("tenantId", tenantId)
                    Log.d(MainScreenTenantActivity::class.java.simpleName, "Tenant ID passed on: $tenantId")
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

                        Icons.Default.Payments,
                        contentDescription = "rent",
                        modifier = Modifier
                            .size(48.dp),
                    )
                    Text(
                        modifier = Modifier
                            .padding(vertical = 10.dp, horizontal = 10.dp)
                            .fillMaxWidth(),
                        text = stringResource(R.string.rent),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        )
        {
            OutlinedButton(
                onClick = { Firebase.auth.signOut()
                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)},
                modifier = Modifier.padding(vertical = 8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
            ) {
                Icon(imageVector = Icons.Default.Logout, contentDescription = "Log out")
                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                Text(text = stringResource(R.string.log_out))
            }
        }


//        Row(
//            modifier = Modifier
//                .height(intrinsicSize = IntrinsicSize.Max)
//                .padding(vertical = 8.dp),
//            horizontalArrangement = Arrangement.spacedBy(space = 16.dp)
//        ) {
//            Card(
//                modifier = Modifier
//                    .weight(0.5f)
//                    .fillMaxSize(),
//                colors = CardDefaults.cardColors(
//                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
//                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
//                ),
//                border = BorderStroke(
//                    width = 1.dp,
//                    color = MaterialTheme.colorScheme.onSecondaryContainer
//                ),
//
//                ) {
//                Column(
//                    modifier = Modifier
//                        .padding(vertical = 16.dp)
//                        .fillMaxSize(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    Icon(
//                        Icons.Default.Build,
//                        contentDescription = "report a fault",
//                        modifier = Modifier
//                            .size(48.dp)
//                    )
//                    Text(
//                        modifier = Modifier
//                            .padding(vertical = 10.dp, horizontal = 10.dp),
//                        text = stringResource(R.string.report_fault),
//                        textAlign = TextAlign.Center
//                    )
//                }
//            }
//            Card(
//                modifier = Modifier
//                    .weight(0.5f)
//                    .fillMaxSize(),
//                colors = CardDefaults.cardColors(
//                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
//                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
//                ),
//                border = BorderStroke(
//                    width = 1.dp,
//                    color = MaterialTheme.colorScheme.onSecondaryContainer
//                )
//            ) {
//                Column(
//                    modifier = Modifier
//                        .padding(vertical = 16.dp)
//                        .fillMaxSize(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    Icon(
//                        Icons.Default.Chat,
//                        contentDescription = "contact landlord",
//                        modifier = Modifier
//                            .size(48.dp),
//                    )
//                    Text(
//                        modifier = Modifier
//                            .padding(vertical = 10.dp, horizontal = 10.dp)
//                            .fillMaxWidth(),
//                        text = stringResource(R.string.contact_landlord),
//                        textAlign = TextAlign.Center
//                    )
//                }
//            }
//        }
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