package ua.piraeusbank.banking.client.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import me.dmdev.rxpm.navigation.NavigationMessage
import me.dmdev.rxpm.navigation.NavigationMessageHandler
import ua.piraeusbank.banking.client.R
import ua.piraeusbank.banking.client.ui.extensions.currentScreen
import ua.piraeusbank.banking.client.ui.extensions.openScreen
import ua.piraeusbank.banking.client.ui.model.BankServiceKind
import ua.piraeusbank.banking.client.ui.model.CardMenuKind
import ua.piraeusbank.banking.client.ui.navigation.*
import ua.piraeusbank.banking.client.ui.screen.*
import ua.piraeusbank.banking.client.ui.screen.base.BackHandler
import ua.piraeusbank.banking.client.ui.screen.base.Screen

class LaunchActivity : AppCompatActivity(), NavigationMessageHandler {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.openScreen(LoginScreen(), addToBackStack = false)
        }
    }

    override fun onBackPressed() {
        supportFragmentManager.currentScreen?.let {
            if (it is BackHandler && it.handleBack()) return
        }

        if (supportFragmentManager.backStackEntryCount == 0) {
            super.onBackPressed()
        }
    }

    override fun handleNavigationMessage(message: NavigationMessage): Boolean {

        val sfm = supportFragmentManager

        when (message) {

            is BackMessage -> super.onBackPressed()

            is UserHasAuthorizedMessage -> sfm.openScreen(MainScreen.create(), addToBackStack = false)

            is ViewAllCardsMessage -> sfm.openScreen(AccountScreen.create())

            is ViewBankCardMessage -> sfm.openScreen(CardStatementScreen.create())

            is SelectBankServiceMessage -> sfm.openScreen(selectBankServiceScreen(message))

            is ShowMoreBankServicesMessage -> sfm.openScreen(BankServicesScreen.create())

            is StartRegistrationMessage -> sfm.openScreen(RegistrationScreen.create())

            is MoneyTransferStartedMessage -> sfm.openScreen(TransferConfirmDialog.create())

            is SelectCardMenuMessage -> sfm.openScreen(selectCardMenuScreen(message))
        }
        return true
    }

    private fun selectBankServiceScreen(message: SelectBankServiceMessage): Screen<*> {
        return when (message.bankServiceKind) {
            BankServiceKind.CARDS_MENU -> CardMenuScreen.create()
            BankServiceKind.MONEY_TRANSFER -> MoneyTransferScreen.create()
            BankServiceKind.SETTINGS -> AppSettingsScreen.create()
            BankServiceKind.ACCOUNT_STATEMENT -> AccountStatementScreen.create()
        }
    }

    private fun selectCardMenuScreen(message: SelectCardMenuMessage): Screen<*> {
        return when (message.preferences.kind) {
            CardMenuKind.ORDER_CARD -> OrderCardDialogScreen.create(message.preferences)
            else -> CardMenuDialogScreen.create(message.preferences)
        }
    }
}