package ua.piraeusbank.banking.ui.screen

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ArrayAdapter
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.view.touches
import kotlinx.android.synthetic.main.screen_main.*
import ua.piraeusbank.banking.R
import ua.piraeusbank.banking.ui.model.PaymentCard
import ua.piraeusbank.banking.ui.navigation.ViewAllCardsMessage
import ua.piraeusbank.banking.ui.navigation.ViewBankCardMessage
import ua.piraeusbank.banking.ui.screen.adapter.MainServiceAdapter
import ua.piraeusbank.banking.ui.screen.adapter.PaymentCardAdapter
import ua.piraeusbank.banking.ui.screen.adapter.ServiceViewResources
import ua.piraeusbank.banking.ui.screen.base.Screen
import ua.piraeusbank.banking.ui.screen.base.ScreenPresentationModel
import java.util.concurrent.TimeUnit

class MainScreen : Screen<MainPm>() {

    private lateinit var bankCardsView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var bankCardAdapter: RecyclerView.Adapter<*>
    private lateinit var mainServicesAdapter: MainServiceAdapter


    companion object {
        fun create() = MainScreen()
    }

    override val screenLayout = R.layout.screen_main

    override fun providePresentationModel(): MainPm {
        return MainPm()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainServicesAdapter = MainServiceAdapter(mainServices)
    }

    override fun onBindPresentationModel(pm: MainPm) {
        super.onBindPresentationModel(pm)

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


    private val mainServices = listOf(
        ServiceViewResources(R.layout.screen_money_transfer, 0, R.string.main_service_transfer_money),
        ServiceViewResources(R.layout.screen_card_menu, 0, R.string.main_service_card_menu),
        ServiceViewResources(R.layout.screen_app_settings, 0, R.string.main_service_app_settings)

    )
}

class MainPm : ScreenPresentationModel() {

    val viewBankCardAction = Action<Unit>()
    val viewAllCardsAction = Action<Unit>()

    override fun onCreate() {
        super.onCreate()


        viewBankCardAction.observable
            .subscribe { sendMessage(ViewBankCardMessage) }
            .untilDestroy()

        viewAllCardsAction.observable
            .subscribe { sendMessage(ViewAllCardsMessage) }
            .untilDestroy()
    }
}