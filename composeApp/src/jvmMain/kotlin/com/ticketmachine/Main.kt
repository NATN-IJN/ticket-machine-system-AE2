package com.ticketmachine

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.ticketmachine.ui.App

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Ticket Machine") {
        App()
    }
}