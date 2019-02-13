package ua.piraeusbank.banking.client.ui.model.request

import ua.piraeusbank.banking.client.ui.model.Money

data class RegistrationRequest(val phone: String,
                               val email: String,
                               val password: String,
                               val name: String,
                               val lastName: String)

data class CardMoneyTransferRequest(
    val sourceCardId: Long,
    val targetCardId: Long,
    val amount: Money,
    val description: String?)

data class AccountAndCardCreationRequest(
    val customerId: Long,
    val currencyCode: String,
    val networkCode: String,
    val balance: Money? = null)