package com.example.appartamenty

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appartamenty.ui.theme.AppartamentyTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppartamentyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginForm(applicationContext)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginForm(context: Context) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val image = painterResource(R.drawable.applogo)

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier.padding(vertical = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                    fontWeight = FontWeight.Light
                )
            }
            Text(
                text = "Welcome again!",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = {
                    Text(
                        text = "E-mail address",
                    )
                },
                placeholder = {
                    Text(
                        text = "Enter your e-mail address...",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )

                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = "email"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(),
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = {
                    Text(
                        text = "Password"
                    )
                },
                placeholder = {
                    Text(
                        text = "Enter your password...",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                    )

                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = "lock"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)

            )
        }
        Button(
            onClick = {
                logged(email, password, context)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 100.dp, end = 100.dp, top = 16.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                text = "Login",
                textAlign = TextAlign.Center
            )

        }
    }
}

fun logged(email: String, password: String, context: Context) {
    if (email == "ania" && password == "1234") {
        Toast.makeText(context, "Logged in successfully", Toast.LENGTH_SHORT).show()
    } else {
        Toast.makeText(context, "Couldn't log in", Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    val context = LocalContext.current
    AppartamentyTheme {
        LoginForm(context)
    }
}