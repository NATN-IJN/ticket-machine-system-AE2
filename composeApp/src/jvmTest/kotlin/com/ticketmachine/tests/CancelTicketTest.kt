package com.ticketmachine.tests

import com.ticketmachine.database.*
import com.ticketmachine.domain.TicketStatus
import com.ticketmachine.domain.TicketType
import com.ticketmachine.domain.User
import com.ticketmachine.service.TicketMachine
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CancelTicketTest {

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
                it[takings] = 5.00
                it[salesCount] = 2
            }

            CardsTable.insert {
                it[cardNumber] = "4242424242424242"
                it[cvc] = 123
                it[expiry] = "12/27"
                it[name] = "Alice"
                it[balance] = 97.50
            }

            CardsTable.insert {
                it[cardNumber] = "5555555555554444"
                it[cvc] = 456
                it[expiry] = "12/27"
                it[name] = "Bob"
                it[balance] = 97.50
            }

            TicketsTable.insert {
                it[ticketRef] = "T-ACT"
                it[username] = "alice"
                it[cardNumber] = "4242424242424242"
                it[destinationName] = "North Bridge"
                it[type] = TicketType.SINGLE.name
                it[price] = 2.50
                it[status] = TicketStatus.ACTIVE.name
                it[purchaseDate] = "2025-01-01"
            }

            TicketsTable.insert {
                it[ticketRef] = "T-BOB"
                it[username] = "bob"
                it[cardNumber] = "5555555555554444"
                it[destinationName] = "North Bridge"
                it[type] = TicketType.SINGLE.name
                it[price] = 2.50
                it[status] = TicketStatus.ACTIVE.name
                it[purchaseDate] = "2025-01-01"
            }
        }

        ticketMachine.setCurrentUser(User("alice"))
        ticketMachine.insertCard("4242424242424242")
    }

    @Test
    fun cancelTicket_activeOwnedTicket_updatesStatusToCancelled() {
        val t = ticketMachine.cancelTicket("T-ACT")
        assertNotNull(t)

        val dbStatus = transaction {
            TicketsTable
                .select(TicketsTable.status)
                .where { TicketsTable.ticketRef eq "T-ACT" }
                .single()[TicketsTable.status]
        }

        assertEquals(TicketStatus.CANCELLED.name, dbStatus)
    }

    @Test
    fun cancelTicket_RefundsCard() {
        val beforeCardBalance = transaction {
            CardsTable
                .select(CardsTable.balance)
                .where { CardsTable.cardNumber eq "4242424242424242" }
                .single()[CardsTable.balance]
        }

        val t = ticketMachine.cancelTicket("T-ACT")
        assertNotNull(t)

        val afterCardBalance = transaction {
            CardsTable
                .select(CardsTable.balance)
                .where { CardsTable.cardNumber eq "4242424242424242" }
                .single()[CardsTable.balance]
        }
        assertEquals(beforeCardBalance + 2.50, afterCardBalance, 0.0001)

    }

    @Test
    fun cancelTicket_decrementsDestinationTakingsAndSalesCount() {
        val beforeDest = transaction {
            DestinationsTable
                .select(DestinationsTable.takings, DestinationsTable.salesCount)
                .where { DestinationsTable.name eq "North Bridge" }
                .single()
        }

        val beforeTakings = beforeDest[DestinationsTable.takings]
        val beforeSales = beforeDest[DestinationsTable.salesCount]

        val t = ticketMachine.cancelTicket("T-ACT")
        assertNotNull(t)

        val afterDest = transaction {
            DestinationsTable
                .select(DestinationsTable.takings, DestinationsTable.salesCount)
                .where { DestinationsTable.name eq "North Bridge" }
                .single()
        }

        val afterTakings = afterDest[DestinationsTable.takings]
        val afterSales = afterDest[DestinationsTable.salesCount]

        assertEquals(beforeTakings - 2.50, afterTakings, 0.0001)
        assertEquals(beforeSales - 1, afterSales)
    }

    @Test
    fun cancelTicket_notOwned_returnsNull() {
        val t = ticketMachine.cancelTicket("T-BOB")
        assertNull(t)
    }

    @Test
    fun cancelTicket_unknownRef_returnsNull() {
        val t = ticketMachine.cancelTicket("NOPE")
        assertNull(t)
    }
}