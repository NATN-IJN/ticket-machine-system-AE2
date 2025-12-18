package com.ticketmachine.service

import com.ticketmachine.database.DatabaseManager
import com.ticketmachine.domain.*
import java.time.LocalDate
class TicketMachine(
    private val originStation: String,
    private val database: DatabaseManager
) {
    private var lastSearchedDestination: Destination? = null
    private var lastSearchedType: TicketType? = null
    private var lastCalculatedPrice: Double? = null
    private var lastAppliedOffer: SpecialOffer? = null
    private var currentUser: User? = null
    private var currentCard: Card? = null

    fun searchTicket(destName: String, type: TicketType): Double?{
        val dest = database.findDestination(destName) ?: return null

        val basePrice =
            if (type == TicketType.SINGLE) dest.singlePrice
            else dest.returnPrice

        val offer = database.findActiveOffer(destination = dest, type = type, onDate = LocalDate.now())
        val finalPrice = calculateFinalPrice(basePrice, offer)

        lastSearchedDestination = dest
        lastSearchedType = type
        lastCalculatedPrice = finalPrice
        lastAppliedOffer = offer

        return finalPrice}

    fun calculateBasePrice(dest: Destination, type: TicketType): Double{
        return TODO("Provide the return value")
    }
    fun calculateFinalPrice(basePrice: Double, offer: SpecialOffer?): Double{
        return TODO("Provide the return value")
    }
    fun buyTicket(): Ticket? {
        val user = currentUser ?: return null
        val card = currentCard ?: return null
        val dest = lastSearchedDestination ?: return null
        val type = lastSearchedType ?: return null
        val price = lastCalculatedPrice ?: return null

        val charge = database.chargeCard(card, price)
        if (!charge) return null

        card.deduct(price)
        database.updateCard(card)

        return database.createTicket(
            destination = dest,
            type = type,
            price = price,
            username = user.username,
            cardNumber = card.cardNumber,
            origin = originStation,
        )
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