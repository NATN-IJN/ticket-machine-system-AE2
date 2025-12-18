package com.ticketmachine.service

import com.ticketmachine.database.DatabaseManager
import com.ticketmachine.domain.*

class TicketMachine(
    private val originStation: String,
    private val database: DatabaseManager
) {
    private var lastSearchedDestination: Destination? = null
    private var lastSearchedType: TicketType? = null
    private var lastCalculatedPrice: Double? = null
    private var lastAppliedOffer: SpecialOffer? = null   // optional, useful later
    private var currentUser: User? = null
    private var currentCard: Card? = null

    fun searchTicket(destName: String, type: TicketType): Double?{
        return TODO("Provide the return value")
    }
    fun calculateBasePrice(dest: Destination, type: TicketType): Double{
        return TODO("Provide the return value")
    }
    fun calculateFinalPrice(basePrice: Double, offer: SpecialOffer?): Double{
        return TODO("Provide the return value")
    }
    fun buyTicket(){
        return TODO("Provide the return value")
    }

    fun getCurrentCard(): Card? = currentCard
    fun insertCard(cardNumber: String): Card?{
        val card = database.getCard(cardNumber) ?: return null
        currentCard = card
        return card
    }

    fun updateTicket(ticketRef: String, status: TicketStatus){
        return TODO("Provide the return value")
    }
    fun setCurrentUser(user: User) { currentUser = user }

    fun viewTicket(ticketRef: String): Ticket?{
        return TODO("Provide the return value")
    }
    fun cancelTicket(ticketRef: String){
        return TODO("Provide the return value")
    }

    fun changeAllTicketPrices(percent: Double): Double{
        return TODO("Provide the return value")
    }
}