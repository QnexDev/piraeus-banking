package ua.piraeusbank.banking.ui.pm

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import me.dmdev.rxpm.skipWhileInProgress
import me.dmdev.rxpm.widget.inputControl
import ua.piraeusbank.banking.ui.navigation.UserHasBeenAuthorizedMessage
import ua.piraeusbank.banking.ui.screen.base.ScreenPresentationModel
import ua.piraeusbank.banking.util.PhoneUtils
import ua.piraeusbank.banking.util.ResourceProvider

class LoginPm(
    private val phoneUtils: PhoneUtils,
    private val resourceProvider: ResourceProvider
) : ScreenPresentationModel() {

    val phoneNumberControl = inputControl()
    val passwordControl = inputControl()
    val inProgress = State(false)

    val loginButtonEnabled = State(false)
    val loginAction = Action<Unit>()

    override fun onCreate() {
        super.onCreate()

        Observable.combineLatest(
            phoneNumberControl.textChanges.observable,
            passwordControl.textChanges.observable,
            BiFunction { phone: String, password: String -> Pair(phone, password) })
            .map {
                val (phone, password) = it
                phoneUtils.isValidPhone(phone) && !password.isBlank()
            }
            .subscribe(loginButtonEnabled.consumer)
            .untilDestroy()


        loginAction.observable
            .skipWhileInProgress(inProgress.observable)
            .map { Pair(phoneNumberControl.text.value, passwordControl.text.value) }
            .subscribe { sendMessage(UserHasBeenAuthorizedMessage) }
            .untilDestroy()


//        phoneNumberControl.textChanges.observable
//            .map { phoneUtils.isValidPhone(it) }
//            .subscribe(loginButtonEnabled.consumer)
//            .untilDestroy()
//

    }

}