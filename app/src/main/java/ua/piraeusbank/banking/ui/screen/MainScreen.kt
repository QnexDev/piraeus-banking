package ua.piraeusbank.banking.ui.screen

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.view.touches
import kotlinx.android.synthetic.main.screen_main.*
import ua.piraeusbank.banking.R
import ua.piraeusbank.banking.ui.model.PeymentCard
import ua.piraeusbank.banking.ui.pm.MainPm
import ua.piraeusbank.banking.ui.screen.base.Screen
import java.util.concurrent.TimeUnit

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
        bankCardAdapter = PaymentCardAdapter(
            listOf(
                PeymentCard("Payment bank card", 7718, PeymentCard.Type.MASTERCARD, "9 950 UAH"),
                PeymentCard("Credit bank card", 5614, PeymentCard.Type.VISA, "7 680 UAH"),
                PeymentCard("Credit bank card", 1414, PeymentCard.Type.VISA, "10 150 UAH")
            ),
            R.layout.bank_card_layout
        )

        bankCardsView = bankCards.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = bankCardAdapter
        }

        bankCardsView.touches().map { }.delay(10, TimeUnit.MILLISECONDS) bindTo pm.viewBankCardAction
        allCardsLink.clicks() bindTo pm.viewAllCardsAction
    }
}