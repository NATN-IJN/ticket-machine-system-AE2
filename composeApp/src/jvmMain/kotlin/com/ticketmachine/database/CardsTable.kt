package com.ticketmachine.database

import org.jetbrains.exposed.sql.Table

object CardsTable : Table("creditcards") {

    val id = integer("id").autoIncrement()
    val cardNumber = varchar("cardnumber", 16)
    val cvc = integer("cvc")
    val expiry = varchar("expiry", 4)
    val name = varchar("name", 50)
    val balance = double("balance")

    override val primaryKey = PrimaryKey(id)
}