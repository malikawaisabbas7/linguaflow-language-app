package com.linguaflow.app.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.linguaflow.app.di.AppContainer
import com.linguaflow.app.navigation.Destinations
import com.linguaflow.app.ui.components.PrimaryGradientButton
import com.linguaflow.app.viewmodel.AuthViewModel

@Composable
fun LoginScreen(navController: NavHostController) {
    val context = LocalContext.current
    val container = remember { AppContainer.get(context) }
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.factory(container))
    val authState by authViewModel.uiState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(true) }

    LaunchedEffect(authState.isSuccess) {
        if (authState.isSuccess) {
            navController.navigate(Destinations.Home.route) {
                popUpTo(Destinations.Login.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 28.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(imageVector = Icons.Filled.Language, contentDescription = null, modifier = Modifier.size(56.dp))
        Spacer(modifier = Modifier.height(12.dp))
        Text("Welcome back", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text("Continue your language journey", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = "Toggle password visibility"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = rememberMe, onCheckedChange = { rememberMe = it })
                Text("Remember me", fontSize = 13.sp)
            }
            TextButton(onClick = { navController.navigate(Destinations.ForgotPassword.route) }) {
                Text("Forgot password?", fontSize = 13.sp)
            }
        }

        authState.errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        PrimaryGradientButton(
            text = if (authState.isLoading) "Signing in..." else "Log In",
            enabled = !authState.isLoading && email.isNotBlank() && password.isNotBlank(),
            onClick = { authViewModel.login(email, password) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { /* Google Sign-In flow via FirebaseAuthManager.signInWithGoogleCredential */ },
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) {
            Text("Continue with Google")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Text("Don't have an account? ", fontSize = 14.sp)
            Text(
                "Sign up",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickableText { navController.navigate(Destinations.Register.route) }
            )
        }
    }
}

// Small extension to keep clickable text concise without importing extra modifiers inline above.
private fun Modifier.clickableText(onClick: () -> Unit): Modifier =
    this.then(Modifier.clickable(onClick = onClick))
