package ua.piraeusbank.banking.ui.screen

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.view.touches
import kotlinx.android.synthetic.main.screen_main.*
import ua.piraeusbank.banking.R
import ua.piraeusbank.banking.ui.model.BankServiceKind
import ua.piraeusbank.banking.ui.model.PaymentCard
import ua.piraeusbank.banking.ui.navigation.*
import ua.piraeusbank.banking.ui.screen.adapter.BankServiceAdapter
import ua.piraeusbank.banking.ui.screen.adapter.PaymentCardAdapter
import ua.piraeusbank.banking.ui.screen.adapter.BankServiceViewConfig
import ua.piraeusbank.banking.ui.screen.base.Screen
import ua.piraeusbank.banking.ui.screen.base.ScreenPresentationModel
import java.util.concurrent.TimeUnit

class MainScreen : Screen<MainPm>() {

    private lateinit var bankCardsView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var bankCardAdapter: RecyclerView.Adapter<*>
    private lateinit var bankServicesAdapter: BankServiceAdapter


    companion object {
        fun create() = MainScreen()
    }

    override val screenLayout = R.layout.screen_main

    override fun providePresentationModel(): MainPm {
        return MainPm()
    }

    override fun onBindPresentationModel(pm: MainPm) {
        super.onBindPresentationModel(pm)

        bankServicesAdapter = BankServiceAdapter(mainBankServices)
        { _, config ->  pm.selectBankServiceAction.consumer.accept(config.bankServiceKindScreen)}

        mainBankServicesGrid.apply {
            adapter = bankServicesAdapter
            numColumns = 3
        }

        viewManager = LinearLayoutManager(this.context)
        bankCardAdapter = PaymentCardAdapter(
            listOf(
                PaymentCard("Payment bank card", 7718, PaymentCard.Type.MASTERCARD, "9 950 UAH"),
                PaymentCard("Credit bank card", 5614, PaymentCard.Type.VISA, "7 680 UAH"),
                PaymentCard("Credit bank card", 1414, PaymentCard.Type.VISA, "10 150 UAH")
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


    private val mainBankServices = listOf(
        BankServiceViewConfig(BankServiceKind.MONEY_TRANSFER, R.drawable.ic_arrow_double_right, R.string.bank_service_transfer_money),
        BankServiceViewConfig(BankServiceKind.CARDS_MENU, R.drawable.ic_cards_menu, R.string.bank_service_card_menu),
        BankServiceViewConfig(BankServiceKind.SETTINGS, R.drawable.ic_app_settings, R.string.bank_service_app_settings)
    )
}

class MainPm : ScreenPresentationModel() {

    val viewBankCardAction = Action<Unit>()
    val viewAllCardsAction = Action<Unit>()
    val selectBankServiceAction = Action<BankServiceKind>()

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
    }
}