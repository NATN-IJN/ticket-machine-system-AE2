package com.ticketmachine.tests

import com.ticketmachine.database.*
import com.ticketmachine.domain.TicketType
import com.ticketmachine.domain.User
import com.ticketmachine.service.TicketMachine
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BuyTicketTest {

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
            SpecialOffersTable.deleteAll()
            DestinationsTable.deleteAll()
            CardsTable.deleteAll()
            AdminsTable.deleteAll()

            DestinationsTable.insert {
                it[name] = "North Bridge"
                it[singlePrice] = 2.50
                it[returnPrice] = 4.00
                it[takings] = 0.0
                it[salesCount] = 0
            }

            CardsTable.insert {
                it[cardNumber] = "4242424242424242"
                it[cvc] = 123
                it[expiry] = "12/27"
                it[name] = "Test User"
                it[balance] = 100.0
            }
        }
    }

    @Test
    fun buyTicket_afterInsertCardAndSearch_returnsTicket_andPersistsIt() {
        ticketMachine.setCurrentUser(User(username = "Nathan"))

        val card = ticketMachine.insertCard(" 4242424242424242 ")
        assertNotNull(card)

        val price = ticketMachine.searchTicket(destName = "North Bridge", type = TicketType.SINGLE)
        assertNotNull(price)

        val ticket = ticketMachine.buyTicket()
        assertNotNull(ticket)

        val count = transaction { TicketsTable.selectAll().count() }
        assertEquals(1, count)
    }

    @Test
    fun buyTicket_withoutCard_returnsNull() {
        ticketMachine.setCurrentUser(User(username = "Test User"))
        val price = ticketMachine.searchTicket(destName = "North Bridge", type = TicketType.SINGLE)
        assertNotNull(price)

        val ticket = ticketMachine.buyTicket()
        assertNull(ticket)
    }

    @Test
    fun buyTicket_insufficientBalance_returnsNull_andDoesNotPersist() {
        ticketMachine.setCurrentUser(User(username = "Test User"))

        val cardNumber = "4000000000000002"
        transaction {
            CardsTable.insert {
                it[this.cardNumber] = cardNumber
                it[cvc] = 123
                it[expiry] = "12/27"
                it[name] = "Test User"
                it[balance] = 1.00
            }
        }

        val card = ticketMachine.insertCard(cardNumber)
        assertNotNull(card)

        val price = ticketMachine.searchTicket("North Bridge", TicketType.SINGLE)
        assertNotNull(price)

        val ticket = ticketMachine.buyTicket()
        assertNull(ticket)

        val count = transaction { TicketsTable.selectAll().count() }
        assertEquals(0, count)
    }
}

