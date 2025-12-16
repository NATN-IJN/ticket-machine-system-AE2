package com.ticketmachine.domain

class Card(
    val cardNumber: String,
    var balance: Double,
    val owner: User?
) {

    fun deduct(price: Double) {
        balance -= price
    }

    fun refund(amount: Double) {
        balance += amount
    }
}