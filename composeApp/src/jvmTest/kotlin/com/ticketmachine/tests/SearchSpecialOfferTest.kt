package com.ticketmachine.tests

import com.ticketmachine.database.*
import com.ticketmachine.domain.OfferStatus
import com.ticketmachine.domain.TicketType
import com.ticketmachine.service.AdminHub
import com.ticketmachine.service.TicketMachine
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.time.LocalDate

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UC_SearchSpecialOfferTest {

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
            DestinationsTable.deleteAll()
            AdminsTable.deleteAll()

            AdminsTable.insert {
                it[username] = "admin"
                it[password] = "pass"
            }

            DestinationsTable.insert {
                it[name] = "North Bridge"
                it[singlePrice] = 2.50
                it[returnPrice] = 4.00
                it[takings] = 0.0
                it[salesCount] = 0
            }
        }

        adminHub.login("admin", "pass")
    }

    @Test
    fun searchSpecialOffer_existingId_returnsOffer() {
        val start = LocalDate.now().minusDays(1).toString()
        val end = LocalDate.now().plusDays(1).toString()

        val id = transaction {
            SpecialOffersTable.insert {
                it[destination] = "North Bridge"
                it[ticketType] = TicketType.SINGLE.name
                it[discountFactor] = 0.80
                it[startDate] = start
                it[endDate] = end
                it[status] = OfferStatus.ACTIVE.name
            } get SpecialOffersTable.offerId
        }

        val offer = adminHub.searchSpecialOfferId(id)
        assertNotNull(offer)
        assertEquals(id, offer!!.offerId)
    }

    @Test
    fun searchSpecialOffer_unknownId_returnsNull() {
        val offer = adminHub.searchSpecialOfferId(999999)
        assertNull(offer)
    }

    @Test
    fun searchSpecialOffer_notLoggedIn_returnsNull() {
        adminHub.logout()
        val offer = adminHub.searchSpecialOfferId(1)
        assertNull(offer)
    }
}