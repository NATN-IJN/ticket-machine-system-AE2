package com.ticketmachine.service

import com.ticketmachine.database.DatabaseManager
import com.ticketmachine.domain.*
import java.time.LocalDateTime

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

        val offer = database.findActiveOffer(destination = dest, type = type, Ondate = LocalDateTime.now())
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
    fun buyTicket(){
        val user = currentUser ?: error("No user selected")
        val card = currentCard ?: error("No card inserted")

        val dest = lastSearchedDestination ?: error("No ticket searched")
        val type = lastSearchedType ?: error("No ticket searched")
        val price = lastCalculatedPrice ?: error("No ticket searched")

        val success = database.chargeCard(card, price)
        if (!success) error("Card declined")

        database.createTicket(
            dest = dest,
            type = type,
            price = price,
            username = user.username,
            cardNumber = card.cardNumber
        )

        lastSearchedDestination = null
        lastSearchedType = null
        lastCalculatedPrice = null
        lastAppliedOffer = null
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