package com.ticketmachine.database

import org.jetbrains.exposed.sql.Table

object DestinationsTable : Table("destinations") {
    // Column names MUST match your SQLite table.
    val name = varchar("name", 255)
    val singlePrice = double("singlePrice")
    val returnPrice = double("returnPrice")
    val takings = double("takings")
    val salesCount = integer("salesCount")

    override val primaryKey = PrimaryKey(name)
}