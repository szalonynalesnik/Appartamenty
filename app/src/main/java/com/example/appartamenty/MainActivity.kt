package com.example.appartamenty

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appartamenty.MainActivity.Companion.TAG
import com.example.appartamenty.login_screen.LoginFormActivity
import com.example.appartamenty.login_screen.logged
import com.example.appartamenty.navigation.NavigationGraph
import com.example.appartamenty.signup_screen.RegisterFormActivity
import com.example.appartamenty.ui.theme.AppartamentyTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }
    private val auth by lazy{
        Firebase.auth
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            NavigationGraph()
            AppartamentyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen(auth)
                }
            }
        }
    }
}

@Composable
fun WelcomeScreen(context: Context) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MainWelcome(context = context)
        LoginOrRegisterChooser(context = context)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(auth: FirebaseAuth) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val isEmailValid by derivedStateOf {
        Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    val isPasswordValid by derivedStateOf {
        password.length > 7
    }

    val focusManager = LocalFocusManager.current

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
                    style = MaterialTheme.typography.bodySmall
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
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            isError = isEmailValid
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            singleLine = true,
            label = {
                Text(
                    text = stringResource(R.string.password)
                )
            },
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
                val image = if (isPasswordVisible)
                    Icons.Default.Visibility
                else
                    Icons.Default.VisibilityOff
                val description = if (isPasswordVisible) "Hide password" else "Show password"
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(imageVector = image, description)
                }
            },
            modifier = Modifier
                .fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.clearFocus() }
            ),
            isError = isPasswordValid

        )
        Button(
            onClick = {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                 if (it.isSuccessful) {
                     Log.d(TAG, "The user has successfully logged in")
                 }
                    else{
                        Log.d(TAG, "The user has faileld to log in", it.exception)
                 }
                }
            },
            modifier = Modifier
                .padding(top = 24.dp)
                .width(intrinsicSize = IntrinsicSize.Max),
            enabled = isEmailValid && isPasswordValid
        ) {
            Text(
                text = stringResource(R.string.loginbutton),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 10.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun MainWelcome(context: Context) {
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

@Composable
fun LoginOrRegisterChooser(context: Context) {
    Column(
        modifier = Modifier
            .width(intrinsicSize = IntrinsicSize.Max),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedButton(
            onClick = {
                val intent = Intent(context, LoginFormActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 10.dp),
                text = stringResource(R.string.loginbutton),
                textAlign = TextAlign.Center
            )

        }
        OutlinedButton(
            onClick = {
                val intent = Intent(context, RegisterFormActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    AppartamentyTheme {
        LoginScreen(Firebase.auth)
    }
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    AppartamentyTheme {
//    }
//}
