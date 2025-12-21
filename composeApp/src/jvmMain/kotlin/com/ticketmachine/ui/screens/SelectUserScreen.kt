package com.ticketmachine.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ticketmachine.domain.User

@Composable
fun SelectUserScreen(
    onContinue: (User) -> Unit,
    onBack: () -> Unit,
) {
    var username by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Select User", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedButton(onClick = onBack) { Text("Back") }
        Button(
            onClick = { onContinue(User(username.trim())) },
            enabled = username.trim().isNotEmpty()
        ) { Text("Continue") }
    }
}