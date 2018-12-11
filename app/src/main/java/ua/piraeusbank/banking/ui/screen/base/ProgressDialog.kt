package ua.piraeusbank.banking.ui.screen.base

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.ProgressBar
import ua.piraeusbank.banking.R


class ProgressDialog : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(context, R.style.ProgressDialogTheme).apply {
            setContentView(ProgressBar(context))
        }
    }
}