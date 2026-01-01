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
fun SearchSpecialOfferScreen(
    adminHub: AdminHub,
    onBack: () -> Unit,
    onDelete: (Int) -> Unit
) {
    var idText by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var offer by remember { mutableStateOf<SpecialOffer?>(null) }

            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Search Special Offer", style = MaterialTheme.typography.headlineMedium)

                OutlinedTextField(
                    value = idText,
                    onValueChange = { idText = it },
                    label = { Text("Offer ID") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            error = null
                            offer = null

                            val trimmedId = idText.trim()

                            when {
                                trimmedId.isBlank() -> {
                                    error = "Please enter an ID"
                                }

                                trimmedId.toIntOrNull() == null -> {
                                    error = "Please enter a valid numeric Offer ID."
                                }

                                else -> {
                                    val found = adminHub.searchSpecialOfferId(trimmedId.toInt())
                                    if (found == null) {
                                        error = "Special offer not found."
                                    } else {
                                        offer = found
                                    }
                                }
                            }
                        }

                    ) { Text("Search") }

                    OutlinedButton(onClick = onBack) { Text("Back") }
                }

                offer?.let { o ->
                    Divider()

                    Card {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("Offer Details", style = MaterialTheme.typography.titleMedium)
                            Text("Offer ID: ${o.offerId}")
                            Text("Destination: ${o.destination.name}")
                            Text("Ticket type: ${o.ticketType}")
                            Text("Discount factor: ${o.discountFactor}")
                            Text("Start date: ${o.startDate}")
                            Text("End date: ${o.endDate}")
                            Text("Status: ${o.status}")
                        }
                    }

                    Spacer(Modifier.height(6.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = {
                                // Navigate to Delete screen
                                onDelete(o.offerId)
                            }
                        ) { Text("Delete") }
                    }
                }
            }
        }