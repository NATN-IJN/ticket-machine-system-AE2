package com.ticketmachine.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.ticketmachine.database.DatabaseManager
import com.ticketmachine.service.TicketMachine
import com.ticketmachine.ui.navigation.Screen
import com.ticketmachine.ui.screens.*


@Composable
fun App() {
    LaunchedEffect(Unit) { DatabaseManager.connect() }

    val ticketMachine = remember { TicketMachine(originStation = "Southampton", database = DatabaseManager ) }
    var screen by remember { mutableStateOf<Screen>(Screen.SelectUser) }

    when (val s = screen) {
        Screen.SelectUser -> SelectUserScreen(
            onContinue = { user ->
                ticketMachine.setCurrentUser(user)
                screen = Screen.InsertCard
            }
        )

        Screen.InsertCard -> InsertCardScreen(
            onBack = { screen = Screen.SelectUser },
            onContinue = { cardNumber ->
                val card = ticketMachine.insertCard(cardNumber)
                if (card != null) screen = Screen.SearchTicket
            }
        )

        Screen.SearchTicket -> SearchTicketScreen(
            onBack = { screen = Screen.InsertCard },
            ticketMachine = ticketMachine,
            onPurchased = { ticket ->
                screen = Screen.Confirmation(ticket)
            }
        )

        is Screen.Confirmation -> ConfirmationScreen(
            ticket = s.ticket,
            onDone = { screen = Screen.SearchTicket }
        )
    }
}