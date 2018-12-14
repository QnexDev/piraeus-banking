package ua.piraeusbank.banking.ui.navigation

import me.dmdev.rxpm.navigation.NavigationMessage


sealed class BaseNavigationMessage : NavigationMessage
object BackMessage : BaseNavigationMessage()
object UserHasBeenAuthorizedMessage : BaseNavigationMessage()
object ViewBankCardMessage : BaseNavigationMessage()
object ViewAllCardsMessage : BaseNavigationMessage()
