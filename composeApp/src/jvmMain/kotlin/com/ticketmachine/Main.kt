package com.ticketmachine

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.ticketmachine.database.DatabaseManager
import com.ticketmachine.ui.App

fun main() = application {
    DatabaseManager.connect()
    Window(onCloseRequest = ::exitApplication, title = "Ticket Machine") {
        App()
    }
}