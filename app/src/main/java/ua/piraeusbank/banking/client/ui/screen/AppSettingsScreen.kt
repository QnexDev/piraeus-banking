package ua.piraeusbank.banking.client.ui.screen

import android.widget.ArrayAdapter
import com.jakewharton.rxbinding2.widget.selectionEvents
import kotlinx.android.synthetic.main.screen_app_settings.*
import ua.piraeusbank.banking.client.R
import ua.piraeusbank.banking.client.ui.model.LanguageCode
import ua.piraeusbank.banking.client.ui.screen.base.Screen
import ua.piraeusbank.banking.client.ui.screen.base.ScreenPresentationModel
import ua.piraeusbank.banking.client.util.toSelectedItemTransformation

class AppSettingsScreen : Screen<AppSettingsPm>() {


    companion object {
        fun create() = AppSettingsScreen()
    }

    override val screenLayout = R.layout.screen_app_settings

    override fun providePresentationModel(): AppSettingsPm {
        return AppSettingsPm()
    }

    override fun onBindPresentationModel(pm: AppSettingsPm) {
        super.onBindPresentationModel(pm)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.languages,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            languageSpinner.adapter = adapter
            languageSpinner.setSelection(0, false)
            languageSpinner.selectionEvents().skipInitialValue()
                .compose(toSelectedItemTransformation)
                .map { toLanguageCode(it.id().toInt()) }
                .subscribe { pm.languageSelection.consumer }
        }
    }

    private fun toLanguageCode(it: Int): LanguageCode {
        return when (it) {
            0 -> LanguageCode.ENG
            1 -> LanguageCode.RUS
            2 -> LanguageCode.URK
            else -> throw IllegalStateException("Wrong id!")
        }
    }
}

// TODO Maybe need to extract ot separate component (spinner actions)
class AppSettingsPm : ScreenPresentationModel() {
    val languageSelection = Action<LanguageCode>()

    val selectedlanguage = State<LanguageCode>()

    override fun onCreate() {
        super.onCreate()
        languageSelection.observable.subscribe { selectedlanguage.consumer }.untilDestroy()



    }
}