package com.ticketmachine.ui.navigation

import com.ticketmachine.domain.Ticket

sealed class Screen {
    data object MainMenu : Screen()
    data object UserMenu : Screen()
    data object SelectUser : Screen()
    data object InsertCard : Screen()
    data object SearchTicket : Screen()
    data class Confirmation(val ticket: Ticket) : Screen()
}