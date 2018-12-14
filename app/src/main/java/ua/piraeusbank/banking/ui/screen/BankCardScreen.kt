package ua.piraeusbank.banking.ui.screen

import ua.piraeusbank.banking.R
import ua.piraeusbank.banking.ui.pm.BankCardPm
import ua.piraeusbank.banking.ui.screen.base.Screen

class BankCardScreen : Screen<BankCardPm>() {

    companion object {
        fun create() = BankCardScreen()
    }

    override val screenLayout = R.layout.screen_bank_card

    override fun providePresentationModel(): BankCardPm {
        return BankCardPm()
    }


}