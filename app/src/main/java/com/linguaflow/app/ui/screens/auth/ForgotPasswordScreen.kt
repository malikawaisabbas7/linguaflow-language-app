package com.linguaflow.app.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.linguaflow.app.di.AppContainer
import com.linguaflow.app.ui.components.PrimaryGradientButton
import com.linguaflow.app.viewmodel.AuthViewModel

@Composable
fun ForgotPasswordScreen(navController: NavHostController) {
    val context = LocalContext.current
    val container = remember { AppContainer.get(context) }
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.factory(container))
    val authState by authViewModel.uiState.collectAsState()

    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text("Reset password", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(
            "Enter your email and we'll send you a reset link",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(28.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        )

        authState.errorMessage?.let {
            Spacer(modifier = Modifier.height(10.dp))
            Text(it, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        PrimaryGradientButton(
            text = if (authState.isSuccess) "Email sent!" else if (authState.isLoading) "Sending..." else "Send reset link",
            enabled = email.isNotBlank() && !authState.isSuccess && !authState.isLoading,
            onClick = { authViewModel.sendPasswordReset(email) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { navController.popBackStack() }) {
            Text("Back to login")
        }
    }
}
