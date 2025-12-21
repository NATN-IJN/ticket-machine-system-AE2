package com.ticketmachine

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.ticketmachine.database.DatabaseManager
import com.ticketmachine.domain.TicketType
import com.ticketmachine.domain.User
import com.ticketmachine.service.TicketMachine
import com.ticketmachine.ui.App

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Ticket Machine") {
        App()
    }
}