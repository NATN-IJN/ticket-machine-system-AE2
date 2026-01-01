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
class AdminLoginTest {

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
                it[username] = "Nathan"
                it[password] = "534"
            }
        }
    }

    @Test
    fun login_validCredentials_returnsTrue() {
        val ok = adminHub.login("Nathan", "534")
        assertTrue(ok)
    }

    @Test
    fun login_wrongPassword_returnsFalse() {
        val ok = adminHub.login("Nathan", "wrong")
        assertFalse(ok)
    }

    @Test
    fun login_unknownUser_returnsFalse() {
        val ok = adminHub.login("nope", "pass1")
        assertFalse(ok)
    }
}