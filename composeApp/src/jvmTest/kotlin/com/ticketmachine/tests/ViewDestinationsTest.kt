package com.ticketmachine.tests

import com.ticketmachine.database.*
import com.ticketmachine.domain.Destination
import com.ticketmachine.service.AdminHub
import com.ticketmachine.service.TicketMachine
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ViewDestinationTest {

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
            DestinationsTable.deleteAll()

            DestinationsTable.insert {
                it[name] = "London"
                it[singlePrice] = 3.00
                it[returnPrice] = 5.00
                it[takings] = 0.0
                it[salesCount] = 0
            }
        }
    }

    @Test
    fun viewDestination_existingDestination_returnsDestination() {
        val dest: Destination? = adminHub.viewDestination("London")

        assertNotNull(dest)
        assertEquals("London", dest!!.name)
        assertEquals(3.00, dest.singlePrice, 0.0001)
        assertEquals(5.00, dest.returnPrice, 0.0001)
    }

    @Test
    fun viewDestination_unknownDestination_returnsNull() {
        val dest = adminHub.viewDestination("Does Not Exist")
        assertNull(dest)
    }
}