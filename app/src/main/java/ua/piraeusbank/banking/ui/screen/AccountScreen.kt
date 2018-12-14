package ua.piraeusbank.banking.ui.screen

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.screen_account.*
import ua.piraeusbank.banking.R
import ua.piraeusbank.banking.ui.model.BankCard
import ua.piraeusbank.banking.ui.pm.AccountPm
import ua.piraeusbank.banking.ui.screen.base.Screen

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
        bankCardAdapter = BankCardAdapter(
            listOf(
                BankCard("Payment bank card", 7718, BankCard.Type.MASTERCARD, "9 950 UAH"),
                BankCard("Credit bank card", 5614, BankCard.Type.VISA, "7 680 UAH"),
                BankCard("Credit bank card", 1414, BankCard.Type.VISA, "10 150 UAH"),
                BankCard("Credit bank card", 8431, BankCard.Type.MASTERCARD, "1 150 EUR"),
                BankCard("Special bank card", 2417, BankCard.Type.VISA, "10 150 UAH"),
                BankCard("Payment bank card", 6354, BankCard.Type.MASTERCARD, "150 USD")
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