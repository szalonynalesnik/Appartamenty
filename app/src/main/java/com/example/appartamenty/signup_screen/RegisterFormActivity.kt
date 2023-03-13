package com.example.appartamenty.signup_screen

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appartamenty.R
import com.example.appartamenty.ui.theme.AppartamentyTheme


class RegisterFormActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppartamentyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RegisterScreen(applicationContext)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(context: Context) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RegisterWelcome(context = context)
        RegisterForm(context = context)
    }
}

@Composable
fun RegisterWelcome(context: Context) {
    val image = painterResource(R.drawable.applogo)

    Column(
        modifier = Modifier
            .padding(bottom = 24.dp),
    ) {
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
        Text(
            text = "Welcome!",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterForm(context: Context) {
    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = {
                Text(
                    text = stringResource(R.string.emailaddress),
                )
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.enteremail),
                    style = MaterialTheme.typography.bodySmall,
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
            value = firstName,
            onValueChange = { firstName = it },
            label = {
                Text(
                    text = stringResource(R.string.firstname),
                )
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.enterfirstname),
                    style = MaterialTheme.typography.bodySmall
                )

            },
            leadingIcon = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "first name"
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
        )
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = {
                Text(
                    text = stringResource(R.string.lastname),
                )
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.enterlastname),
                    style = MaterialTheme.typography.bodySmall
                )

            },
            leadingIcon = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "last name"
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
                    text = stringResource(R.string.password)
                )
            },
            singleLine = true,
            placeholder = {
                Text(
                    text = stringResource(R.string.enterpassword),
                    style = MaterialTheme.typography.bodySmall
                )

            },
            leadingIcon = {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = "lock"
                )
            },
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Default.Visibility
                else
                    Icons.Default.VisibilityOff
                val description = if (passwordVisible) "Hide password" else "Show password"
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, description)
                }
            },
            modifier = Modifier
                .fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)

        )
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = {
                Text(
                    text = stringResource(R.string.confirmpassword)
                )
            },
            placeholder = {
                Text(
                    text = "Confirm your password...",
                    style = MaterialTheme.typography.bodySmall
                )

            },
            leadingIcon = {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = "lock"
                )
            },
            singleLine = true,
            trailingIcon = {
                val image = if (confirmPasswordVisible)
                    Icons.Default.Visibility
                else
                    Icons.Default.VisibilityOff
                val description = if (confirmPasswordVisible) "Hide password" else "Show password"
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(imageVector = image, description)
                }
            },
            modifier = Modifier
                .fillMaxWidth(),
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)

        )
        Button(
            onClick = {
                registered(email, password, context)
            },
            modifier = Modifier
                .padding(top = 24.dp)
                .width(intrinsicSize = IntrinsicSize.Max),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 10.dp),
                text = stringResource(R.string.registerbutton),
                textAlign = TextAlign.Center
            )

        }
    }
}

//@Composable
//fun RegisterButton(email: String, password: String, context: Context) {
//
//}

fun registered(email: String, password: String, context: Context) {
    if (email == "ania" && password == "1234") {
        Toast.makeText(context, "Logged in successfully", Toast.LENGTH_SHORT).show()
    } else {
        Toast.makeText(context, "Couldn't log in", Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterPreview() {
    val context = LocalContext.current
    AppartamentyTheme {
        RegisterScreen(context)
    }
}