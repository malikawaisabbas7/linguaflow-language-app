package com.linguaflow.app.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.linguaflow.app.di.AppContainer
import com.linguaflow.app.navigation.Destinations
import com.linguaflow.app.ui.components.PrimaryGradientButton
import com.linguaflow.app.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(navController: NavHostController) {
    val context = LocalContext.current
    val container = remember { AppContainer.get(context) }
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.factory(container))
    val authState by authViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(authState.isSuccess) {
        if (authState.isSuccess) {
            // Persist chosen language/level defaults; Onboarding lets the user refine them.
            scope.launch { container.preferencesManager.setOnboardingDone(false) }
            navController.navigate(Destinations.Onboarding.route) {
                popUpTo(Destinations.Register.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 28.dp, vertical = 40.dp)
    ) {
        Text("Create account", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text(
            "Start learning a new language today",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(28.dp))

        OutlinedTextField(
            value = name, onValueChange = { name = it },
            label = { Text("Full name") }, singleLine = true,
            shape = MaterialTheme.shapes.medium, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(14.dp))
        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("Email") }, singleLine = true,
            shape = MaterialTheme.shapes.medium, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(14.dp))
        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Password") }, singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            shape = MaterialTheme.shapes.medium, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(14.dp))
        OutlinedTextField(
            value = confirmPassword, onValueChange = { confirmPassword = it },
            label = { Text("Confirm password") }, singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            shape = MaterialTheme.shapes.medium, modifier = Modifier.fillMaxWidth()
        )

        (localError ?: authState.errorMessage)?.let {
            Spacer(modifier = Modifier.height(10.dp))
            Text(it, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryGradientButton(
            text = if (authState.isLoading) "Creating account..." else "Create Account",
            enabled = !authState.isLoading && name.isNotBlank() && email.isNotBlank() &&
                password.length >= 6 && password == confirmPassword,
            onClick = {
                if (password != confirmPassword) {
                    localError = "Passwords do not match"
                } else {
                    localError = null
                    // Native/target language + level default here; refined in Onboarding.
                    authViewModel.register(
                        name = name,
                        email = email,
                        password = password,
                        nativeLanguage = "English",
                        targetLanguage = "Spanish",
                        proficiencyLevel = "Beginner"
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Text("Already have an account? ", fontSize = 14.sp)
            Text(
                "Log in",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    navController.navigate(Destinations.Login.route) {
                        popUpTo(Destinations.Register.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
