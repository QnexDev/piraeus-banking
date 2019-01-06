package ua.piraeusbank.banking.ui.screen

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.screen_account.*
import ua.piraeusbank.banking.R
import ua.piraeusbank.banking.ui.model.PaymentCard
import ua.piraeusbank.banking.ui.screen.base.Screen
import ua.piraeusbank.banking.ui.screen.base.ScreenPresentationModel

class ClientRegistrationScreen : Screen<ClientRegistrationPm>() {


    companion object {
        fun create() = ClientRegistrationScreen()
    }

    override val screenLayout = R.layout.screen_client_registration

    override fun providePresentationModel(): ClientRegistrationPm {
        return ClientRegistrationPm()
    }

    override fun onBindPresentationModel(pm: ClientRegistrationPm) {
        super.onBindPresentationModel(pm)


    }
}

class ClientRegistrationPm: ScreenPresentationModel()