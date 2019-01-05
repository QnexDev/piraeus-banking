package ua.piraeusbank.banking.ui.screen

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.screen_account.*
import ua.piraeusbank.banking.R
import ua.piraeusbank.banking.ui.model.PaymentCard
import ua.piraeusbank.banking.ui.screen.base.Screen
import ua.piraeusbank.banking.ui.screen.base.ScreenPresentationModel

class CardMenuScreen : Screen<CardMenuPm>() {


    companion object {
        fun create() = CardMenuScreen()
    }

    override val screenLayout = R.layout.screen_card_menu

    override fun providePresentationModel(): CardMenuPm {
        return CardMenuPm()
    }

    override fun onBindPresentationModel(pm: CardMenuPm) {
        super.onBindPresentationModel(pm)


    }
}

class CardMenuPm : ScreenPresentationModel()