package com.ticketmachine.tests

import com.ticketmachine.database.*
import com.ticketmachine.domain.OfferStatus
import com.ticketmachine.domain.TicketType
import com.ticketmachine.service.TicketMachine
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import java.time.LocalDate
import org.junit.jupiter.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SearchTicketTest {

    private lateinit var ticketMachine: TicketMachine

    @BeforeAll
    fun setupAll() {
//        DatabaseManager.disconnectForTests()
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
        }
    }

    @Test
    fun searchSingle_noOffer_returnsSinglePrice() {
        val price = ticketMachine.searchTicket(destName = "North Bridge", type = TicketType.SINGLE)
        assertNotNull(price)
        assertEquals(2.50, price!!, 0.0001)
    }

    @Test
    fun searchReturn_noOffer_returnsReturnPrice() {
        val price = ticketMachine.searchTicket(destName = "North Bridge", type = TicketType.RETURN)
        assertNotNull(price)
        assertEquals(4.00, price!!, 0.0001)
    }

    @Test
    fun searchUnknownDestination_returnsNull() {
        val price = ticketMachine.searchTicket(destName = "Does Not Exist", type = TicketType.SINGLE)
        assertNull(price)
    }

    @Test
    fun searchSingle_activeOffer_appliesDiscountFactor() {
        val start = LocalDate.now().minusDays(1).toString()
        val end = LocalDate.now().plusDays(1).toString()

        transaction {
            SpecialOffersTable.insert {
                it[destination] = "North Bridge"
                it[ticketType] = TicketType.SINGLE.name
                it[discountFactor] = 0.80
                it[startDate] = start
                it[endDate] = end
                it[status] = OfferStatus.ACTIVE.name
            }
        }

        val price = ticketMachine.searchTicket(destName = "North Bridge", type = TicketType.SINGLE)
        assertNotNull(price)
        assertEquals(2.00, price!!, 0.0001)
    }
}