package ua.piraeusbank.banking.ui.screen

import ua.piraeusbank.banking.R
import ua.piraeusbank.banking.ui.pm.MainPm
import ua.piraeusbank.banking.ui.screen.base.Screen

class MainScreen : Screen<MainPm>() {

    override val screenLayout = R.layout.screen_main

    override fun providePresentationModel(): MainPm {
        return MainPm()
    }

    override fun onBindPresentationModel(pm: MainPm) {
        super.onBindPresentationModel(pm)

        //Bind PM to UI components


        //Bind UI components to PM

    }
}