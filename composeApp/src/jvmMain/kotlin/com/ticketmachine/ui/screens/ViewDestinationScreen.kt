package com.ticketmachine.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ticketmachine.domain.Destination
import com.ticketmachine.service.AdminHub

@Composable
fun ViewDestinationScreen(
    adminHub: AdminHub,
    onBack: () -> Unit,
    onEdit: (Destination) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<Destination?>(null) }
    var message by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxWidth().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("View Destination", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Destination name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = {
                message = null
                error = null
                result = adminHub.viewDestination(name.trim())
                if (result == null) error = "Destination not found."
            }) {
                Text("Search")
            }

            OutlinedButton(onClick = onBack) { Text("Back") }
        }

        message?.let { Text(it) }

        result?.let { d ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Divider()
                    Text("Name: ${d.name}")
                    Text("Single: £${"%.2f".format(d.singlePrice)}")
                    Text("Return: £${"%.2f".format(d.returnPrice)}")
                    Text("Takings: £${"%.2f".format(d.takings)}")
                    Text("Sales: ${d.salesCount}")
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = { onEdit(d) }) {
                    Text("Edit Prices")
                }
            }
        }
    }
}