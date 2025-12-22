package com.ticketmachine.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ticketmachine.domain.Ticket
import com.ticketmachine.service.TicketMachine

@Composable
fun ViewTicketScreen(
    ticketMachine: TicketMachine,
    onBack: () -> Unit
) {
    var ticketRef by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf<String?>(null) }
    var ticket by remember { mutableStateOf<Ticket?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("View Ticket", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = ticketRef,
            onValueChange = { ticketRef = it },
            label = { Text("Ticket Reference") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    resultText = null
                    ticket = null

                    val trimmed = ticketRef.trim()
                    if (trimmed.isEmpty()) {
                        resultText = "Please enter a ticket reference."
                        return@Button
                    }

                    val found = ticketMachine.viewTicket(trimmed)
                    if (found == null) {
                        resultText = "Ticket not found"
                    } else {
                        ticket = found
                        resultText = "Ticket found."
                    }
                }
            ) { Text("View") }

            OutlinedButton(onClick = onBack) { Text("Back") }
        }

        if (resultText != null) {
            Divider()
            Text(resultText!!)
        }

        ticket?.let { t ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Ticket Details", style = MaterialTheme.typography.titleMedium)

                    DetailRow("TICKET REFERENCE:", t.ticketRef)
                    DetailRow("ORIGIN STATION:", t.origin)
                    Text("TO", fontWeight = FontWeight.Bold)
                    DetailRow("DESTINATION STATION:", t.destination.name)
                    DetailRow("PRICE:", "£${"%.2f".format(t.price)} [${t.type}]")
                    DetailRow("STATUS:", t.status.name)
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, fontWeight = FontWeight.Bold)
        Text(value)
    }
}