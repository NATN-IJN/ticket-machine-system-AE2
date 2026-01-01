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
class AddDestinationTest {

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

            AdminsTable.deleteAll()
            AdminsTable.insert {
                it[username] = "Nathan"
                it[password] = "pass"
            }
        }

        ticketMachine = TicketMachine(originStation = "Southampton", database = DatabaseManager)
        adminHub = AdminHub(database = DatabaseManager, ticketMachine = ticketMachine)

        val ok = adminHub.login("Nathan", "pass")
        assertTrue(ok)
    }

    @BeforeEach
    fun reset() {
        transaction {
            DestinationsTable.deleteAll()
        }
    }

    @Test
    fun addDestination_newDestination_persistsAndReturnsDestination() {
        val created: Destination? = adminHub.addDestination(
            name = "West Quay",
            single = 2.75,
            returnP = 4.50
        )

        assertNotNull(created)
        assertEquals("West Quay", created!!.name)
        assertEquals(2.75, created.singlePrice, 0.0001)
        assertEquals(4.50, created.returnPrice, 0.0001)

        val fetched = adminHub.viewDestination("West Quay")
        assertNotNull(fetched)
        assertEquals("West Quay", fetched!!.name)
    }

    @Test
    fun addDestination_duplicateName_returnsNull() {
        val first = adminHub.addDestination(
            name = "West Quay",
            single = 2.75,
            returnP = 4.50
        )
        assertNotNull(first)

        val second = adminHub.addDestination(
            name = "West Quay",
            single = 3.00,
            returnP = 5.00
        )
        assertNull(second)
    }

    @Test
    fun nameIsEmpty_returnsNull() {
        val empty = adminHub.addDestination(
            name = "",
            single = 2.75,
            returnP = 4.50
        )
        assertNull(empty)
    }
}
