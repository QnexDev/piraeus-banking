package ua.piraeusbank.banking.ui.screen

import kotlinx.android.synthetic.main.screen_bank_services.*
import kotlinx.android.synthetic.main.screen_main.*
import ua.piraeusbank.banking.R
import ua.piraeusbank.banking.ui.model.BankServiceKind
import ua.piraeusbank.banking.ui.navigation.*
import ua.piraeusbank.banking.ui.screen.adapter.BankServiceAdapter
import ua.piraeusbank.banking.ui.screen.adapter.BankServiceViewConfig
import ua.piraeusbank.banking.ui.screen.base.Screen
import ua.piraeusbank.banking.ui.screen.base.ScreenPresentationModel

class BankServicesScreen : Screen<BankServicesPm>() {

    private lateinit var bankServicesAdapter: BankServiceAdapter


    companion object {
        fun create() = BankServicesScreen()

        val BANK_SERVICE_VIEW_CONFIGS = listOf(
            BankServiceViewConfig(BankServiceKind.MONEY_TRANSFER, R.drawable.ic_arrow_double_right, R.string.bank_service_transfer_money),
            BankServiceViewConfig(BankServiceKind.CARDS_MENU, R.drawable.ic_cards_menu, R.string.bank_service_card_menu),
            BankServiceViewConfig(BankServiceKind.SETTINGS, R.drawable.ic_app_settings, R.string.bank_service_app_settings)
        )
    }

    override val screenLayout = R.layout.screen_bank_services

    override fun providePresentationModel(): BankServicesPm {
        return BankServicesPm()
    }

    override fun onBindPresentationModel(pm: BankServicesPm) {
        super.onBindPresentationModel(pm)

        bankServicesAdapter = BankServiceAdapter(BANK_SERVICE_VIEW_CONFIGS)
        { _, config ->  pm.selectBankServiceAction.consumer.accept(config.bankServiceKind)}

        bankServicesGrid.apply {
            adapter = bankServicesAdapter
            numColumns = 3
        }
    }

}

class BankServicesPm : ScreenPresentationModel() {

    val selectBankServiceAction = Action<BankServiceKind>()

    override fun onCreate() {
        super.onCreate()


        selectBankServiceAction.observable
            .subscribe { sendMessage(SelectBankServiceMessage(it)) }
            .untilDestroy()
    }
}