package com.ticketmachine.tests

import com.ticketmachine.database.*
import com.ticketmachine.domain.TicketStatus
import com.ticketmachine.domain.TicketType
import com.ticketmachine.domain.User
import com.ticketmachine.service.TicketMachine
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ViewTicketTest {

    private lateinit var ticketMachine: TicketMachine

    @BeforeAll
    fun setupAll() {
        DatabaseManager.disconnectForTests()
        DatabaseManager.connect("ticketmachine_test.db")
        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                DestinationsTable,
                CardsTable,
                TicketsTable,
                SpecialOffersTable,
                AdminsTable
            )
        }
        ticketMachine = TicketMachine(originStation = "Southampton", database = DatabaseManager)
    }

    @BeforeEach
    fun reset() {
        transaction {
            TicketsTable.deleteAll()
            CardsTable.deleteAll()
            SpecialOffersTable.deleteAll()
            DestinationsTable.deleteAll()
            AdminsTable.deleteAll()

            DestinationsTable.insert {
                it[name] = "North Bridge"
                it[singlePrice] = 2.50
                it[returnPrice] = 4.00
                it[takings] = 0.0
                it[salesCount] = 0
            }

            TicketsTable.insert {
                it[ticketRef] = "T-123"
                it[username] = "alice"
                it[cardNumber] = "4242424242424242"
                it[destinationName] = "North Bridge"
                it[type] = TicketType.SINGLE.name
                it[price] = 2.50
                it[status] = TicketStatus.ACTIVE.name
                it[purchaseDate] = "2025-01-01"
            }

            TicketsTable.insert {
                it[ticketRef] = "T-999"
                it[username] = "bob"
                it[cardNumber] = "5555555555554444"
                it[destinationName] = "North Bridge"
                it[type] = TicketType.RETURN.name
                it[price] = 4.00
                it[status] = TicketStatus.ACTIVE.name
                it[purchaseDate] = "2025-01-01"
            }
        }

        ticketMachine.setCurrentUser(User(username = "alice"))
    }

    @Test
    fun viewTicket_validOwnedTicket_returnsTicket() {
        val t = ticketMachine.viewTicket("T-123")
        assertNotNull(t)
    }

    @Test
    fun viewTicket_ticketNotOwned_returnsNull() {
        val t = ticketMachine.viewTicket("T-999")
        assertNull(t)
    }

    @Test
    fun viewTicket_unknownRef_returnsNull() {
        val t = ticketMachine.viewTicket("NOPE")
        assertNull(t)
    }
}