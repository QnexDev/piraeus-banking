package ua.piraeusbank.banking.account.domain

import org.javamoney.moneta.Money
import java.time.Instant
import java.time.LocalDateTime
import javax.money.CurrencyUnit
import javax.money.Monetary

data class Account(
        val accountId: Long,
        val customerId: Long,
        val cardId: Long,
        val creationDate: LocalDateTime,
        val balance: Money)

data class AccountTransaction(
        val id: Long,
        val status: AccountTransactionStatus,
        val timestamp: Instant,
        val transactionType: AccountTransactionType,
        val amount: Money,
        val sourceAccount: Long,
        val targetAccount: Long,
        val description: String?,
        val errorMessage: String?)

enum class AccountTransactionType {
    CHECK, TRANSFER
}

enum class AccountTransactionStatus {
    COMPLETED, FAILED
}

val DEFAULT_CURRENCY: CurrencyUnit = Monetary.getCurrency("UAH")