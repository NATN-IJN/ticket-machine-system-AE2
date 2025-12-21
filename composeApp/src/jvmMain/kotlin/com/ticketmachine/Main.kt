package com.ticketmachine

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.ticketmachine.database.DatabaseManager
import com.ticketmachine.domain.TicketType
import com.ticketmachine.domain.User
import com.ticketmachine.service.TicketMachine
import com.ticketmachine.ui.App

fun main() = application {
    DatabaseManager.connect()

    val machine = TicketMachine(
        originStation = "Southampton",
        database = DatabaseManager
    )

// 1. Set current user + card
    machine.setCurrentUser(User("2nnadn68@solent.ac.uk"))
    machine.insertCard("4242424242424242")

// 2. Search (this populates lastSearchedX fields)
    val price = machine.searchTicket("North Bridge", TicketType.SINGLE)
    println("Calculated price: $price")

// 3. Buy ticket
    val ticket = machine.buyTicket()
    println("Ticket created: $ticket")

//    val card1 = DatabaseManager.getCard("4242424242424242")
//    println("Before: ${card1?.balance}")
//
//    card1?.deduct(10.0)
//    DatabaseManager.updateCard(card1!!)
//
//    val card2 = DatabaseManager.getCard("4242424242424242")
//    println("After: ${card2?.balance}")

//    val destinations = DatabaseManager.getAllDestinations()
//    println("Destinations loaded: ${destinations.size}")
//    println(destinations.take(5))



}