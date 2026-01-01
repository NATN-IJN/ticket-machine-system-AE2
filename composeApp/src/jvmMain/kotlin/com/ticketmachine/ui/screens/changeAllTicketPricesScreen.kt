package com.ticketmachine.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ticketmachine.service.AdminHub

@Composable
fun ChangeAllTicketPricesScreen(
    adminHub: AdminHub,
    onBack: () -> Unit
) {
    var factorText by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Change All Ticket Prices", style = MaterialTheme.typography.headlineMedium)

                Text(
                    "Enter a multiplier (e.g. 1.10 increases by 10%, 0.90 decreases by 10%).",
                    style = MaterialTheme.typography.bodyMedium
                )

                OutlinedTextField(
                    value = factorText,
                    onValueChange = { factorText = it },
                    label = { Text("Price multiplier") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                message?.let { Text(it) }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = {
                        error = null
                        message = null

                        val factor = factorText.trim().toDoubleOrNull()
                        if (factor == null) {
                            error = "Please enter a valid number."
                            return@Button
                        }

                        val updated = adminHub.changeAllTicketPrices(factor = factor)

                        if (updated == null) {
                            error = "Update failed, please enter a valid price multiplier."
                        } else if (updated.isEmpty()) {
                            error = "No destinations found to update."
                        } else {
                            message = "All ticket prices updated successfully."
                        }
                    }) {
                        Text("Apply")
                    }

                    OutlinedButton(onClick = onBack) { Text("Back") }
                }
            }
        }