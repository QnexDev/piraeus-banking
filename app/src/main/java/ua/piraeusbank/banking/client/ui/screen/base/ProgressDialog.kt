package ua.piraeusbank.banking.client.ui.screen.base

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.ProgressBar
import ua.piraeusbank.banking.client.R


class ProgressDialog : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.ProgressDialogTheme).apply {
            setContentView(ProgressBar(context))
        }
    }
}