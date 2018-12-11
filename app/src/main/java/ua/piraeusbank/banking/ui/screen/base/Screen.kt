package ua.piraeusbank.banking.ui.screen.base

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.functions.Consumer
import me.dmdev.rxpm.base.PmSupportFragment
import ua.piraeusbank.banking.R
import ua.piraeusbank.banking.ui.extensions.findScreen
import ua.piraeusbank.banking.ui.extensions.showDialog


abstract class Screen<PM : ScreenPresentationModel> :
    PmSupportFragment<PM>(),
    BackHandler {

    abstract val screenLayout: Int

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(screenLayout, container, false)
    }

    @CallSuper
    override fun onBindPresentationModel(pm: PM) {
        pm.errorDialog bindTo { message, _ ->
            AlertDialog.Builder(context!!)
                .setMessage(message)
                .setPositiveButton(R.string.ok_button, null)
                .create()
        }
    }

    override fun handleBack(): Boolean {
        presentationModel.backAction.consumer.accept(Unit)
        return true
    }

    val progressConsumer = Consumer<Boolean> {
        if (it) {
            childFragmentManager.showDialog(ProgressDialog())
        } else {
            childFragmentManager
                .findScreen<ProgressDialog>()
                ?.dismiss()
        }
    }
}