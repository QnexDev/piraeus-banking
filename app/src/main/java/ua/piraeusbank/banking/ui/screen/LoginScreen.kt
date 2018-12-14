package ua.piraeusbank.banking.ui.screen

import com.jakewharton.rxbinding2.view.clicks
import kotlinx.android.synthetic.main.screen_login.*
import ua.piraeusbank.banking.App
import ua.piraeusbank.banking.R
import ua.piraeusbank.banking.ui.pm.LoginPm
import ua.piraeusbank.banking.ui.screen.base.Screen

class LoginScreen : Screen<LoginPm>() {

    override val screenLayout = R.layout.screen_login

    override fun providePresentationModel(): LoginPm {
        return LoginPm(App.component.phoneUtils, App.component.resourceProvider)
    }

    override fun onBindPresentationModel(pm: LoginPm) {
        super.onBindPresentationModel(pm)

        //Bind PM to UI components
        pm.loginButtonEnabled bindTo loginButton::setEnabled
        pm.phoneNumberControl bindTo phoneInput
        pm.passwordControl bindTo passwordInput
        pm.inProgress bindTo progressConsumer

        //Bind UI components to PM
        loginButton.clicks() bindTo pm.loginAction
    }
}