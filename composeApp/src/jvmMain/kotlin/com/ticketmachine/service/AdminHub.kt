package com.ticketmachine.service

import com.ticketmachine.database.DatabaseManager
import com.ticketmachine.domain.*

class AdminHub(
    private val database: DatabaseManager,
    private val ticketMachine: TicketMachine
) {
    private var currentAdmin: Admin? = null

    fun login(username: String, password: String): Boolean
    fun setCurrentAdmin(admin: Admin) { currentAdmin = admin }

    fun adminMenu()

    fun viewDestination(name: String): Destination?
    fun addDestination()
    fun updateDestinationPrices(name: String)

    fun addSpecialOffer(name: String)
    fun searchSpecialOfferId(id: String)
    fun confirmDelete()
}