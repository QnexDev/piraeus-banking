package ua.piraeusbank.banking.client.ui.navigation

import me.dmdev.rxpm.navigation.NavigationMessage
import ua.piraeusbank.banking.client.ui.model.BankServiceKind
import ua.piraeusbank.banking.client.ui.screen.adapter.CardMenuPreferences
import ua.piraeusbank.banking.common.domain.Customer


sealed class BaseNavigationMessage : NavigationMessage
object BackMessage : BaseNavigationMessage()
data class UserHasAuthorizedMessage(val accessToken: String, val customer: Customer) : BaseNavigationMessage()
object ViewBankCardMessage : BaseNavigationMessage()
object ViewAllCardsMessage : BaseNavigationMessage()
data class SelectBankServiceMessage(val bankServiceKind: BankServiceKind): BaseNavigationMessage()
data class SelectCardMenuMessage(val preferences: CardMenuPreferences): BaseNavigationMessage()
object ShowMoreBankServicesMessage : BaseNavigationMessage()
object StartRegistrationMessage : BaseNavigationMessage()
object MoneyTransferStartedMessage : BaseNavigationMessage()

