package com.ticketmachine.database
import org.jetbrains.exposed.sql.Table
object TicketsTable : Table("tickets") {
    val ticketRef = varchar("ticketRef", 8)
    val username = varchar("username", 50)
    val cardNumber = varchar("cardNumber", 16)
    val destinationName = varchar("destinationName", 50)
    val type = varchar("type", 6)
    val price = double("price")
    val status = varchar("status", 10)
    val purchaseDate = varchar("purchaseDate", 20)

    override val primaryKey = PrimaryKey(ticketRef)
}