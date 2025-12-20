package com.ticketmachine.database

import org.jetbrains.exposed.sql.Table

object SpecialOffersTable: Table("specialOffers") {
    val offerId = varchar("offerId", 4)
    val destination = varchar("destination", 10)
    val ticketType = varchar("ticketType", 10)
    val discountFactor = double("discountFactor")
    val discount = varchar("discount", 10)
    val startDate = varchar("startDate", 10)
    val endDate = varchar("endDate", 10)
    val status = varchar("status", 10)
    override val primaryKey = PrimaryKey(offerId)
}