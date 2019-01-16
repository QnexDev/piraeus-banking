package ua.piraeusbank.banking.client.ui.screen

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.screen_account.*
import ua.piraeusbank.banking.client.R
import ua.piraeusbank.banking.client.ui.model.PaymentCard
import ua.piraeusbank.banking.client.ui.screen.adapter.PaymentCardAdapter
import ua.piraeusbank.banking.client.ui.screen.base.Screen
import ua.piraeusbank.banking.client.ui.screen.base.ScreenPresentationModel

class AccountScreen : Screen<AccountPm>() {

    private lateinit var bankCardsView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var bankCardAdapter: RecyclerView.Adapter<*>


    companion object {
        fun create() = AccountScreen()
    }

    override val screenLayout = R.layout.screen_account

    override fun providePresentationModel(): AccountPm {
        return AccountPm()
    }

    override fun onBindPresentationModel(pm: AccountPm) {
        super.onBindPresentationModel(pm)

        viewManager = LinearLayoutManager(this.context)
        bankCardAdapter = PaymentCardAdapter(
            listOf(
                PaymentCard("Payment bank card", 7718, PaymentCard.Type.MASTERCARD, "9 950 UAH"),
                PaymentCard("Credit bank card", 5614, PaymentCard.Type.VISA, "7 680 UAH"),
                PaymentCard("Credit bank card", 1414, PaymentCard.Type.VISA, "10 150 UAH"),
                PaymentCard("Credit bank card", 8431, PaymentCard.Type.MASTERCARD, "1 150 EUR"),
                PaymentCard("Special bank card", 2417, PaymentCard.Type.VISA, "10 150 UAH"),
                PaymentCard("Payment bank card", 6354, PaymentCard.Type.MASTERCARD, "150 USD")
            ),
            R.layout.full_bank_card_layout
        )

        bankCardsView = allBankCards.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = bankCardAdapter
        }
    }
}

class AccountPm : ScreenPresentationModel()