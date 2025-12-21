package com.ticketmachine.domain

import java.time.LocalDate

data class SpecialOffer(
    val offerId: Int,
    val destination: Destination,
    val ticketType: TicketType,
    val discountFactor: Double,          // e.g. 0.80 means 20% off
    val startDate: LocalDate,
    val endDate: LocalDate,
    var status: OfferStatus = OfferStatus.ACTIVE
) {

    fun isActiveOn(date: LocalDate): Boolean {
        if (status != OfferStatus.ACTIVE) return false
        return !date.isBefore(startDate) && !date.isAfter(endDate)
    }

    fun applyTo(basePrice: Double): Double {
        return basePrice * discountFactor
    }
}