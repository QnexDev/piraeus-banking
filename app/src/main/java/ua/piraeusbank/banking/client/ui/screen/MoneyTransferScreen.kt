package ua.piraeusbank.banking.client.ui.screen

import android.widget.ArrayAdapter
import android.widget.TextView
import com.fasterxml.jackson.core.type.TypeReference
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.selectionEvents
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.screen_money_transfer.*
import me.dmdev.rxpm.widget.inputControl
import ua.piraeusbank.banking.client.App
import ua.piraeusbank.banking.client.R
import ua.piraeusbank.banking.client.service.client.getPaymentCardsByCustomerId
import ua.piraeusbank.banking.client.ui.model.CurrencyCode
import ua.piraeusbank.banking.client.ui.model.Money
import ua.piraeusbank.banking.client.ui.model.PaymentCard
import ua.piraeusbank.banking.client.ui.navigation.MoneyTransferStartedMessage
import ua.piraeusbank.banking.client.ui.screen.adapter.CardSpinnerAdapter
import ua.piraeusbank.banking.client.ui.screen.base.Screen
import ua.piraeusbank.banking.client.ui.screen.base.ScreenPresentationModel
import ua.piraeusbank.banking.client.util.CurrentUserContext
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

        val customerId = CurrentUserContext.customer.customerId!!
        App.component.cardAccountRestClient.getPaymentCardsByCustomerId(customerId)
            .subscribeOn(Schedulers.io())
            .map {
                val paymentCards: List<PaymentCard> =
                    App.component.objectMapper.readValue<List<PaymentCard>>(
                        it.string(),
                        object : TypeReference<List<PaymentCard>>() {})

                paymentCards
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { cards ->
                fromCardAdapter = CardSpinnerAdapter(
                    requireContext(),
                    R.layout.transfer_card_layout,
                    R.layout.simple_transfer_card_layout,
                    cards.toMutableList()
                ).also { adapter ->
                    fromCards.adapter = adapter

                    fromCards.selectionEvents().skipInitialValue()
                        .compose(toSelectedItemTransformation)
                        .map { fromCardAdapter.cards[it.position()] }
                        .subscribe {  pm.cardFrom = it }
                }

                toCardAdapter = CardSpinnerAdapter(
                    requireContext(),
                    R.layout.transfer_card_layout,
                    R.layout.simple_transfer_card_layout,
                    cards.filter { it != cards.firstOrNull() }.toMutableList()
                ).also { adapter ->
                    toCards.adapter = adapter

                    toCards.selectionEvents().skipInitialValue()
                        .compose(toSelectedItemTransformation)
                        .map { toCardAdapter.cards[it.position()] }
                        .subscribe { pm.cardTo = it }
                }

                pm.cardFrom = cards.first()
                cards.elementAtOrNull(1)?.let {
                    pm.cardTo = it
                }
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
        pm.amount bindTo amountInput
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

    val amount = inputControl()

    val currencySelection = Action<CurrencyCode>()
    val fromCardSelection = Action<PaymentCard>()
    val toCardSelection = Action<PaymentCard>()
    val transferAction = Action<Unit>()

    val selectedCurrency = State<CurrencyCode>()
    val selectedCardFrom = State<PaymentCard>()
    val selectedCardTo = State<PaymentCard>()

    lateinit var cardFrom : PaymentCard
    lateinit var cardTo : PaymentCard

    override fun onCreate() {
        super.onCreate()
        currencySelection.observable.subscribe { selectedCurrency.consumer }.untilDestroy()
        fromCardSelection.observable.subscribe { selectedCardFrom.consumer }.untilDestroy()
        toCardSelection.observable.subscribe { selectedCardTo.consumer }.untilDestroy()

        transferAction.observable
            .subscribe {
                try {
                    sendMessage(
                        MoneyTransferStartedMessage(
                            cardFrom.id,
                            cardTo.id,
                            Money(amount.text.value.toBigDecimal(), "UAH")
                        )
                    )
                } catch (e: Throwable) {
                    showError("Wrong input data!")
                }
            }
            .untilDestroy()

    }
}