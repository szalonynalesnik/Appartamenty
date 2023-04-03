
package com.example.appartamenty

import android.annotation.SuppressLint
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.example.appartamenty.MainActivity.Companion.TAG
import com.example.appartamenty.signup_screen.RegisterFormActivity
import com.example.appartamenty.ui.theme.AppartamentyTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import android.content.Context


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
            AppartamentyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(auth)
                }
            }
        }
    }
}

@Composable
fun MainScreen(auth: FirebaseAuth) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoginWelcome()
        LoginScreen(Firebase.auth)
    }
}


fun checkIfLandlord(context: Context){
    val userId = Firebase.auth.currentUser?.uid.toString()
    var isLandlord: Boolean = false
    // on below line creating variable for freebase database
    // and database reference.
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // on below line getting data from our database
    db.collection("landlords").document(userId).get()
        .addOnSuccessListener { doc ->
            if (doc.data != null) {
                logInUser(context, true, userId)
                Log.d(TAG, "DocumentSnapshot data: ${doc.data}")
            } else {
                logInUser(context, false, userId)
            }
        }
        .addOnFailureListener {
            Log.d(MainActivity::class.java.simpleName, "Is user a landlord: $isLandlord")
        }
}

fun logInUser(context: Context, isLandlord: Boolean, userId: String){

    if (isLandlord){
        val intent = Intent(context, MainScreenLandlordActivity::class.java)
        intent.putExtra("landlordId", userId)
        Log.d(MainActivity::class.java.simpleName, "Landlord ID passed on: $userId")
        context.startActivity(intent)
    }
    else{
        val intent = Intent(context, MainScreenTenantActivity::class.java)
        intent.putExtra("tenantId", userId)
        Log.d(MainActivity::class.java.simpleName, "Tenant ID passed on: $userId")
        context.startActivity(intent)
    }

}

@SuppressLint("UnrememberedMutableState")
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
    val context = LocalContext.current
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
            singleLine = true,
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
            isError = !isEmailValid
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
            visualTransformation =
            if (isPasswordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.clearFocus() }
            ),
            isError = !isPasswordValid

        )
        Button(
            onClick = {
                      auth.signInWithEmailAndPassword(email, password)
                          .addOnCompleteListener {
                              if (it.isSuccessful){
                                  Log.d(TAG, "The user has logged in successfully.")
                                  checkIfLandlord(context)

                              }
                              else{
                                  Log.w(TAG, "The user has failed to log in.", it.exception)

                              }
                          }
            },
            modifier = Modifier
                .padding(top = 10.dp)
                .width(intrinsicSize = IntrinsicSize.Max),
            enabled = isEmailValid && isPasswordValid
        ) {
            Text(
                text = stringResource(R.string.loginbutton),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 10.dp),
                textAlign = TextAlign.Center,
            )
        }
        OutlinedButton(
            onClick = {
                context.startActivity(Intent(context, RegisterFormActivity::class.java))
            },
            modifier = Modifier
                .padding(top = 10.dp)
                .width(intrinsicSize = IntrinsicSize.Max),
            enabled = true,
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 10.dp),
                text = stringResource(R.string.donthaveanaccount),
                textAlign = TextAlign.Center
            )

        }
    }
}

@Composable
fun LoginWelcome() {
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
            text = "Nice to see you again!",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    AppartamentyTheme {
        MainScreen(Firebase.auth)
    }
}
