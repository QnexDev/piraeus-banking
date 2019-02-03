package ua.piraeusbank.banking.client.ui.screen

import ua.piraeusbank.banking.client.R
import ua.piraeusbank.banking.client.ui.screen.base.Screen
import ua.piraeusbank.banking.client.ui.screen.base.ScreenPresentationModel

class TransferConfirmDialog : Screen<TransferConfirmDialogPm>() {

    companion object {
        fun create() = TransferConfirmDialog()
    }

    override val screenLayout = R.layout.screen_transfer_confirm

    override fun providePresentationModel(): TransferConfirmDialogPm {
        return TransferConfirmDialogPm()
    }

    override fun onBindPresentationModel(pm: TransferConfirmDialogPm) {
        super.onBindPresentationModel(pm)

        //Bind UI components to PM

    }
}

class TransferConfirmDialogPm : ScreenPresentationModel()