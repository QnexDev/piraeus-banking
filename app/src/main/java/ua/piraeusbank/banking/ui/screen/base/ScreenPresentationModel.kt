package ua.piraeusbank.banking.ui.screen.base

import me.dmdev.rxpm.PresentationModel
import me.dmdev.rxpm.navigation.NavigationMessage
import me.dmdev.rxpm.widget.dialogControl
import ua.piraeusbank.banking.ui.navigation.BackMessage


abstract class ScreenPresentationModel : PresentationModel() {

    val errorDialog = dialogControl<String, Unit>()

    private val backActionDefault = Action<Unit>()

    open val backAction: Action<Unit> = backActionDefault

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