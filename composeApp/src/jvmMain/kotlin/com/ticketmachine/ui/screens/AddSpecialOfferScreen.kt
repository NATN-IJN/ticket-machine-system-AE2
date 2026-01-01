package com.ticketmachine.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ticketmachine.domain.SpecialOffer
import com.ticketmachine.domain.TicketType
import com.ticketmachine.service.AdminHub
import java.time.LocalDate

@Composable
fun AddSpecialOfferScreen(
    adminHub: AdminHub,
    onBack: () -> Unit,
    onCreated: (SpecialOffer) -> Unit
) {
    var destinationName by remember { mutableStateOf("") }
    var discountText by remember { mutableStateOf("") }
    var startDateText by remember { mutableStateOf("") }
    var endDateText by remember { mutableStateOf("") }
    var ticketType by remember { mutableStateOf(TicketType.SINGLE) }

    var message by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var createdOffer by remember { mutableStateOf<SpecialOffer?>(null) }

    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Add Special Offer", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = destinationName,
            onValueChange = { destinationName = it },
            label = { Text("Destination name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Text("Ticket type", style = MaterialTheme.typography.titleMedium)

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = ticketType == TicketType.SINGLE,
                    onClick = { ticketType = TicketType.SINGLE }
                )
                Text("SINGLE")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = ticketType == TicketType.RETURN,
                    onClick = { ticketType = TicketType.RETURN }
                )
                Text("RETURN")
            }
        }

        OutlinedTextField(
            value = discountText,
            onValueChange = { discountText = it },
            label = { Text("Discount factor (e.g. 0.8)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = startDateText,
            onValueChange = { startDateText = it },
            label = { Text("Start date (YYYY-MM-DD)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = endDateText,
            onValueChange = { endDateText = it },
            label = { Text("End date (YYYY-MM-DD)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        message?.let { Text(it) }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = {
                error = null
                message = null
                createdOffer = null

                val trimmedName = destinationName.trim()
                val discount = discountText.trim().toDoubleOrNull()
                val startDate = runCatching { LocalDate.parse(startDateText.trim()) }.getOrNull()
                val endDate = runCatching { LocalDate.parse(endDateText.trim()) }.getOrNull()

                when {
                    trimmedName.isEmpty() -> {
                        error = "Please enter a destination name."
                        return@Button
                    }
                    discount == null -> {
                        error = "Please enter a valid discount factor."
                        return@Button
                    }
                    discount <= 0.0 || discount >= 1.0 -> {
                        error = "Discount factor must be between 0 and 1"
                        return@Button
                    }
                    startDate == null || endDate == null -> {
                        error = "Please enter valid dates (YYYY-MM-DD)."
                        return@Button
                    }
                    endDate.isBefore(startDate) -> {
                        error = "End date must be on or after the start date."
                        return@Button
                    }
                }

                val offer = adminHub.addSpecialOffer(
                    destinationName = trimmedName,
                    ticketType = ticketType,
                    discountFactor = discount,
                    startDate = startDate,
                    endDate = endDate
                )

                if (offer == null) {
                    error = "Failed to create special offer (destination may not exist)."
                } else {
                    message = "Special offer created successfully."
                    createdOffer = offer
                    onCreated(offer)
                }
            }) {
                Text("Create")
            }

            OutlinedButton(onClick = onBack) {
                Text("Back")
            }
        }

        createdOffer?.let { offer ->
            HorizontalDivider()
            Card(Modifier.fillMaxWidth()) {
                Column(
                    Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("Created Offer", style = MaterialTheme.typography.titleMedium)
                    Text("Destination: ${offer.destination.name}")
                    Text("Type: ${offer.ticketType}")
                    Text("Discount factor: ${"%.2f".format(offer.discountFactor)}")
                    Text("Valid: ${offer.startDate} to ${offer.endDate}")
                }
            }
        }
    }
}
