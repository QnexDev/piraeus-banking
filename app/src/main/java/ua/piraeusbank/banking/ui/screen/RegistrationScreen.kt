package ua.piraeusbank.banking.ui.screen

import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import kotlinx.android.synthetic.main.screen_client_registration.*
import kotlinx.android.synthetic.main.screen_login.*
import me.dmdev.rxpm.skipWhileInProgress
import me.dmdev.rxpm.widget.inputControl
import ua.piraeusbank.banking.R
import ua.piraeusbank.banking.ui.model.request.RegistrationRequest
import ua.piraeusbank.banking.ui.screen.base.Screen
import ua.piraeusbank.banking.ui.screen.base.ScreenPresentationModel
import ua.piraeusbank.banking.util.PhoneUtils

class RegistrationScreen : Screen<RegistrationPm>() {


    companion object {
        fun create() = RegistrationScreen()
    }

    override val screenLayout = R.layout.screen_client_registration

    override fun providePresentationModel(): RegistrationPm {
        return RegistrationPm()
    }

    override fun onBindPresentationModel(pm: RegistrationPm) {
        super.onBindPresentationModel(pm)

        //Bind PM to UI components
        pm.registrationButtonEnabled bindTo loginButton::setEnabled
        pm.phoneNumberControl bindTo regPhoneInput
        pm.emailControl bindTo regEmailInput
        pm.passwordControl bindTo regPasswordInput
        pm.passwordAgainControl bindTo regPasswordAgainInput
        pm.inProgress bindTo progressConsumer

        //Bind UI components to PM
        registrationButton.clicks() bindTo pm.registrationAction

    }
}

class RegistrationPm : ScreenPresentationModel() {

    val phoneNumberControl = inputControl()
    val emailControl = inputControl()
    val passwordControl = inputControl()
    val passwordAgainControl = inputControl()
    val nameControl = inputControl()
    val lastNameControl = inputControl()

    val inProgress = State(false)

    val registrationButtonEnabled = State(false)
    val registrationAction = Action<Unit>()


    override fun onCreate() {
        super.onCreate()

        Observable.combineLatest(
            arrayOf(
                phoneNumberControl.textChanges.observable,
                emailControl.textChanges.observable,
                passwordControl.textChanges.observable,
                passwordAgainControl.textChanges.observable,
                nameControl.textChanges.observable,
                lastNameControl.textChanges.observable
            )
        ) { it -> it }
            .map {
                val phoneValid = PhoneUtils.isValidPhone(phoneNumberControl.text.value)
                val passwordsValid = passwordControl.text.value == passwordAgainControl.text.value
                phoneValid && passwordsValid
            }
            .subscribe(registrationButtonEnabled.consumer)
            .untilDestroy()


        registrationAction.observable
            .skipWhileInProgress(inProgress.observable)
            .map { buildRequest() }
            .subscribe { print(it) }
            .untilDestroy()
    }

    private fun buildRequest() = RegistrationRequest(
        phoneNumberControl.text.value,
        emailControl.text.value,
        passwordControl.text.value,
        nameControl.text.value,
        lastNameControl.text.value
    )
}