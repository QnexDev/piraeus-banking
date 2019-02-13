package ua.piraeusbank.banking.client.ui.screen

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.selectionEvents
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.screen_order_card.*
import ua.piraeusbank.banking.client.App
import ua.piraeusbank.banking.client.R
import ua.piraeusbank.banking.client.service.client.rxCreateCardAndAccount
import ua.piraeusbank.banking.client.ui.model.CurrencyCode
import ua.piraeusbank.banking.client.ui.model.request.AccountAndCardCreationRequest
import ua.piraeusbank.banking.client.ui.screen.adapter.CardMenuPreferences
import ua.piraeusbank.banking.client.ui.screen.base.Screen
import ua.piraeusbank.banking.client.ui.screen.base.ScreenPresentationModel
import ua.piraeusbank.banking.client.util.CurrentUserContext
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
        cardOrderMenuToolbar.title = resources.getString(preferences.name)

        super.onBindPresentationModel(pm)

        currencyAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.currencies,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            currencyOrderSpinner.adapter = adapter
            currencyOrderSpinner.setSelection(2, false)
            currencyOrderSpinner.selectionEvents().skipInitialValue()
                .compose(toSelectedItemTransformation)
                .map { toCurrencyCode(it.id().toInt()) }
                .subscribe { pm.currencySelection.consumer }
        }

        val customerId = CurrentUserContext.customer.customerId!!

        confirmOrderButton.clicks() bindTo Consumer {
            App.component.cardAccountRestClient.rxCreateCardAndAccount(
                AccountAndCardCreationRequest(customerId, "UAH", "VISA"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it.isEmpty()) {
                        Toast.makeText(requireContext(), "The card has been created!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                    }
                }
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