package ua.piraeusbank.banking.ui.screen

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.screen_account.*
import ua.piraeusbank.banking.R
import ua.piraeusbank.banking.ui.model.PaymentCard
import ua.piraeusbank.banking.ui.screen.base.Screen
import ua.piraeusbank.banking.ui.screen.base.ScreenPresentationModel

class AppSettingsScreen : Screen<AppSettingsPm>() {


    companion object {
        fun create() = AppSettingsPm()
    }

    override val screenLayout = R.layout.screen_app_settings

    override fun providePresentationModel(): AppSettingsPm {
        return AppSettingsPm()
    }

    override fun onBindPresentationModel(pm: AppSettingsPm) {
        super.onBindPresentationModel(pm)


    }
}

class AppSettingsPm : ScreenPresentationModel()