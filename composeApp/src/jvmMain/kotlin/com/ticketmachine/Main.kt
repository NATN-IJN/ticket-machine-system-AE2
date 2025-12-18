package com.ticketmachine

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.ticketmachine.database.DatabaseManager
import com.ticketmachine.ui.App

fun main() = application {
    DatabaseManager.connect()

    val card1 = DatabaseManager.getCard("4242424242424242")
    println("Before: ${card1?.balance}")

    card1?.deduct(10.0)
    DatabaseManager.updateCard(card1!!)

    val card2 = DatabaseManager.getCard("4242424242424242")
    println("After: ${card2?.balance}")

}