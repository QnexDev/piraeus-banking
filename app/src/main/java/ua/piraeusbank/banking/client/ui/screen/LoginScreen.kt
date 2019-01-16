package ua.piraeusbank.banking.client.ui.screen

import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.screen_login.*
import me.dmdev.rxpm.skipWhileInProgress
import me.dmdev.rxpm.widget.inputControl
import ua.piraeusbank.banking.client.App
import ua.piraeusbank.banking.client.R
import ua.piraeusbank.banking.client.ui.navigation.StartRegistrationMessage
import ua.piraeusbank.banking.client.ui.navigation.UserHasAuthorizedMessage
import ua.piraeusbank.banking.client.ui.screen.base.Screen
import ua.piraeusbank.banking.client.ui.screen.base.ScreenPresentationModel
import ua.piraeusbank.banking.client.util.PhoneUtils
import ua.piraeusbank.banking.client.util.ResourceProvider

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
        startRegistrationLink.clicks() bindTo pm.startRegistrationAction
    }
}

class LoginPm(
    private val phoneUtils: PhoneUtils,
    private val resourceProvider: ResourceProvider
) : ScreenPresentationModel() {

    val phoneNumberControl = inputControl()
    val passwordControl = inputControl()
    val inProgress = State(false)

    val loginButtonEnabled = State(false)
    val loginAction = Action<Unit>()
    val startRegistrationAction = Action<Unit>()

    override fun onCreate() {
        super.onCreate()

        Observable.combineLatest(
            phoneNumberControl.textChanges.observable,
            passwordControl.textChanges.observable,
            BiFunction { phone: String, password: String -> Pair(phone, password) })
            .map {
                val (phone, password) = it
//                phoneUtils.isValidPhone(phone) && !password.isBlank()
                true
            }
            .subscribe(loginButtonEnabled.consumer)
            .untilDestroy()


        loginAction.observable
            .skipWhileInProgress(inProgress.observable)
            .map { Pair(phoneNumberControl.text.value, passwordControl.text.value) }
            .subscribe { sendMessage(UserHasAuthorizedMessage) }
            .untilDestroy()

        startRegistrationAction.observable
            .subscribe { sendMessage(StartRegistrationMessage) }
            .untilDestroy()
    }

}