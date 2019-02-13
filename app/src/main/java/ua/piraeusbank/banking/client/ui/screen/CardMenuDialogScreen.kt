package ua.piraeusbank.banking.client.ui.screen

import android.os.Bundle
import android.view.View
import com.fasterxml.jackson.core.type.TypeReference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.screen_card_menu_dialog.*
import ua.piraeusbank.banking.client.App
import ua.piraeusbank.banking.client.R
import ua.piraeusbank.banking.client.service.client.getPaymentCardsByCustomerId
import ua.piraeusbank.banking.client.ui.model.CardMenuKind
import ua.piraeusbank.banking.client.ui.model.PaymentCard
import ua.piraeusbank.banking.client.ui.screen.adapter.CardMenuPreferences
import ua.piraeusbank.banking.client.ui.screen.adapter.CardSpinnerAdapter
import ua.piraeusbank.banking.client.ui.screen.base.Screen
import ua.piraeusbank.banking.client.ui.screen.base.ScreenPresentationModel
import ua.piraeusbank.banking.client.util.CurrentUserContext

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

        when (preferences.kind) {
            CardMenuKind.GET_SECURITY_CODE -> {
                noteInput.visibility = View.GONE
            }
            else -> { }
        }

        val customerId = CurrentUserContext.customer.customerId!!
        App.component.cardAccountRestClient.getPaymentCardsByCustomerId(customerId)
            .subscribeOn(Schedulers.io())
            .map {
                val paymentCards: List<PaymentCard> =
                    App.component.objectMapper.readValue<List<PaymentCard>>(it.string(), object : TypeReference<List<PaymentCard>>() {})

                cardAdapter.cards.addAll(paymentCards)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                cardAdapter.notifyDataSetChanged()
            }

        cardAdapter = CardSpinnerAdapter(
            requireContext(),
            R.layout.transfer_card_layout,
            R.layout.simple_transfer_card_layout,
            ArrayList()
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