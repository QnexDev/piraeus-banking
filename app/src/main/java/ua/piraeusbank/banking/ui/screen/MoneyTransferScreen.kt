package ua.piraeusbank.banking.ui.screen

import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.screen_money_transfer.*
import ua.piraeusbank.banking.R
import ua.piraeusbank.banking.ui.screen.adapter.TransferPaymentCardAdapter
import ua.piraeusbank.banking.ui.screen.base.Screen
import ua.piraeusbank.banking.ui.screen.base.ScreenPresentationModel

class MoneyTransferScreen : Screen<MoneyTransferPm>() {

    private lateinit var fromCardAdapter: TransferPaymentCardAdapter
    private lateinit var toCardAdapter: TransferPaymentCardAdapter
    private lateinit var currenciesAdapter: ArrayAdapter<*>

    companion object {
        fun create() = MoneyTransferScreen()
    }

    override val screenLayout = R.layout.screen_money_transfer

    override fun providePresentationModel(): MoneyTransferPm {
        return MoneyTransferPm()
    }

    override fun onBindPresentationModel(pm: MoneyTransferPm) {
        super.onBindPresentationModel(pm)

        fromCardAdapter = TransferPaymentCardAdapter(
            requireContext(),
            R.layout.transfer_card_layout,
            R.layout.simple_transfer_card_layout,
            MainScreen.PAYMENT_CARDS).also { adapter ->
            fromCards.adapter = adapter
        }

        toCardAdapter = TransferPaymentCardAdapter(
            requireContext(),
            R.layout.transfer_card_layout,
            R.layout.simple_transfer_card_layout,
            MainScreen.PAYMENT_CARDS).also { adapter ->
            toCards.adapter = adapter
        }

        currenciesAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.currencies,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            currencySpinner.adapter = adapter
        }

        amountInput.setText(100.toString(), TextView.BufferType.EDITABLE)
    }
}

class MoneyTransferPm : ScreenPresentationModel()