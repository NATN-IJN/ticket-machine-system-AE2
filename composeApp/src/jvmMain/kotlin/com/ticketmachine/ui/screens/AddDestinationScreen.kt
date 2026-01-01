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
fun AddDestinationScreen(
    adminHub: AdminHub,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var single by remember { mutableStateOf("") }
    var ret by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }
    var added by remember { mutableStateOf<Destination?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Add Destination", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            singleLine = true,
            isError = error != null,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = single,
            onValueChange = { single = it },
            label = { Text("Single price") },
            singleLine = true,
            isError = error != null,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = ret,
            onValueChange = { ret = it },
            label = { Text("Return price") },
            singleLine = true,
            isError = error != null,
            modifier = Modifier.fillMaxWidth()
        )

        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {
                error = null
                message = null
                added = null

                val trimmedName = name.trim()
                val s = single.trim().toDoubleOrNull()
                val r = ret.trim().toDoubleOrNull()

                when {
                    trimmedName.isEmpty() -> {
                        error = "Please enter a destination name."
                        return@Button
                    }
                    s == null || r == null -> {
                        error = "Please enter valid numbers for prices."
                        return@Button
                    }
                    s <= 0.0 || r <= 0.0 -> {
                        error = "Prices must be greater than 0."
                        return@Button
                    }
                }

                val dest = adminHub.addDestination(trimmedName, s, r)
                if (dest == null) {
                    error = "Destination already exists."
                } else {
                    added = dest
                    message = "Destination added."

                }
            }) { Text("Add") }

            OutlinedButton(onClick = onBack) { Text("Back") }
        }

        message?.let {
            HorizontalDivider()
            Text(it)
        }

        added?.let { d ->
            Card(Modifier.fillMaxWidth()) {
                Column(
                    Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("NAME: ${d.name}", style = MaterialTheme.typography.titleMedium)
                    Text("SINGLE: £${"%.2f".format(d.singlePrice)}")
                    Text("RETURN: £${"%.2f".format(d.returnPrice)}")
                }
            }
        }
    }
}
