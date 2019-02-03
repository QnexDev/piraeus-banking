package ua.piraeusbank.banking.client.ui.screen

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.view.touches
import kotlinx.android.synthetic.main.screen_main.*
import ua.piraeusbank.banking.client.R
import ua.piraeusbank.banking.client.ui.model.BankServiceKind
import ua.piraeusbank.banking.client.ui.model.PaymentCard
import ua.piraeusbank.banking.client.ui.navigation.SelectBankServiceMessage
import ua.piraeusbank.banking.client.ui.navigation.ShowMoreBankServicesMessage
import ua.piraeusbank.banking.client.ui.navigation.ViewAllCardsMessage
import ua.piraeusbank.banking.client.ui.navigation.ViewBankCardMessage
import ua.piraeusbank.banking.client.ui.screen.adapter.BankServiceAdapter
import ua.piraeusbank.banking.client.ui.screen.adapter.AccountCardsAdapter
import ua.piraeusbank.banking.client.ui.screen.base.Screen
import ua.piraeusbank.banking.client.ui.screen.base.ScreenPresentationModel
import java.util.concurrent.TimeUnit

class MainScreen : Screen<MainPm>() {

    private lateinit var bankCardsView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var bankCardAdapter: RecyclerView.Adapter<*>
    private lateinit var bankServicesAdapter: BankServiceAdapter


    companion object {
        fun create() = MainScreen()

        val PAYMENT_CARDS = listOf(
            PaymentCard("Payment bank card", 7718, PaymentCard.Type.MASTERCARD, "9 950 UAH"),
            PaymentCard("Credit bank card", 5614, PaymentCard.Type.VISA, "7 680 UAH"),
            PaymentCard("Credit bank card", 1414, PaymentCard.Type.VISA, "10 150 UAH")
        )
    }

    override val screenLayout = R.layout.screen_main

    override fun providePresentationModel(): MainPm {
        return MainPm()
    }

    override fun onBindPresentationModel(pm: MainPm) {
        super.onBindPresentationModel(pm)

        bankServicesAdapter = BankServiceAdapter(BankServicesScreen.BANK_SERVICE_VIEW_CONFIGS.take(6))
        { _, config -> pm.selectBankServiceAction.consumer.accept(config.bankServiceKind) }

        mainBankServicesGrid.apply {
            adapter = bankServicesAdapter
            numColumns = 3
        }

        viewManager = LinearLayoutManager(this.context)
        bankCardAdapter = AccountCardsAdapter(
            PAYMENT_CARDS,
            R.layout.bank_card_layout
        )

        bankCardsView = bankCards.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = bankCardAdapter
        }

        bankCardsView.touches().map { }.delay(10, TimeUnit.MILLISECONDS) bindTo pm.viewBankCardAction
        allCardsLink.clicks() bindTo pm.viewAllCardsAction
        servicesMenuButton.clicks() bindTo pm.showMoreBankServicesAction
    }
}

class MainPm : ScreenPresentationModel() {

    val viewBankCardAction = Action<Unit>()
    val viewAllCardsAction = Action<Unit>()
    val selectBankServiceAction = Action<BankServiceKind>()
    val showMoreBankServicesAction = Action<Unit>()

    override fun onCreate() {
        super.onCreate()

        viewBankCardAction.observable
            .subscribe { sendMessage(ViewBankCardMessage) }
            .untilDestroy()

        viewAllCardsAction.observable
            .subscribe { sendMessage(ViewAllCardsMessage) }
            .untilDestroy()

        selectBankServiceAction.observable
            .subscribe { sendMessage(SelectBankServiceMessage(it)) }
            .untilDestroy()

        showMoreBankServicesAction.observable
            .subscribe { sendMessage(ShowMoreBankServicesMessage) }
            .untilDestroy()
    }
}