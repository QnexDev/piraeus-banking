package ua.piraeusbank.banking.domain.model

import ua.piraeusbank.banking.domain.entity.TransactionEntity

data class AccountMoneyTransferMessage(val transaction: TransactionEntity)