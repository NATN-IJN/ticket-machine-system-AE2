package com.ticketmachine.database

import com.ticketmachine.domain.Admin
import com.ticketmachine.domain.SpecialOffer
import com.ticketmachine.domain.Ticket
import com.ticketmachine.domain.TicketType
import com.ticketmachine.domain.TicketStatus
import com.ticketmachine.domain.User
import java.time.LocalDate
import com.ticketmachine.domain.Card
import com.ticketmachine.domain.Destination
import com.ticketmachine.domain.OfferStatus
import org.jetbrains.exposed.sql.SchemaUtils
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
        println("DB path in use = ${targetPath.toAbsolutePath()}")
        transaction {
            SchemaUtils.create(
                DestinationsTable,
                CardsTable,
                TicketsTable,
                SpecialOffersTable
            )
        }
        seedDestinationsIfEmpty()
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


    fun findDestination(name: String): Destination? = transaction {
        DestinationsTable
            .selectAll()
            .where { DestinationsTable.name eq name }
            .singleOrNull()
            ?.let {
                Destination(
                    name = it[DestinationsTable.name],
                    singlePrice = it[DestinationsTable.singlePrice],
                    returnPrice = it[DestinationsTable.returnPrice],
                    takings = it[DestinationsTable.takings],
                    salesCount = it[DestinationsTable.salesCount]
                )
            }
    }

    fun checkSpecialOffer(dest: Destination, type: TicketType): SpecialOffer? {
        // TODO: query SpecialOffersTable for active offer for (dest, type, date)
        return null
    }

    fun chargeCard(card: Card, amount: Double): Boolean {
        if (amount <= 0.0) return false
        if(amount > card.balance) return false
        card.deduct(amount)
        updateCard(card)

        return true
    }

    fun createTicket(
        destination: Destination,
        type: TicketType,
        price: Double,
        username: String,
        cardNumber: String,
        origin: String
    ): Ticket = transaction {

        val ticketRef = generateTicketRef()
        val purchaseDate = LocalDate.now().toString()

        TicketsTable.insert {
            it[TicketsTable.ticketRef] = ticketRef
            it[TicketsTable.username] = username
            it[TicketsTable.cardNumber] = cardNumber
            it[TicketsTable.destinationName] = destination.name
            it[TicketsTable.type] = type.name
            it[TicketsTable.price] = price
            it[TicketsTable.status] = TicketStatus.ACTIVE.name
            it[TicketsTable.purchaseDate] = purchaseDate
        }

        Ticket(
            ticketRef = ticketRef,
            origin = origin,                 // passed in
            destination = destination,
            price = price,
            type = type,
            status = TicketStatus.ACTIVE
        )


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

    fun findActiveOffer(
        destination: Destination,
        type: TicketType,
        onDate: LocalDate
    ): SpecialOffer? = transaction {

        SpecialOffersTable
            .selectAll()
            .where {
                (SpecialOffersTable.destination eq destination.name) and
                        (SpecialOffersTable.ticketType eq type.name) and
                        (SpecialOffersTable.status eq OfferStatus.ACTIVE.name)
            }
            .singleOrNull()
            ?.let { row ->

                val offer = SpecialOffer(
                    offerId = row[SpecialOffersTable.id].value,
                    destination = destination,
                    ticketType = TicketType.valueOf(row[SpecialOffersTable.ticketType]),
                    discountFactor = row[SpecialOffersTable.discountFactor],
                    startDate = LocalDate.parse(row[SpecialOffersTable.startDate]),
                    endDate = LocalDate.parse(row[SpecialOffersTable.endDate]),
                    status = OfferStatus.valueOf(row[SpecialOffersTable.status])
                )

                if (offer.isActiveOn(onDate)) offer else null
            }
    }

    private fun generateTicketRef(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..4).map { chars.random() }.joinToString("")
    }

    fun seedDestinationsIfEmpty() = transaction {
        if (DestinationsTable.selectAll().empty()) {
            DestinationsTable.insert {
                it[name] = "North Bridge"
                it[singlePrice] = 2.50
                it[returnPrice] = 4.00
                it[takings] = 0.0
                it[salesCount] = 0
            }
        }
    }
}
