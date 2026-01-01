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
class DeleteSpecialOfferTest {

    private lateinit var adminHub: AdminHub
    private lateinit var ticketMachine: TicketMachine

    private var offerId: Int = -1

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

            DestinationsTable.insert {
                it[name] = "North Bridge"
                it[singlePrice] = 2.50
                it[returnPrice] = 4.00
                it[takings] = 0.0
                it[salesCount] = 0
            }

            val row = SpecialOffersTable.insert {
                it[destination] = "North Bridge"
                it[ticketType] = TicketType.SINGLE.name
                it[discountFactor] = 0.80
                it[startDate] = LocalDate.now().minusDays(1).toString()
                it[endDate] = LocalDate.now().plusDays(1).toString()
                it[status] = OfferStatus.ACTIVE.name
            }

            offerId = row[SpecialOffersTable.offerId]
        }
    }

    @Test
    fun deleteOffer_existingActiveOffer_marksCancelledInDb_andReturnsCancelledOffer() {
        val result = adminHub.deleteOffer(offerId)

        assertNotNull(result)
        assertEquals(offerId, result!!.offerId)
        assertEquals(OfferStatus.CANCELLED, result.status)

        val statusInDb = transaction {
            SpecialOffersTable
                .select(SpecialOffersTable.status)
                .where { SpecialOffersTable.offerId eq offerId }
                .single()[SpecialOffersTable.status]
        }
        assertEquals(OfferStatus.CANCELLED.name, statusInDb)
    }

    @Test
    fun deleteOffer_unknownId_returnsNull() {
        val result = adminHub.deleteOffer(999999)
        assertNull(result)
    }


}