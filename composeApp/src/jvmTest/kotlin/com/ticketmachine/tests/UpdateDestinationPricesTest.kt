package com.ticketmachine.tests

import com.ticketmachine.database.*
import com.ticketmachine.service.AdminHub
import com.ticketmachine.service.TicketMachine
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UpdateDestinationPricesTest {

    private lateinit var ticketMachine: TicketMachine
    private lateinit var adminHub: AdminHub

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
        adminHub = AdminHub(database = DatabaseManager, ticketMachine = ticketMachine)
    }

    @BeforeEach
    fun reset() {
        transaction {
            TicketsTable.deleteAll()
            CardsTable.deleteAll()
            SpecialOffersTable.deleteAll()
            AdminsTable.deleteAll()
            DestinationsTable.deleteAll()

            DestinationsTable.insert {
                it[name] = "North Bridge"
                it[singlePrice] = 2.50
                it[returnPrice] = 4.00
                it[takings] = 0.0
                it[salesCount] = 0
            }
        }
    }

    @Test
    fun updateDestinationPrices_validNewPrices_updatesAndReturnsDestination() {
        val d = adminHub.viewDestination("North Bridge")
        assertNotNull(d)

        val updated = adminHub.updateDestinationPrices(d!!, newSingle = 3.00, newReturn = 5.50)
        assertNotNull(updated)
        assertEquals(3.00, updated!!.singlePrice, 0.0001)
        assertEquals(5.50, updated.returnPrice, 0.0001)

        val reread = adminHub.viewDestination("North Bridge")
        assertNotNull(reread)
        assertEquals(3.00, reread!!.singlePrice, 0.0001)
        assertEquals(5.50, reread.returnPrice, 0.0001)
    }

    @Test
    fun updateDestinationPrices_invalidPrice_returnsNull_andDoesNotChangeDb() {
        val d = adminHub.viewDestination("North Bridge")
        assertNotNull(d)

        val updated = adminHub.updateDestinationPrices(d!!, newSingle = 0.0, newReturn = 5.50)
        assertNull(updated)

        val reread = adminHub.viewDestination("North Bridge")
        assertNotNull(reread)
        assertEquals(2.50, reread!!.singlePrice, 0.0001)
        assertEquals(4.00, reread.returnPrice, 0.0001)
    }
}