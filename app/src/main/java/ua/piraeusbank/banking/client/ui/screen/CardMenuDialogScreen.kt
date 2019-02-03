package ua.piraeusbank.banking.client.ui.screen

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.screen_card_menu_dialog.*
import ua.piraeusbank.banking.client.R
import ua.piraeusbank.banking.client.ui.screen.adapter.CardMenuPreferences
import ua.piraeusbank.banking.client.ui.screen.adapter.CardSpinnerAdapter
import ua.piraeusbank.banking.client.ui.screen.base.Screen
import ua.piraeusbank.banking.client.ui.screen.base.ScreenPresentationModel

class CardMenuDialogScreen : Screen<CardMenuDialogPm>() {

    private lateinit var cardAdapter: CardSpinnerAdapter

    companion object {
        fun create(preferences: CardMenuPreferences) : CardMenuDialogScreen {
            val screen = CardMenuDialogScreen()
            val bundle = Bundle()
            bundle.putSerializable("preferences", preferences)
            screen.arguments = bundle
            return screen
        }

    }

    override val screenLayout = R.layout.screen_card_menu_dialog

    override fun providePresentationModel(): CardMenuDialogPm {
        return CardMenuDialogPm()
    }

    override fun onBindPresentationModel(pm: CardMenuDialogPm) {
        super.onBindPresentationModel(pm)

        val preferences = arguments?.get("preferences") as CardMenuPreferences
        cardMenuToolbar.title = resources.getString(preferences.name)

        if (preferences.description != null) {
            cardMenuDescr.text = resources.getString(preferences.description)
        } else {
            cardMenuDescr.visibility = View.GONE
        }

        val cards = MainScreen.PAYMENT_CARDS

        cardAdapter = CardSpinnerAdapter(
            requireContext(),
            R.layout.transfer_card_layout,
            R.layout.simple_transfer_card_layout,
            cards
        ).also { adapter ->
            chosenCard.adapter = adapter

//            fromCards.selectionEvents().skipInitialValue()
//                .compose(toSelectedItemTransformation)
//                .map { cards[it.position()] }
//                .subscribe { pm.currencySelection.consumer }
        }
    }
}

// TODO Maybe need to extract ot separate component (spinner actions)
class CardMenuDialogPm : ScreenPresentationModel() {


    override fun onCreate() {
        super.onCreate()

    }
}