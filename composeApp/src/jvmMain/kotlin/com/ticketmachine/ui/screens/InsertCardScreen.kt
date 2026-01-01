package com.ticketmachine.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun InsertCardScreen(
    onBack: () -> Unit,
    onContinue: (String) -> Boolean
) {
    var cardNumber by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Insert Card", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = cardNumber,
            onValueChange = {
                cardNumber = it.filter(Char::isDigit)
                if (cardNumber.length == 16) error = null
            },
            label = { Text("Card number") },
            singleLine = true,
            isError = error != null,
            modifier = Modifier.fillMaxWidth()
        )

        error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onBack) {
                Text("Back")
            }

            Button(
                onClick = {
                    when {
                        cardNumber.isBlank() ->
                            error = "Please enter a card number"

                        cardNumber.length < 16 ->
                            error = "Card number must have 16 digits"

                        else -> {
                            val ok = onContinue(cardNumber)
                            error = if (ok) null else "Card not found"
                        }
                    }
                }
            ) {
                Text("Continue")
            }
        }
    }
}
