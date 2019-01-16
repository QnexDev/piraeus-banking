package ua.piraeusbank.banking.client.util

import java.util.*


class Country(val region: String, val countryCallingCode: Int) {

    companion object {
        private const val UNKNOWN_REGION = "ZZ"
        private const val INVALID_COUNTRY_CODE = 0
        val UNKNOWN = Country(UNKNOWN_REGION, INVALID_COUNTRY_CODE)
    }

    val name = Locale("en", region).getDisplayCountry(Locale.ENGLISH)!!

}
