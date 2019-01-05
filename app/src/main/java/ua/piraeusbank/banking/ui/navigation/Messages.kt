package ua.piraeusbank.banking.ui.navigation

import me.dmdev.rxpm.navigation.NavigationMessage


sealed class BaseNavigationMessage : NavigationMessage
object BackMessage : BaseNavigationMessage()
object UserHasAuthorizedMessage : BaseNavigationMessage()
object ViewBankCardMessage : BaseNavigationMessage()
object ViewAllCardsMessage : BaseNavigationMessage()
data class OpenScreenMessage(val appScreen: AppScreenName): BaseNavigationMessage()
