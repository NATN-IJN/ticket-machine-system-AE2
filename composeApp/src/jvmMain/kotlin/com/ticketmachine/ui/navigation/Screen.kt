package com.ticketmachine.ui.navigation

import com.ticketmachine.domain.Destination
import com.ticketmachine.domain.Ticket

sealed class Screen {
    data object MainMenu : Screen()
    data object UserMenu : Screen()
    data object SelectUser : Screen()
    data object InsertCard : Screen()
    data object SearchTicket : Screen()
    data object CancelTicket : Screen()
    data object ViewTicket : Screen()


    data object AdminLogin : Screen()
    data object AdminMenu : Screen()

    data object ViewDestination : Screen()
    data class UpdateDestinationPrices(val destination: Destination) : Screen()
    data object AddDestination : Screen()
    data object ChangeAllTicketPrices : Screen()
    data object AddSpecialOffer : Screen()
    data object SearchSpecialOffer : Screen()
    data class DeleteSpecialOffer (val offerId: Int): Screen()
    data class Confirmation(val ticket: Ticket) : Screen()
}