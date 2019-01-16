package ua.piraeusbank.banking.client.ui.navigation

import me.dmdev.rxpm.navigation.NavigationMessage
import ua.piraeusbank.banking.client.ui.model.BankServiceKind


sealed class BaseNavigationMessage : NavigationMessage
object BackMessage : BaseNavigationMessage()
object UserHasAuthorizedMessage : BaseNavigationMessage()
object ViewBankCardMessage : BaseNavigationMessage()
object ViewAllCardsMessage : BaseNavigationMessage()
data class SelectBankServiceMessage(val bankServiceKind: BankServiceKind): BaseNavigationMessage()
object ShowMoreBankServicesMessage : BaseNavigationMessage()
object StartRegistrationMessage : BaseNavigationMessage()
