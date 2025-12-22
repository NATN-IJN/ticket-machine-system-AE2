package com.ticketmachine.database
import org.jetbrains.exposed.sql.Table

object AdminsTable : Table("admins") {

    val username = varchar("username", 50)
    val password = varchar("password", 100)


    override val primaryKey = PrimaryKey(username)
}