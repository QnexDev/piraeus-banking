package ua.piraeusbank.banking.common.domain

import org.javamoney.moneta.Money


data class AccountAndCardCreationRequest(val customerId: Long, val currencyCode: String, val networkCode: String)
data class ChangePinCodeRequest(val cardId: Long, val newPinCode: Short)
data class CardMoneyTransferRequest(
        val sourceCardId: Long,
        val targetCardId: Long,
        val amount: Money,
        val description: String?)


