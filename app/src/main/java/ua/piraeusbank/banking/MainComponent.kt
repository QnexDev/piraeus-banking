package ua.piraeusbank.banking

import android.app.Application
import ua.piraeusbank.banking.util.PhoneUtils
import ua.piraeusbank.banking.util.ResourceProvider

class MainComponent(private val context: Application) {

    val phoneUtils by lazy { PhoneUtils() }

    val resourceProvider by lazy { ResourceProvider(context) }


}