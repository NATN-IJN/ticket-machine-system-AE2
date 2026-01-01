package com.ticketmachine.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ticketmachine.service.AdminHub

@Composable
fun AdminLoginScreen(
    onBack: () -> Unit,
    onLogin: () -> Unit,
    adminHub: AdminHub
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Admin Login", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onBack) { Text("Back") }

            Button(onClick = {
                error = null

                val u = username.trim()
                val p = password

                when {
                    u.isBlank() -> {
                        error = "Please enter a username."
                        return@Button
                    }
                    p.isBlank() -> {
                        error = "Please enter a password."
                        return@Button
                    }
                }

                val ok = adminHub.login(u, p)
                if (ok) {
                    onLogin()
                } else {
                    error = "Invalid username or password."
                }
            }) {
                Text("Login")
            }
        }
    }
}
