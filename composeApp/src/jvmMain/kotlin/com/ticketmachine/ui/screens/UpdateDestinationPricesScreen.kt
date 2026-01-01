package com.ticketmachine.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ticketmachine.domain.Destination
import com.ticketmachine.service.AdminHub

@Composable
fun UpdateDestinationPricesScreen(
    destination: Destination,
    onBack: () -> Unit,
    adminHub: AdminHub
) {
    var singleText by remember { mutableStateOf(destination.singlePrice.toString()) }
    var returnText by remember { mutableStateOf(destination.returnPrice.toString()) }
    var message by remember { mutableStateOf<String?>(null) }
    var lastUpdated by remember { mutableStateOf<Destination?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.widthIn(max = 520.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Update Destination Prices", style = MaterialTheme.typography.headlineMedium)
                Text("Destination: ${destination.name}", style = MaterialTheme.typography.titleMedium)

                Divider()

                OutlinedTextField(
                    value = singleText,
                    onValueChange = { singleText = it },
                    label = { Text("New Single Price") },
                    singleLine = true,
                    isError = error != null,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = returnText,
                    onValueChange = { returnText = it },
                    label = { Text("New Return Price") },
                    singleLine = true,
                    isError = error != null,
                    modifier = Modifier.fillMaxWidth()
                )

                error?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            message = null
                            lastUpdated = null
                            error = null

                            val newSingle = singleText.trim().toDoubleOrNull()
                            val newReturn = returnText.trim().toDoubleOrNull()



                            if (newSingle == null || newReturn == null) {
                                error = "Please enter valid prices."
                                return@Button
                            }

                            val updated = adminHub.updateDestinationPrices(
                                destination = destination,
                                newSingle = newSingle,
                                newReturn = newReturn
                            )

                            if (updated == null) {
                                error = "Update failed. Check values and try again."
                            } else {
                                lastUpdated = updated
                                message = "Prices updated successfully."
                            }
                        }
                    ) { Text("Save") }

                    OutlinedButton(onClick = onBack) { Text("Exit") }
                }

                message?.let {
                    Divider()
                    Text(it)
                }

                lastUpdated?.let { d ->
                    Spacer(Modifier.height(6.dp))
                    Card {
                        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Updated Details", style = MaterialTheme.typography.titleMedium)
                            Text("Single: £${"%.2f".format(d.singlePrice)}")
                            Text("Return: £${"%.2f".format(d.returnPrice)}")
                        }
                    }
                }
            }
        }
    }
}

