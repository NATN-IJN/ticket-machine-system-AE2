package com.ticketmachine.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class MainMenuChoice { USER, ADMIN }
@Composable
fun MainMenuScreen(
    onSelectUser: () -> Unit,
    onSelectAdmin: () -> Unit
) {
    var choice by remember { mutableStateOf<MainMenuChoice?>(null) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(modifier = Modifier.widthIn(max = 420.dp)) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Ticket Machine", style = MaterialTheme.typography.headlineMedium)
                Text("Choose an option, then press Select:")

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FilterChip(
                        selected = choice == MainMenuChoice.USER,
                        onClick = { choice = MainMenuChoice.USER },
                        label = { Text("User") }
                    )
                    FilterChip(
                        selected = choice == MainMenuChoice.ADMIN,
                        onClick = { choice = MainMenuChoice.ADMIN },
                        label = { Text("Admin") }
                    )
                }

                Button(
                    onClick = {
                        when (choice) {
                            MainMenuChoice.USER -> onSelectUser()
                            MainMenuChoice.ADMIN -> onSelectAdmin()
                            null -> {}
                        }
                    },
                    enabled = choice != null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Select")
                }
            }
        }
    }
}