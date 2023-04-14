package com.example.appartamenty

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Bundle
import android.view.Window
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
import com.example.appartamenty.ui.theme.AppartamentyTheme
import com.example.appartamenty.R
import com.example.appartamenty.data.Landlord
import com.example.appartamenty.data.Property
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainScreenLandlordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val landlordId = intent.getStringExtra("landlordId")

        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContent {
            AppartamentyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (landlordId != null) {
                        LandlordMainScreen(LocalContext.current, landlordId)
                    }
                }
            }
        }
    }
}

@Composable
fun LandlordMainScreen(context: Context, landlordId: String) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppLogo()
        LandlordChooser(context, landlordId)
    }
}

@Composable
fun AppLogo() {
    val image = painterResource(R.drawable.applogo)

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = image,
            contentDescription = "logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(50.dp)
                .padding(horizontal = 8.dp),
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
        )
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Light,
            modifier = Modifier,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandlordChooser(context: Context, landlordId: String) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 8.dp)
        ) {
            Card(
                modifier = Modifier,
                colors = CardDefaults.cardColors(
                    containerColor =  MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onPrimaryContainer),
                onClick = {
                    var intent = Intent(context, LandlordListPropertiesActivity::class.java)
                    intent.putExtra("landlordId", landlordId)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        Icons.Default.Home,
                        contentDescription = "properties",
                        modifier = Modifier
                            .size(48.dp)
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp, horizontal = 10.dp),
                        text = stringResource(R.string.manage_properties),
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
                    containerColor =  MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onPrimaryContainer)
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
                        contentDescription = "utility meter readings",
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
                    containerColor =  MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onPrimaryContainer)
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
                        text = stringResource(R.string.calculate_rent),
                        textAlign = TextAlign.Center
                    )
                }
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
//                    containerColor =  MaterialTheme.colorScheme.secondaryContainer,
//                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
//                ),
//                border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onSecondaryContainer)
//            ) {
//                Column(
//                    modifier = Modifier
//                        .padding(vertical = 16.dp)
//                        .fillMaxSize(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    Icon(
//                        Icons.Default.Build,
//                        contentDescription = "fault reports",
//                        modifier = Modifier
//                            .size(48.dp)
//                    )
//                    Text(
//                        modifier = Modifier
//                            .padding(vertical = 10.dp, horizontal = 10.dp),
//                        text = stringResource(R.string.faultr_reports),
//                        textAlign = TextAlign.Center
//                    )
//                }
//            }
//            Card(
//                modifier = Modifier
//                    .weight(0.5f)
//                    .fillMaxSize(),
//                colors = CardDefaults.cardColors(
//                    containerColor =  MaterialTheme.colorScheme.secondaryContainer,
//                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
//                ),
//                border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onSecondaryContainer)
//
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
//                        contentDescription = "contact tenants",
//                        modifier = Modifier
//                            .size(48.dp),
//                    )
//                    Text(
//                        modifier = Modifier
//                            .padding(vertical = 10.dp, horizontal = 10.dp)
//                            .fillMaxWidth(),
//                        text = stringResource(R.string.contact_tenants),
//                        textAlign = TextAlign.Center
//                    )
//                }
//            }
//        }
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
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenLandlordPreview() {
    com.example.appartamenty.ui.theme.AppartamentyTheme {
        LandlordMainScreen(LocalContext.current, "XJWXUFoiAEV0efxdGpPrdDNVS3M2")
    }
}