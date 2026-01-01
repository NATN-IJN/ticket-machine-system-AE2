package com.ticketmachine.tests

import com.ticketmachine.database.*
import com.ticketmachine.domain.OfferStatus
import com.ticketmachine.domain.TicketType
import com.ticketmachine.service.AdminHub
import com.ticketmachine.service.TicketMachine
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.time.LocalDate

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AddSpecialOfferTest {

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
        val ticketMachine = TicketMachine(
            originStation = "Southampton",
            database = DatabaseManager
        )

        adminHub = AdminHub(
            database = DatabaseManager,
            ticketMachine = ticketMachine
        )
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
        }
    }

    @Test
    fun addSpecialOffer_validOffer_persistsAndReturnsOffer() {
        val start = LocalDate.now().minusDays(1)
        val end = LocalDate.now().plusDays(5)

        val offer = adminHub.addSpecialOffer(
            destinationName = "North Bridge",
            ticketType = TicketType.SINGLE,
            discountFactor = 0.80,
            startDate = start,
            endDate = end
        )

        assertNotNull(offer)

        val row = transaction {
            SpecialOffersTable.selectAll().single()
        }

        assertEquals("North Bridge", row[SpecialOffersTable.destination])
        assertEquals(TicketType.SINGLE.name, row[SpecialOffersTable.ticketType])
        assertEquals(0.80, row[SpecialOffersTable.discountFactor], 0.0001)
        assertEquals(start.toString(), row[SpecialOffersTable.startDate])
        assertEquals(end.toString(), row[SpecialOffersTable.endDate])
        assertEquals(OfferStatus.ACTIVE.name, row[SpecialOffersTable.status])
    }

    @Test
    fun addSpecialOffer_unknownDestination_returnsNull() {
        val offer = adminHub.addSpecialOffer(
            destinationName = "Does Not Exist",
            ticketType = TicketType.SINGLE,
            discountFactor = 0.80,
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusDays(1)
        )
        assertNull(offer)
        val count = transaction { SpecialOffersTable.selectAll().count() }
        assertEquals(0, count)
    }

    @Test
    fun addSpecialOffer_endBeforeStart_returnsNull() {
        val offer = adminHub.addSpecialOffer(
            destinationName = "North Bridge",
            ticketType = TicketType.SINGLE,
            discountFactor = 0.80,
            startDate = LocalDate.now().plusDays(2),
            endDate = LocalDate.now().plusDays(1)
        )
        assertNull(offer)
        val count = transaction { SpecialOffersTable.selectAll().count() }
        assertEquals(0, count)
    }

    @Test
    fun addSpecialOffer_invalidDiscountFactor_returnsNull() {
        val offer = adminHub.addSpecialOffer(
            destinationName = "North Bridge",
            ticketType = TicketType.SINGLE,
            discountFactor = 1.50,
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusDays(1)
        )
        assertNull(offer)
        val count = transaction { SpecialOffersTable.selectAll().count() }
        assertEquals(0, count)
    }
}