package ua.piraeusbank.banking.ui.screen

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.screen_account.*
import ua.piraeusbank.banking.R
import ua.piraeusbank.banking.ui.model.PaymentCard
import ua.piraeusbank.banking.ui.screen.base.Screen
import ua.piraeusbank.banking.ui.screen.base.ScreenPresentationModel

class MoneyTransferScreen : Screen<MoneyTransferPm>() {


    companion object {
        fun create() = AppSettingsPm()
    }

    override val screenLayout = R.layout.screen_money_transfer

    override fun providePresentationModel(): MoneyTransferPm {
        return MoneyTransferPm()
    }

    override fun onBindPresentationModel(pm: MoneyTransferPm) {
        super.onBindPresentationModel(pm)


    }
}

class MoneyTransferPm : ScreenPresentationModel()