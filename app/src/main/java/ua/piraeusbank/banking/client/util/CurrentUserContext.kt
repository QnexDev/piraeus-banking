package ua.piraeusbank.banking.client.util

import ua.piraeusbank.banking.common.domain.Customer


object CurrentUserContext {

    lateinit var customer: Customer

    lateinit var accessToken: String

}