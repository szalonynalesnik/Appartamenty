package com.example.appartamenty.signup_screen

import com.example.appartamenty.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(viewModel: SignUpViewModel = hiltViewModel()) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var scope = rememberCoroutineScope()
    val context = LocalContext.current
    val state = viewModel.signUpState.collectAsState(initial = null)
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

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
                    text = "Email address",
                )
            },
            placeholder = {
                Text(
                    text = "Enter email address",
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
            value = password,
            onValueChange = { password = it },
            label = {
                Text(
                    text = "Password"
                )
            },
            singleLine = true,
            placeholder = {
                Text(
                    text = "Enter password",
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
                text = "Register",
                textAlign = TextAlign.Center
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                if (state.value?.isLoading == true) {

                }

            }
            Text(text = "Already have an account? Log in")
            Text("or connect with")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = {/*TODO*/ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = "Google icon",
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
        }
    }

}
