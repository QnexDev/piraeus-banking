package ua.piraeusbank.banking.client.ui.screen

import android.widget.ArrayAdapter
import android.widget.TextView
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.selectionEvents
import kotlinx.android.synthetic.main.screen_money_transfer.*
import ua.piraeusbank.banking.client.R
import ua.piraeusbank.banking.client.ui.model.CurrencyCode
import ua.piraeusbank.banking.client.ui.model.PaymentCard
import ua.piraeusbank.banking.client.ui.navigation.MoneyTransferStartedMessage
import ua.piraeusbank.banking.client.ui.screen.adapter.CardSpinnerAdapter
import ua.piraeusbank.banking.client.ui.screen.base.Screen
import ua.piraeusbank.banking.client.ui.screen.base.ScreenPresentationModel
import ua.piraeusbank.banking.client.util.toSelectedItemTransformation

class MoneyTransferScreen : Screen<MoneyTransferPm>() {

    private lateinit var fromCardAdapter: CardSpinnerAdapter
    private lateinit var toCardAdapter: CardSpinnerAdapter
    private lateinit var currencyAdapter: ArrayAdapter<*>


    companion object {
        fun create() = MoneyTransferScreen()
    }

    override val screenLayout = R.layout.screen_money_transfer

    override fun providePresentationModel(): MoneyTransferPm {
        return MoneyTransferPm()
    }

    override fun onBindPresentationModel(pm: MoneyTransferPm) {
        super.onBindPresentationModel(pm)

        val cards = MainScreen.PAYMENT_CARDS

        fromCardAdapter = CardSpinnerAdapter(
            requireContext(),
            R.layout.transfer_card_layout,
            R.layout.simple_transfer_card_layout,
            cards
        ).also { adapter ->
            fromCards.adapter = adapter

            fromCards.selectionEvents().skipInitialValue()
                .compose(toSelectedItemTransformation)
                .map { cards[it.position()] }
                .subscribe { pm.currencySelection.consumer }
        }

        toCardAdapter = CardSpinnerAdapter(
            requireContext(),
            R.layout.transfer_card_layout,
            R.layout.simple_transfer_card_layout,
            cards
        ).also { adapter ->
            toCards.adapter = adapter

            toCards.selectionEvents().skipInitialValue()
                .compose(toSelectedItemTransformation)
                .map { cards[it.position()] }
                .subscribe { pm.currencySelection.consumer }
        }

        currencyAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.currencies,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            currencySpinner.adapter = adapter
            currencySpinner.setSelection(2, false)
            currencySpinner.selectionEvents().skipInitialValue()
                .compose(toSelectedItemTransformation)
                .map { toCurrencyCode(it.id().toInt()) }
                .subscribe { pm.currencySelection.consumer }
        }

        amountInput.setText(100.toString(), TextView.BufferType.EDITABLE)

        transferLink.clicks() bindTo pm.transferAction

    }

    private fun toCurrencyCode(it: Int): CurrencyCode {
        return when (it) {
            0 -> CurrencyCode.EUR
            1 -> CurrencyCode.USD
            2 -> CurrencyCode.UAH
            else -> throw IllegalStateException("Wrong id!")
        }
    }
}

// TODO Maybe need to extract ot separate component (spinner actions)
class MoneyTransferPm : ScreenPresentationModel() {

    val currencySelection = Action<CurrencyCode>()
    val fromCardSelection = Action<PaymentCard>()
    val toCardSelection = Action<PaymentCard>()
    val transferAction = Action<Unit>()

    val selectedCurrency = State<CurrencyCode>()
    val selectedCardFrom = State<PaymentCard>()
    val selectedCardTo = State<PaymentCard>()

    override fun onCreate() {
        super.onCreate()
        currencySelection.observable.subscribe { selectedCurrency.consumer }.untilDestroy()
        fromCardSelection.observable.subscribe { selectedCardFrom.consumer }.untilDestroy()
        toCardSelection.observable.subscribe { selectedCardTo.consumer }.untilDestroy()

        transferAction.observable
            .subscribe { sendMessage(MoneyTransferStartedMessage) }
            .untilDestroy()

    }
}