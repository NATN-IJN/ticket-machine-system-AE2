package com.ticketmachine.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ticketmachine.domain.Ticket


@Composable
fun ConfirmationScreen(
    ticket: Ticket,
    onDone: () -> Unit
) {
        Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Purchase Complete", style = MaterialTheme.typography.headlineMedium)
            Text("TICKET REFERENCE: ${ticket.ticketRef}")
            Text("ORIGIN STATION: ${ticket.origin}")
            Text("TO")
            Text("DESTINATION STATION: ${ticket.destination.name.trim()}")
            Text("PRICE: £${"%.2f".format(ticket.price)} [${ticket.type}]")

            Button(onClick = onDone) { Text("Done") }
        }
}

