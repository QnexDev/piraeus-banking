package ua.piraeusbank.banking.ui.pm

import ua.piraeusbank.banking.ui.navigation.ViewAllCardsMessage
import ua.piraeusbank.banking.ui.navigation.ViewBankCardMessage
import ua.piraeusbank.banking.ui.screen.base.ScreenPresentationModel

class MainPm : ScreenPresentationModel() {

    val viewBankCardAction = Action<Unit>()
    val viewAllCardsAction = Action<Unit>()

    override fun onCreate() {
        super.onCreate()


        viewBankCardAction.observable
            .subscribe { sendMessage(ViewBankCardMessage) }
            .untilDestroy()

        viewAllCardsAction.observable
            .subscribe { sendMessage(ViewAllCardsMessage) }
            .untilDestroy()
    }
}