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
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Select User", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                if (it.isNotBlank()) error = null
            },
            label = { Text("Username") },
            singleLine = true,
            isError = error != null,
            modifier = Modifier.fillMaxWidth()
        )

        error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onBack) {
                Text("Back")
            }

            Button(
                onClick = {
                    if (username.isBlank()) {
                        error = "Username cannot be blank"
                    } else {
                        error = null
                        onContinue(User(username.trim()))
                    }
                }
            ) {
                Text("Continue")
            }
        }
    }
}
