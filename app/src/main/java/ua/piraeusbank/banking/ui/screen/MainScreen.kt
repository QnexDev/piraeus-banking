package ua.piraeusbank.banking.ui.screen

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.jakewharton.rxbinding2.view.clicks
import kotlinx.android.synthetic.main.screen_main.*
import ua.piraeusbank.banking.R
import ua.piraeusbank.banking.ui.model.BankCard
import ua.piraeusbank.banking.ui.pm.MainPm
import ua.piraeusbank.banking.ui.screen.base.Screen

class MainScreen : Screen<MainPm>() {

    private lateinit var bankCardsView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var bankCardAdapter: RecyclerView.Adapter<*>

    companion object {
        fun create() = MainScreen()
    }

    override val screenLayout = R.layout.screen_main

    override fun providePresentationModel(): MainPm {
        return MainPm()
    }

    override fun onBindPresentationModel(pm: MainPm) {
        super.onBindPresentationModel(pm)

        viewManager = LinearLayoutManager(this.context)
        bankCardAdapter = BankCardAdapter(
            listOf(
                BankCard("Payment bank card", 7718, BankCard.Type.MASTERCARD, "9 950 UAH"),
                BankCard("Credit bank card", 5614, BankCard.Type.VISA, "7 680 UAH"),
                BankCard("Credit bank card", 1414, BankCard.Type.VISA, "10 150 UAH")
            ),
            R.layout.bank_card_layout
        )

        bankCardsView = bankCards.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = bankCardAdapter
        }

        bankCardsView.clicks() bindTo pm.viewBankCardAction
        allCardsLink.clicks() bindTo pm.viewAllCardsAction
    }
}