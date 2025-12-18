package com.ticketmachine.service

import com.ticketmachine.database.DatabaseManager
import com.ticketmachine.domain.*

class TicketMachine(
    private val originStation: String,
    private val database: DatabaseManager
) {
    private var currentUser: User? = null
    private var currentCard: Card? = null

    fun searchTicket(destName: String, type: TicketType): Double?
    fun calculateBasePrice(dest: Destination, type: TicketType): Double
    fun calculateFinalPrice(basePrice: Double, offer: SpecialOffer?): Double
    fun buyTicket()

    fun getCurrentCard(): Card? = currentCard
    fun insertCard(cardNumber: String): Card?
    fun setCard(card: Card) { currentCard = card }

    fun updateTicket(ticketRef: String)
    fun setCurrentUser(user: User) { currentUser = user }

    fun viewTicket(ticketRef: String): Ticket?
    fun cancelTicket(ticketRef: String)

    fun changeAllTicketPrices(percent: Double): Double
}