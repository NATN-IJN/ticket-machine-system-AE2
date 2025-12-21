package com.ticketmachine.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ticketmachine.database.DatabaseManager
import com.ticketmachine.domain.Ticket
import com.ticketmachine.domain.TicketType
import com.ticketmachine.service.TicketMachine

@Composable
fun SearchTicketScreen(
    onBack: () -> Unit,
    ticketMachine: TicketMachine,
    onPurchased: (Ticket) -> Unit
) {
    val destinations = remember { DatabaseManager.getAllDestinations() }

    var destinationName by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(TicketType.SINGLE) }

    var searchedPrice by remember { mutableStateOf<Double?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Search Ticket", style = MaterialTheme.typography.headlineMedium)

        // Destination dropdown (basic)
        OutlinedTextField(
            value = destinationName,
            onValueChange = { destinationName = it },
            label = { Text("Destination") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FilterChip(
                selected = type == TicketType.SINGLE,
                onClick = { type = TicketType.SINGLE },
                label = { Text("Single") }
            )
            FilterChip(
                selected = type == TicketType.RETURN,
                onClick = { type = TicketType.RETURN },
                label = { Text("Return") }
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onBack) { Text("Back") }
            Button(onClick = {
                error = null
                searchedPrice = ticketMachine.searchTicket(destinationName.trim(), type)
                if (searchedPrice == null) error = "Could not find ticket for that destination."
            }) { Text("Search") }
        }

        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        searchedPrice?.let { price ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Destination: ${destinationName.trim()}")
                    Text("Type: ${type}")
                    Text("Price: £${"%.2f".format(price)}")

                    Button(onClick = {
                        val bought = ticketMachine.buyTicket()
                        if (bought != null) onPurchased(bought) else error = "Purchase failed."
                    }) { Text("Buy") }
                }
            }
        }
    }
}

