package com.ticketmachine.service

import com.ticketmachine.database.DatabaseManager
import com.ticketmachine.domain.*

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
        return TODO("Provide the return value")
    }
    fun addDestination(){
        return TODO("Provide the return value")
    }
    fun updateDestinationPrices(name: String){
        return TODO("Provide the return value")
    }

    fun addSpecialOffer(name: String){
        return TODO("Provide the return value")
    }
    fun searchSpecialOfferId(id: String){
        return TODO("Provide the return value")
    }
    fun confirmDelete(){
        return TODO("Provide the return value")
    }
}