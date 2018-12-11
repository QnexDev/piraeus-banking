package ua.piraeusbank.banking.ui.pm

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import me.dmdev.rxpm.widget.inputControl
import ua.piraeusbank.banking.ui.screen.base.ScreenPresentationModel
import ua.piraeusbank.banking.util.PhoneUtils

class LoginPm(private val phoneUtils: PhoneUtils) : ScreenPresentationModel() {

    val phoneNumber = inputControl()
    val password = inputControl()
    val inProgress = State(false)

    val loginButtonEnabled = State(false)
    val loginAction = Action<Unit>()

    override fun onCreate() {
        super.onCreate()

        Observable.combineLatest(phoneNumber.textChanges.observable,  password.textChanges.observable,
            BiFunction { number: String, password: String ->
                phoneUtils.isValidPhone(number)
            })
            .subscribe(loginButtonEnabled.consumer)
            .untilDestroy()

    }
}