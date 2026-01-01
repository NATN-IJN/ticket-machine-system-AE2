package com.ticketmachine.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ticketmachine.domain.Ticket

@Composable
fun ConfirmationScreen(
    ticket: Ticket,
    onDone: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            Modifier.fillMaxWidth().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Purchase Complete", style = MaterialTheme.typography.headlineMedium)

            DetailRow("TICKET REFERENCE:", ticket.ticketRef)
            DetailRow("ORIGIN STATION:", ticket.origin)
            Text("TO")
            DetailRow("DESTINATION STATION:", ticket.destination.name.trim())
            DetailRow("PRICE:", "£${"%.2f".format(ticket.price)} [${ticket.type}]")

            Button(onClick = onDone) { Text("Done") }
        }
    }
}



@Composable
private fun DetailRow(label: String, value: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, fontWeight = FontWeight.Bold)
        Text(value)
    }
}
