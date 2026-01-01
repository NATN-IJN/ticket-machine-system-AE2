package com.ticketmachine.tests

import com.ticketmachine.database.*
import com.ticketmachine.service.TicketMachine
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InsertCardTest {

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
        }
    }

    @Test
    fun insertCard_validCardNumber_returnsCard() {
        transaction {
            CardsTable.insert {
                it[cardNumber] = "4242424242424242"
                it[cvc] = 123
                it[expiry] = "12/27"
                it[name] = "Test User"
                it[balance] = 100.0
            }
        }

        val card = ticketMachine.insertCard("4242424242424242")

        assertNotNull(card)
        assertEquals("4242424242424242", card!!.cardNumber)
        assertEquals(100.0, card.balance, 0.0001)
    }

    @Test
    fun insertCard_invalidCardNumber_returnsNull() {
        val card = ticketMachine.insertCard("0000000000000000")
        assertNull(card)
    }

    @Test
    fun insertCard_trimsSpaces_stillFindsCard() {
        transaction {
            CardsTable.insert {
                it[cardNumber] = "4242424242424242"
                it[cvc] = 123
                it[expiry] = "12/27"
                it[name] = "Test User"
                it[balance] = 100.0
            }
        }

        val card = ticketMachine.insertCard("  4242424242424242  ")

        assertNotNull(card)
        assertEquals("4242424242424242", card!!.cardNumber)
        assertEquals(100.0, card.balance, 0.0001)
    }
}