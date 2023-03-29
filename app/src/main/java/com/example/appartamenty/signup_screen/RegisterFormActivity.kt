package com.example.appartamenty.signup_screen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.appartamenty.MainActivity
import com.example.appartamenty.MainScreenLandlordActivity
import com.example.appartamenty.R
import com.example.appartamenty.ui.theme.AppartamentyTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.appartamenty.composables.CustomOutlinedTextField
import com.example.appartamenty.data.Landlord
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore


class RegisterFormActivity : ComponentActivity() {
    private val auth by lazy {
        Firebase.auth
    }
    private val database by lazy {
        Firebase.database
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppartamentyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RegisterScreen(auth, database)
                }
            }
        }
    }
}

@Composable
fun RegisterScreen(auth: FirebaseAuth, database: FirebaseDatabase) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RegisterWelcome()
        ShowForm(auth, database)
    }
}

@Composable
fun RegisterWelcome() {
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
fun ShowForm(auth: FirebaseAuth, database: FirebaseDatabase) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    var landlord: Landlord

    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    var validateEmail by remember { mutableStateOf(true) }
    var validateFirstName by remember { mutableStateOf(true) }
    var validateLastName by remember { mutableStateOf(true) }
    var validatePassword by rememberSaveable { mutableStateOf(true) }
    var validatePasswordsEqual by rememberSaveable { mutableStateOf(true) }
    var validateConfirmPassword by rememberSaveable { mutableStateOf(true) }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var isConfirmPasswordVisible by rememberSaveable { mutableStateOf(false) }

    var validateEmailError = "The email address format is invalid."
    var validatePasswordError =
        "The password must be at least 8 characters long and include a number."
    var validateEqualPasswordError = "The passwords don't match."

    var database = FirebaseFirestore.getInstance()

    fun validateData(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        val passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$".toRegex()

        validateFirstName = firstName.isNotBlank()
        validateLastName = lastName.isNotBlank()
        validateEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        validatePassword = passwordRegex.matches(password)
        validateConfirmPassword = passwordRegex.matches((confirmPassword))
        validatePasswordsEqual = password == confirmPassword

        return validateFirstName && validateLastName && validateEmail && validatePassword && validateConfirmPassword && validatePasswordsEqual
    }

    fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        if (validateData(firstName, lastName, email, password, confirmPassword)) {
            Log.d(
                MainActivity::class.java.simpleName,
                "User ID: ${auth.currentUser?.uid}"
            )
            auth.createUserWithEmailAndPassword(email, password)
            var landlordId = auth.currentUser?.uid
            Log.d(
                MainActivity::class.java.simpleName,
                "User ID: ${landlordId}"
            )
            landlord = Landlord(firstName, lastName, email)
            if (landlordId != null) {
                val handle = database.collection("landlords").document(landlordId).set(landlord)
                handle.addOnSuccessListener {
                    Log.d(
                        MainActivity::class.java.simpleName,
                        "Adding to database successful"
                    )
                }
                val intent = Intent(context, MainScreenLandlordActivity::class.java)
                intent.putExtra("landlordId", landlordId)
                context.startActivity(intent)
                handle.addOnFailureListener {
                    Log.d(
                        MainActivity::class.java.simpleName,
                        "Adding to database failed"
                    )
                }
            }
            //

            Log.d(
                MainActivity::class.java.simpleName,
                "First name: $firstName, Last name: $lastName, Email: $email, Password: $password"
            )

        } else {
            Toast.makeText(
                context,
                context.getString(R.string.reviewformfields),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CustomOutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = stringResource(R.string.emailaddress),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            showError = !validateEmail,
            errorMessage = validateEmailError,
            leadingIconImageVector = Icons.Default.Email,
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        CustomOutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = stringResource(R.string.firstname),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            showError = !validateFirstName,
            leadingIconImageVector = Icons.Default.PermIdentity,
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        CustomOutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = stringResource(R.string.lastname),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            showError = !validateLastName,
            leadingIconImageVector = Icons.Default.PermIdentity,
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        CustomOutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = stringResource(R.string.password),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            isPasswordField = true,
            isPasswordVisible = isPasswordVisible,
            onVisibilityChange = { isPasswordVisible = it },
            singleLine = true,
            showError = !validatePassword,
            errorMessage = validatePasswordError,
            leadingIconImageVector = Icons.Default.Password,
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        CustomOutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = stringResource(R.string.confirmpassword),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            isPasswordField = true,
            isPasswordVisible = isConfirmPasswordVisible,
            onVisibilityChange = { isConfirmPasswordVisible = it },
            singleLine = true,
            showError = !validateConfirmPassword || !validatePasswordsEqual,
            errorMessage = if (!validateConfirmPassword) validatePasswordError else validateEqualPasswordError,
            leadingIconImageVector = Icons.Default.Password,
            keyboardActions = KeyboardActions(
                onNext = { focusManager.clearFocus() }
            )
        )
        Button(
            onClick = {
                register(firstName, lastName, email, password, confirmPassword)
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
        OutlinedButton(
            onClick = {
                context.startActivity(Intent(context, MainActivity::class.java))
            },
            modifier = Modifier
                .padding(top = 24.dp)
                .width(intrinsicSize = IntrinsicSize.Max),
            enabled = true,
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 10.dp),
                text = stringResource(R.string.haveanaccount),
                textAlign = TextAlign.Center
            )

        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterPreview() {
    AppartamentyTheme {
        RegisterScreen(Firebase.auth, Firebase.database)
    }
}