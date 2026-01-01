package com.ticketmachine.ui.screens
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private enum class UserMenuOption { SEARCH, VIEW, CANCEL , INSERT}

@Composable
fun UserMenuScreen(
    onSearchTicket: () -> Unit,
    onViewTicket: () -> Unit,
    onCancelTicket: () -> Unit,
    onInsertCard: () -> Unit,
    onBack: () -> Unit
) {
    var selected by remember { mutableStateOf(UserMenuOption.SEARCH) }


        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("User Menu", style = MaterialTheme.typography.headlineMedium)

            OptionRow(
                label = "Search Ticket",
                selected = selected == UserMenuOption.SEARCH,
                onSelect = { selected = UserMenuOption.SEARCH }
            )
            OptionRow(
                label = "View Ticket",
                selected = selected == UserMenuOption.VIEW,
                onSelect = { selected = UserMenuOption.VIEW }
            )
            OptionRow(
                label = "Cancel Ticket",
                selected = selected == UserMenuOption.CANCEL,
                onSelect = { selected = UserMenuOption.CANCEL }
            )
            OptionRow(
                label = "InsertCard",
                selected = selected == UserMenuOption.INSERT,
                onSelect = { selected = UserMenuOption.INSERT }
            )

            Spacer(Modifier.height(8.dp))

            Row(

                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(onClick = onBack) {
                    Text("Back")
                }
                Button(
                    onClick = {
                        when (selected) {
                            UserMenuOption.SEARCH -> onSearchTicket()
                            UserMenuOption.VIEW -> onViewTicket()
                            UserMenuOption.CANCEL -> onCancelTicket()
                            UserMenuOption.INSERT -> onInsertCard()
                        }
                    }
                ) {
                    Text("Continue")
                }
            }
        }
    }


@Composable
private fun OptionRow(
    label: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
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