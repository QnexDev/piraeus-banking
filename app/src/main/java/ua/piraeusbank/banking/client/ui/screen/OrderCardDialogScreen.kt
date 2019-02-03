package ua.piraeusbank.banking.client.ui.screen

import android.os.Bundle
import android.widget.ArrayAdapter
import com.jakewharton.rxbinding2.widget.selectionEvents
import kotlinx.android.synthetic.main.screen_card_menu_dialog.*
import kotlinx.android.synthetic.main.screen_money_transfer.*
import ua.piraeusbank.banking.client.R
import ua.piraeusbank.banking.client.ui.model.CurrencyCode
import ua.piraeusbank.banking.client.ui.screen.adapter.CardMenuPreferences
import ua.piraeusbank.banking.client.ui.screen.base.Screen
import ua.piraeusbank.banking.client.ui.screen.base.ScreenPresentationModel
import ua.piraeusbank.banking.client.util.toSelectedItemTransformation

class OrderCardDialogScreen : Screen<OrderCardDialogScreenPm>() {

    private lateinit var currencyAdapter: ArrayAdapter<*>

    companion object {
        fun create(preferences: CardMenuPreferences) : OrderCardDialogScreen {
            val screen = OrderCardDialogScreen()
            val bundle = Bundle()
            bundle.putSerializable("preferences", preferences)
            screen.arguments = bundle
            return screen
        }
    }

    override val screenLayout = R.layout.screen_order_card

    override fun providePresentationModel(): OrderCardDialogScreenPm {
        return OrderCardDialogScreenPm()
    }

    override fun onBindPresentationModel(pm: OrderCardDialogScreenPm) {
        val preferences = arguments?.get("preferences") as CardMenuPreferences
        cardMenuToolbar.title = resources.getString(preferences.name)

        super.onBindPresentationModel(pm)

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
class OrderCardDialogScreenPm : ScreenPresentationModel() {

    val currencySelection = Action<CurrencyCode>()

    val selectedCurrency = State<CurrencyCode>()


    override fun onCreate() {
        super.onCreate()

        currencySelection.observable.subscribe { selectedCurrency.consumer }.untilDestroy()

    }
}