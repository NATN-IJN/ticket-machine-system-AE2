package com.ticketmachine.service

import com.ticketmachine.database.DatabaseManager
import com.ticketmachine.domain.*
import java.time.LocalDate

class AdminHub(
    private val database: DatabaseManager,
    private val ticketMachine: TicketMachine
) {
    private var currentAdmin: Admin? = null

    fun login(username: String, password: String): Boolean{
        val admin = database.getAdmin(username) ?: return false
        val ok = admin.checkLogin(password)
        if (ok) currentAdmin = admin
        return ok
    }
    fun setCurrentAdmin(admin: Admin) { currentAdmin = admin }

    fun adminMenu() {

    }

    fun viewDestination(name: String): Destination?{
        return database.findDestination(name)
    }

    fun addDestination(name: String, single: Double, returnP: Double): Destination? {
        val admin = currentAdmin ?: return null

        val trimmed = name.trim()
        if (trimmed.isEmpty()) return null
        if (single <= 0.0 || returnP <= 0.0) return null

        val existing = database.findDestination(trimmed)
        if (existing != null) return null
        return database.createDestination(trimmed, single, returnP)
    }
    fun updateDestinationPrices(destination: Destination, newSingle: Double, newReturn: Double): Destination? {
        if (newSingle <= 0.0 || newReturn <= 0.0) return null
        destination.setPrices(newSingle = newSingle, newReturn = newReturn)
        val ok = database.EditPrices(
            name = destination.name,
            newSingle = destination.singlePrice,
            newReturn = destination.returnPrice
        )
        if (!ok) return null
        return destination
    }

    fun addSpecialOffer(
        destinationName: String,
        ticketType: TicketType,
        discountFactor: Double,
        startDate: LocalDate,
        endDate: LocalDate
        ): SpecialOffer? {

        val admin = currentAdmin ?: return null
        val dest = database.findDestination(destinationName.trim()) ?: return null
        return database.saveSpecialOffer(
            destination = dest,
            ticketType = ticketType,
            discount = discountFactor,
            startDate = startDate,
            endDate = endDate
        )
    }
    fun searchSpecialOfferId(id: Int): SpecialOffer? {
        val admin = currentAdmin ?: return null
        return database.getSpecialOffer(id)
    }
    fun DeleteOffer(id: Int) : SpecialOffer? {
        val admin = currentAdmin ?: return null
        val offer = database.getSpecialOffer(id) ?: return null
        val ok = database.deleteSpecialOffer(id)
        if (!ok) return null

        return offer.copy(status = OfferStatus.CANCELLED)
    }

    fun changeAllTicketPrices(percent: Double): List<Destination>? {
        val admin = currentAdmin ?: return null
        if (percent <= 0.0) return null

        val destinations = database.getAllDestinations().toMutableList()
        if (destinations.isEmpty()) return emptyList()

        destinations.forEach { it.adjustPrices(percent) }

        database.saveAllDestinations(destinations)

        return destinations
    }
}