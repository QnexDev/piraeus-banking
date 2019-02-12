package ua.piraeusbank.banking.domain.model

import ua.piraeusbank.banking.common.domain.AccountAndCardCreationRequest
import ua.piraeusbank.banking.domain.entity.TransactionEntity

data class AccountMoneyTransferMessage(val transaction: TransactionEntity)
data class CardAccountCreationMessage(val request: AccountAndCardCreationRequest)
data class UserRegistrationMessage(val request: AuthUser)