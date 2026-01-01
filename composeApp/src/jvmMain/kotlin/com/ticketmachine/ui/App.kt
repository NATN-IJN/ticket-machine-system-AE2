package com.ticketmachine.ui
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.ticketmachine.database.DatabaseManager
import com.ticketmachine.service.AdminHub
import com.ticketmachine.service.TicketMachine
import com.ticketmachine.ui.navigation.Screen
import com.ticketmachine.ui.screens.*


@Composable
fun App() {
    LaunchedEffect(Unit) { DatabaseManager.connect() }


    val ticketMachine = remember { TicketMachine(originStation = "Southampton", database = DatabaseManager ) }
    val adminHub = remember { AdminHub(database = DatabaseManager, ticketMachine = ticketMachine) }
    var screen by remember { mutableStateOf<Screen>(Screen.MainMenu) }

    when (val s = screen) {

        Screen.MainMenu -> MainMenuScreen(
            onSelectUser = {screen = Screen.SelectUser},
            onSelectAdmin = { screen = Screen.AdminLogin})

        Screen.SelectUser -> SelectUserScreen(
            onBack = { screen = Screen.MainMenu },
            onContinue = { user ->
                ticketMachine.setCurrentUser(user)
                screen = Screen.UserMenu
            }
        )

        Screen.UserMenu -> UserMenuScreen(
            onSearchTicket = {screen = Screen.SearchTicket},
            onViewTicket = {screen = Screen.ViewTicket },
            onCancelTicket = {screen = Screen.CancelTicket},
            onInsertCard =  {screen = Screen.InsertCard},
            onBack = { screen = Screen.SelectUser })

        Screen.InsertCard -> InsertCardScreen(
            onBack = { screen = Screen.SelectUser },
            onContinue = { cardNumber ->
                val card = ticketMachine.insertCard(cardNumber)
                if (card != null) {
                    screen = Screen.SearchTicket
                    true
                } else {
                    false
                }
            }
        )


        Screen.SearchTicket -> SearchTicketScreen(
            onBack = { screen = Screen.UserMenu },
            ticketMachine = ticketMachine,
            onPurchased = { ticket ->
                screen = Screen.Confirmation(ticket)
            }
        )

        Screen.CancelTicket -> CancelTicketScreen(
            ticketMachine = ticketMachine,
            onBack = { screen = Screen.UserMenu }
        )

        Screen.ViewTicket -> ViewTicketScreen(
            ticketMachine = ticketMachine,
            onBack = { screen = Screen.UserMenu }
        )

        is Screen.Confirmation -> ConfirmationScreen(
            ticket = s.ticket,
            onDone = { screen = Screen.UserMenu }
        )

        Screen.AdminLogin -> AdminLoginScreen(
            onBack = { screen = Screen.MainMenu },
            onLogin = { screen = Screen.AdminMenu },
            adminHub = adminHub
        )

        Screen.AdminMenu -> AdminMenuScreen(
            onViewDestination = { screen = Screen.ViewDestination },
            onAddDestination = { screen = Screen.AddDestination },
            onChangeAllTicketPrices = { screen = Screen.ChangeAllTicketPrices },
            onAddSpecialOffer = { screen = Screen.AddSpecialOffer },
            onSearchSpecialOffer = { screen = Screen.SearchSpecialOffer },
            onBack = { screen = Screen.MainMenu }
        )

        Screen.ViewDestination -> ViewDestinationScreen(
            adminHub = adminHub,
            onBack = { screen = Screen.AdminMenu },
            onEdit = { destination ->
                screen = Screen.UpdateDestinationPrices(destination)
            }
        )

        is Screen.UpdateDestinationPrices -> UpdateDestinationPricesScreen(
            destination = s.destination,
            adminHub = adminHub,
            onBack = { screen = Screen.AdminMenu },
        )

        Screen.AddDestination -> AddDestinationScreen(
            adminHub = adminHub,
            onBack = { screen = Screen.AdminMenu }
        )

        Screen.ChangeAllTicketPrices -> ChangeAllTicketPricesScreen(
            adminHub = adminHub,
            onBack = { screen = Screen.AdminMenu }
        )

        Screen.AddSpecialOffer -> AddSpecialOfferScreen(
            adminHub = adminHub,
            onBack = { screen = Screen.AdminMenu },
            onCreated = { newOffer ->
                screen = Screen.AdminMenu
            }
        )
        Screen.SearchSpecialOffer -> SearchSpecialOfferScreen(
            adminHub = adminHub,
            onBack = { screen = Screen.AdminMenu },
            onDelete = { offerId ->
                screen = Screen.DeleteSpecialOffer(offerId)
            }
        )

        is Screen.DeleteSpecialOffer -> DeleteSpecialOfferScreen(
            adminHub = adminHub,
            offerId = s.offerId,
            onBack = { screen = Screen.SearchSpecialOffer }
        )
    }
}