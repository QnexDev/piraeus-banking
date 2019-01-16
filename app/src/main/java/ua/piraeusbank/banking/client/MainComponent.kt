package ua.piraeusbank.banking.client

import android.app.Application
import ua.piraeusbank.banking.client.util.PhoneUtils
import ua.piraeusbank.banking.client.util.ResourceProvider

class MainComponent(private val context: Application) {

    val phoneUtils by lazy { PhoneUtils }

    val resourceProvider by lazy { ResourceProvider(context) }


}