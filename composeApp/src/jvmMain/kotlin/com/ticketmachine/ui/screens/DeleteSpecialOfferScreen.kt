package com.ticketmachine.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ticketmachine.domain.SpecialOffer
import com.ticketmachine.service.AdminHub

@Composable
fun DeleteSpecialOfferScreen(
    adminHub: AdminHub,
    offerId: Int,
    onBack: () -> Unit
) {
    var offer by remember { mutableStateOf<SpecialOffer?>(null) }
    var message by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(offerId) {
        message = null
        error = null

        val found = adminHub.searchSpecialOfferId(offerId)
        if (found == null) {
            error = "Special offer not found ."
        } else {
            offer = found
        }

    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(modifier = Modifier.widthIn(max = 560.dp)) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Delete Special Offer", style = MaterialTheme.typography.headlineMedium)

                Text("Offer ID: $offerId", style = MaterialTheme.typography.titleMedium)

                Divider()

                when {
                    error != null -> {
                        Text(error!!, color = MaterialTheme.colorScheme.error)
                    }

                    offer != null -> {
                        val o = offer!!

                        Text("Destination: ${o.destination.name}")
                        Text("Ticket Type: ${o.ticketType}")
                        Text("Discount Factor: ${o.discountFactor}")
                        Text("Start Date: ${o.startDate}")
                        Text("End Date: ${o.endDate}")
                        Text("Status: ${o.status}")

                        Spacer(Modifier.height(8.dp))

                        Text(
                            "Are you sure you want to delete this offer?",
                            color = MaterialTheme.colorScheme.error
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    message = null
                                    error = null

                                    val deleted = adminHub.deleteOffer(offerId)
                                    if (deleted == null) {
                                        error = "Delete failed."
                                    } else {
                                        message = "Offer deleted successfully."
                                    }
                                }
                            ) {
                                Text("Delete")
                            }

                            OutlinedButton(onClick = onBack) {
                                Text("Back")
                            }
                        }
                    }
                }

                message?.let {
                    Divider()
                    Text(it)
                }
            }
        }
    }
}