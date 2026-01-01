package com.ticketmachine.ui.screens
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private enum class AdminMenuOption {
    VIEW_DESTINATION,
    ADD_DESTINATION,
    CHANGE_ALL_TICKET_PRICES,
    ADD_SPECIAL_OFFER,
    SEARCH_SPECIAL_OFFER,

}

@Composable
fun AdminMenuScreen(
    onViewDestination: () -> Unit,
    onAddDestination: () -> Unit,
    onChangeAllTicketPrices: () -> Unit,
    onAddSpecialOffer: () -> Unit,
    onSearchSpecialOffer: () -> Unit,
    onBack: () -> Unit
) {
    var selected by remember { mutableStateOf(AdminMenuOption.VIEW_DESTINATION) }

        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Admin Menu", style = MaterialTheme.typography.headlineMedium)

            AdminOptionRow("View Destination", selected == AdminMenuOption.VIEW_DESTINATION) {
                selected = AdminMenuOption.VIEW_DESTINATION
            }

            AdminOptionRow("Add Destination", selected == AdminMenuOption.ADD_DESTINATION) {
                selected = AdminMenuOption.ADD_DESTINATION
            }
            AdminOptionRow("Change All Ticket Prices", selected == AdminMenuOption.CHANGE_ALL_TICKET_PRICES) {
                selected = AdminMenuOption.CHANGE_ALL_TICKET_PRICES
            }
            AdminOptionRow("Add Special Offer", selected == AdminMenuOption.ADD_SPECIAL_OFFER) {
                selected = AdminMenuOption.ADD_SPECIAL_OFFER
            }
            AdminOptionRow("Search Special Offer", selected == AdminMenuOption.SEARCH_SPECIAL_OFFER) {
                selected = AdminMenuOption.SEARCH_SPECIAL_OFFER
            }


            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    when (selected) {
                        AdminMenuOption.VIEW_DESTINATION -> onViewDestination()
                        AdminMenuOption.ADD_DESTINATION -> onAddDestination()
                        AdminMenuOption.CHANGE_ALL_TICKET_PRICES -> onChangeAllTicketPrices()
                        AdminMenuOption.ADD_SPECIAL_OFFER -> onAddSpecialOffer()
                        AdminMenuOption.SEARCH_SPECIAL_OFFER -> onSearchSpecialOffer()
                    }
                }) { Text("Continue") }

                OutlinedButton(onClick = onBack) { Text("Logout") }
            }
        }
    }

@Composable
private fun AdminOptionRow(label: String, selected: Boolean, onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onSelect)
        Spacer(Modifier.width(8.dp))
        Text(label)
    }
}

