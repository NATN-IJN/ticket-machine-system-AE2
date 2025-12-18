package com.ticketmachine.database

import com.ticketmachine.domain.Admin
import com.ticketmachine.domain.SpecialOffer
import com.ticketmachine.domain.Ticket
import com.ticketmachine.domain.TicketType
import com.ticketmachine.domain.User
import java.time.LocalDate
import com.ticketmachine.domain.Card
import com.ticketmachine.domain.Destination
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.outputStream

object DatabaseManager {

    private var db: Database? = null

    /**
     * Connects Exposed to a writable copy of the SQLite DB.
     */
    fun connect(dbFileName: String = "ticketmachine.db") {
        if (db != null) return

        val targetPath = ensureWritableDbCopy(dbFileName)
        db = Database.connect(
            url = "jdbc:sqlite:${targetPath.toAbsolutePath()}",
            driver = "org.sqlite.JDBC"
        )
    }

    /**
     * Reads all destinations from the database.
     */
    fun getAllDestinations(): List<Destination> = transaction {
        DestinationsTable
            .selectAll()
            .map {
                Destination(
                    name = it[DestinationsTable.name],
                    singlePrice = it[DestinationsTable.singlePrice],
                    returnPrice = it[DestinationsTable.returnPrice],
                    takings = it[DestinationsTable.takings],
                    salesCount = it[DestinationsTable.salesCount]
                )
            }
    }

    /**
     * Persists updated destination prices / takings / sales.
     */
    fun saveAllDestinations(destinations: List<Destination>) = transaction {
        destinations.forEach { d ->
            val updated = DestinationsTable.update({ DestinationsTable.name eq d.name }) {
                it[singlePrice] = d.singlePrice
                it[returnPrice] = d.returnPrice
                it[takings] = d.takings
                it[salesCount] = d.salesCount
            }

            if (updated == 0) {
                DestinationsTable.insert {
                    it[name] = d.name
                    it[singlePrice] = d.singlePrice
                    it[returnPrice] = d.returnPrice
                    it[takings] = d.takings
                    it[salesCount] = d.salesCount
                }
            }
        }
    }

    /**
     * Copies the bundled DB from resources to a writable folder.
     */
    private fun ensureWritableDbCopy(dbFileName: String): Path {
        val appDir = Path(System.getProperty("user.home"), ".ticketmachineae2")
        Files.createDirectories(appDir)

        val target = appDir.resolve(dbFileName)
        if (target.exists()) return target

        val resourcePath = "/db/$dbFileName"
        val input = DatabaseManager::class.java.getResourceAsStream(resourcePath)
            ?: error("Database not found at $resourcePath")

        input.use { ins ->
            target.outputStream().use { outs ->
                ins.copyTo(outs)
            }
        }
        return target
    }

    fun getCard(cardNumber: String): Card? = transaction {
        CardsTable
            .selectAll()
            .where { CardsTable.cardNumber eq cardNumber }
            .singleOrNull()
            ?.let {
                Card(
                    cardNumber = it[CardsTable.cardNumber],
                    balance = it[CardsTable.balance],
                )
            }
    }

    fun updateCard(card: Card) = transaction {
        CardsTable.update(
            where = { CardsTable.cardNumber eq card.cardNumber }
        ) {
            it[balance] = card.balance
        }
    }


    fun findDestination(name: String): Destination? {
        // TODO: query DestinationsTable by name
        return null
    }

    fun checkSpecialOffer(dest: Destination, type: TicketType): SpecialOffer? {
        // TODO: query SpecialOffersTable for active offer for (dest, type, date)
        return null
    }

    fun chargeCard(card: Card, amount: Double): Boolean {
        // TODO: read balance from card.cardNumber and compare with calculated price.
        //  if balance > price -> return true else false
        return false
    }

    fun createTicket(
        ticketRef: String,
        username: String,
        cardNumber: String,
        destinationName: String,
        ticketType: TicketType,
        pricePaid: Double
    ): Ticket {
        // TODO: insert into Tickets table and return a Ticket object
        throw NotImplementedError("createTicket not implemented yet")
    }

    fun updateTicketStatus(ticketRef: String){
        // TODO: Changes ticketStatus to CANCELLED/REFUNDED
    }

    fun getTicket(ticketRef: String, user: String?): Ticket? {
        // TODO: query Tickets table by ticketRef and user
        return null
    }

    fun getAdmin(username: String): Admin? {
        // TODO: query Admins table by username
        return null
    }

    fun createDestination(name: String, singlePrice: Double, returnPrice: Double) {
        // TODO: insert new destination into DestinationsTable
        throw NotImplementedError("createDestination not implemented yet")
    }

    fun saveSpecialOffer(
        destination: Destination,
        ticketType: TicketType,
        discount: Double,
        startDate: LocalDate,
        endDate: LocalDate
    ): SpecialOffer {
        // TODO: insert new special offer into SpecialOffers table and return SpecialOffer object
        throw NotImplementedError("saveSpecialOffer not implemented yet")
    }

    fun getSpecialOffer(id: String): SpecialOffer? {
        // TODO: query SpecialOffers table by offerId
        return null
    }

    fun deleteSpecialOffer(id: String) {
        // TODO: delete from SpecialOffers table by offerId
        throw NotImplementedError("deleteSpecialOffer not implemented yet")
    }
}
