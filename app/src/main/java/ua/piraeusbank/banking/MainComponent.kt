package ua.piraeusbank.banking

import android.app.Application
import ua.piraeusbank.banking.util.PhoneUtils

class MainComponent(private val context: Application) {

    val phoneUtils by lazy { PhoneUtils() }


}