package com.ticketmachine.domain

class Ticket(
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