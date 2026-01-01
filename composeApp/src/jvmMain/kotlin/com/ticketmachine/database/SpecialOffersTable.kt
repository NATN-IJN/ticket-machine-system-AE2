package com.ticketmachine.database
import org.jetbrains.exposed.sql.Table

object SpecialOffersTable : Table("specialOffers") {
    val offerId = integer("offerId").autoIncrement()
    val destination = varchar("destination", 50)
    val ticketType = varchar("ticketType", 10)
    val discountFactor = double("discountFactor")
    val startDate = varchar("startDate", 10)
    val endDate = varchar("endDate", 10)
    val status = varchar("status", 10)

    override val primaryKey = PrimaryKey(offerId)
}