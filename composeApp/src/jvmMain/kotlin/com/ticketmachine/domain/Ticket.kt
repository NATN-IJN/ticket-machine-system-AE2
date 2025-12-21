package com.ticketmachine.domain

data class Ticket(
    val ticketRef: String,
    val origin: String,
    val destination: Destination,
    val price: Double,
    val type: TicketType,
    var status: TicketStatus = TicketStatus.ACTIVE
) {
    fun cancel() {
        status = TicketStatus.CANCELLED
    }
}