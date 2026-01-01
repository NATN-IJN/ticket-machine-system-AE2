package com.ticketmachine.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ticketmachine.domain.Ticket
import com.ticketmachine.domain.TicketStatus
import com.ticketmachine.service.TicketMachine

@Composable
fun CancelTicketScreen(
    ticketMachine: TicketMachine,
    onBack: () -> Unit
) {
    var ticketRef by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var resultText by remember { mutableStateOf<String?>(null) }
    var lastTicket by remember { mutableStateOf<Ticket?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Cancel Ticket", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = ticketRef,
            onValueChange = { ticketRef = it },
            label = { Text("Ticket Reference") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Text(
            "Are you sure you want to cancel this ticket?",
            color = MaterialTheme.colorScheme.error
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    error = null
                    resultText = null
                    lastTicket = null

                    val trimmed = ticketRef.trim()
                    when {
                        trimmed.isEmpty() -> {
                            error = "Please enter a ticket reference."
                        }
                        ticketMachine.viewTicket(trimmed) == null -> {
                            error = "Ticket not found."
                        }
                        ticketMachine.viewTicket(trimmed)!!.status == TicketStatus.CANCELLED -> {
                            val existing = ticketMachine.viewTicket(trimmed)!!
                            lastTicket = existing
                            error = "Ticket is already cancelled."
                        }
                        else -> {
                            val cancelled = ticketMachine.cancelTicket(trimmed)
                            if (cancelled == null) {
                                error = "Cancel failed."
                            } else {
                                lastTicket = cancelled
                                resultText = "Ticket cancelled successfully."
                            }
                        }
                    }
                }
            ) { Text("Cancel") }

            OutlinedButton(onClick = onBack) { Text("Back") }
        }

        resultText?.let {
            HorizontalDivider()
            Text(it)
        }

        lastTicket?.let { t ->
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
