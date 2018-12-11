package ua.piraeusbank.banking.ui.screen.base

import android.support.annotation.CallSuper
import me.dmdev.rxpm.PresentationModel
import me.dmdev.rxpm.navigation.NavigationMessage
import me.dmdev.rxpm.widget.dialogControl
import ua.piraeusbank.banking.ui.navigation.BackMessage


abstract class ScreenPresentationModel : PresentationModel() {

    val errorDialog = dialogControl<String, Unit>()

    private val backActionDefault = Action<Unit>()

    open val backAction: Action<Unit> = backActionDefault

    @CallSuper
    override fun onCreate() {
        super.onCreate()

        backActionDefault.observable
            .subscribe { sendMessage(BackMessage()) }
            .untilDestroy()
    }

    protected fun sendMessage(message: NavigationMessage) {
        navigationMessages.consumer.accept(message)
    }

    protected fun showError(errorMessage: String?) {
        errorDialog.show(errorMessage ?: "Unknown error")
    }
}