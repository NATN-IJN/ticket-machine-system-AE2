package com.ticketmachine.domain

data class Admin(
    val username: String,
    private val password: String
) {

    fun checkLogin(password: String): Boolean {
        return this.password == password
    }
}