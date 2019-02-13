package ua.piraeusbank.banking.client.ui.screen

import android.os.Bundle
import android.widget.Toast
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.screen_transfer_confirm.*
import ua.piraeusbank.banking.client.App
import ua.piraeusbank.banking.client.R
import ua.piraeusbank.banking.client.service.client.rxTransferMoneyBetweenCards
import ua.piraeusbank.banking.client.ui.model.request.CardMoneyTransferRequest
import ua.piraeusbank.banking.client.ui.navigation.MoneyTransferStartedMessage
import ua.piraeusbank.banking.client.ui.navigation.OnMainScreenMessage
import ua.piraeusbank.banking.client.ui.screen.base.Screen
import ua.piraeusbank.banking.client.ui.screen.base.ScreenPresentationModel

class TransferConfirmDialog : Screen<TransferConfirmDialogPm>() {

    companion object {
        fun create(message: MoneyTransferStartedMessage): TransferConfirmDialog {
            val screen = TransferConfirmDialog()
            val bundle = Bundle()
            bundle.putSerializable("message", message)
            screen.arguments = bundle
            return screen
        }
    }

    override val screenLayout = R.layout.screen_transfer_confirm

    override fun providePresentationModel(): TransferConfirmDialogPm {
        return TransferConfirmDialogPm()
    }

    override fun onBindPresentationModel(pm: TransferConfirmDialogPm) {
        super.onBindPresentationModel(pm)

        val message = arguments?.get("message") as MoneyTransferStartedMessage
        amount.text = message.amount.amount.toString() + " " + message.amount.currency

        confirmButton.clicks() bindTo Consumer {
            App.component.cardAccountRestClient.rxTransferMoneyBetweenCards(
                CardMoneyTransferRequest(
                    message.source,
                    message.target,
                    message.amount,
                    "Some cool description....."
                )
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it.isEmpty()) {
                        Toast.makeText(requireContext(), "Money has been sent!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                    }

                    pm.confirmAction.consumer.accept(Unit)
                }
        }
        //Bind UI components to PM

    }
}

class TransferConfirmDialogPm : ScreenPresentationModel() {
    val confirmAction = Action<Unit>()
    val inProgress = State(false)


    override fun onCreate() {
        super.onCreate()

        confirmAction.observable
            .subscribe {
                sendMessage(OnMainScreenMessage)
            }

    }
}