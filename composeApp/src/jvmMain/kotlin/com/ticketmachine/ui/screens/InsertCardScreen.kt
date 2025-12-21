package com.ticketmachine.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InsertCardScreen(
    onBack: () -> Unit,
    onContinue: (String) -> Unit
) {
    var cardNumber by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Insert Card", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = cardNumber,
            onValueChange = { cardNumber = it.filter(Char::isDigit) },
            label = { Text("Card number") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onBack) { Text("Back") }
            Button(
                onClick = { onContinue(cardNumber.trim()) },
                enabled = cardNumber.trim().length >= 12
            ) { Text("Continue") }
        }
    }
}