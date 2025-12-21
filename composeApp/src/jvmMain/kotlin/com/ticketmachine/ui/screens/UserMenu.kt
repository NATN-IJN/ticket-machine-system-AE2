package com.ticketmachine.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UserMenuScreen(
    onSearchTicket: () -> Unit,
    onViewTicket: () -> Unit,
    onCancelTicket: () -> Unit,
    onBack: () -> Unit
) {
    Card(Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("User Menu", style = MaterialTheme.typography.headlineMedium)

            Button(onClick = onSearchTicket, modifier = Modifier.fillMaxWidth()) {
                Text("Search Ticket")
            }
            Button(onClick = onViewTicket, modifier = Modifier.fillMaxWidth()) {
                Text("View Ticket")
            }
            Button(onClick = onCancelTicket, modifier = Modifier.fillMaxWidth()) {
                Text("Cancel Ticket")
            }

            OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text("Back")
            }
        }
    }
}