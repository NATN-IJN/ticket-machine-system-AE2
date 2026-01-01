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
class ChangeAllTicketPricesTest {

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
    fun changeAllTicketPrices_validDiscountFactor_updatesAllDestinations() {
        val ok = adminHub.changeAllTicketPrices(factor = 1.10)
        assertNotNull(ok)

        val d1 = adminHub.viewDestination("North Bridge")
        assertNotNull(d1)
        assertEquals(2.75, d1!!.singlePrice, 0.0001)
        assertEquals(4.40, d1.returnPrice, 0.0001)
    }

    @Test
    fun changeAllTicketPrices_invalidFactor_returnsFalse_andDoesNotChange() {
        val ok = adminHub.changeAllTicketPrices(factor = 0.0)
        assertNull(ok)

        val d1 = adminHub.viewDestination("North Bridge")
        assertNotNull(d1)
        assertEquals(2.50, d1!!.singlePrice, 0.0001)
        assertEquals(4.00, d1.returnPrice, 0.0001)

    }
}