package com.ticketmachine.database

import androidx.compose.ui.unit.IntRect
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

object SpecialOffersTable: IntIdTable(
    name = "specialOffers",
    columnName = "offerId"
) {
    val destination = varchar("destination", 10)
    val ticketType = varchar("ticketType", 10)
    val discountFactor = double("discountFactor")
    val discount = varchar("discount", 10)
    val startDate = varchar("startDate", 10)
    val endDate = varchar("endDate", 10)
    val status = varchar("status", 10)
}