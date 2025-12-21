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
    var screen by remember { mutableStateOf<Screen>(Screen.MainMenu) }

    when (val s = screen) {

        Screen.MainMenu -> MainMenuScreen(
            onSelectUser = {screen = Screen.SelectUser},
            onSelectAdmin = { /* TODO later */ })

        Screen.SelectUser -> SelectUserScreen(
            onBack = { screen = Screen.MainMenu },
            onContinue = { user ->
                ticketMachine.setCurrentUser(user)
                screen = Screen.UserMenu
            }
        )

        Screen.UserMenu -> UserMenuScreen(
            onSearchTicket = {screen = Screen.SearchTicket},
            onViewTicket = { /* TODO later */ },
            onCancelTicket = { /* TODO later */ },
            onInsertCard =  {screen = Screen.InsertCard},
            onBack = { screen = Screen.SelectUser })

        Screen.InsertCard -> InsertCardScreen(
            onBack = { screen = Screen.SelectUser },
            onContinue = { cardNumber ->
                val card = ticketMachine.insertCard(cardNumber)
                if (card != null) screen = Screen.SearchTicket
            }
        )

        Screen.SearchTicket -> SearchTicketScreen(
            onBack = { screen = Screen.UserMenu },
            ticketMachine = ticketMachine,
            onPurchased = { ticket ->
                screen = Screen.Confirmation(ticket)
            }
        )

        is Screen.Confirmation -> ConfirmationScreen(
            ticket = s.ticket,
            onDone = { screen = Screen.UserMenu }
        )
    }
}