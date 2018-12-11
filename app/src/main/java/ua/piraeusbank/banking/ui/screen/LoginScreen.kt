package ua.piraeusbank.banking.ui.screen

import kotlinx.android.synthetic.main.screen_login.*
import ua.piraeusbank.banking.App
import ua.piraeusbank.banking.R
import ua.piraeusbank.banking.ui.pm.LoginPm
import ua.piraeusbank.banking.ui.screen.base.Screen

class LoginScreen : Screen<LoginPm>() {

    override val screenLayout = R.layout.screen_login

    override fun providePresentationModel(): LoginPm {
        return LoginPm(App.component.phoneUtils)
    }

    override fun onBindPresentationModel(pm: LoginPm) {
        super.onBindPresentationModel(pm)

        //Bind PM to UI components
        pm.loginButtonEnabled bindTo loginButton::setEnabled
        pm.phoneNumber bindTo phoneInput
        pm.password bindTo passwordInput

        //Bind UI components to PM
    }
}