package ua.piraeusbank.banking.ui.screen

import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.screen_app_settings.*
import ua.piraeusbank.banking.R
import ua.piraeusbank.banking.ui.screen.base.Screen
import ua.piraeusbank.banking.ui.screen.base.ScreenPresentationModel

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
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            languageSpinner.adapter = adapter
        }

    }
}

class AppSettingsPm : ScreenPresentationModel()