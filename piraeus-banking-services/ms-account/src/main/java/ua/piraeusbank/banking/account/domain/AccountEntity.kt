package ua.piraeusbank.banking.account.domain

import org.javamoney.moneta.Money
import java.time.Instant
import java.time.LocalDateTime
import javax.money.CurrencyUnit
import javax.money.Monetary
import javax.persistence.*

@Entity
@Table(name = "ACCOUNT")
data class AccountEntity(
        @Column(name = "account_id") val accountId: Long? = null,
        @ManyToOne
        @JoinColumn(name = "accountTypeId")
        val accountTypeId: AccountType,
        @Column(name = "customer_id") val customerId: Long,
        @Column(name = "creation_date") val creationDate: LocalDateTime,
        @Column(name = "state") val state: AccountState,
        @Column(name = "balance") val balance: Money,
        @JoinColumn(name = "currency_id")
        @ManyToOne val currency: Currency)


@Entity
@Table(name = "ACCOUNT_TYPE")
data class AccountType(
        @Column(name = "account_type_id") val accountTypeId: Long,
        @Column(name = "name") val name: String,
        @Column(name = "description") val description: String
)

@Entity
@Table(name = "TRANSACTION")
data class TransactionEntity(
        @Column(name = "transaction_id") val id: Long? = null,
        @Column(name = "status") val status: TransactionStatus,
        @ManyToOne
        @JoinColumn(name = "transaction_type_id")
        val type: TransactionType,
        @Column(name = "timestamp") val timestamp: Instant,
        @Column(name = "amount") val amount: Money? = null,
        @Column(name = "source_account_id") val sourceAccountId: Long? = null,
        @Column(name = "target_account_id") val targetAccountId: Long? = null,
        @Column(name = "description") val description: String? = null,
        @Column(name = "errorMessage") val errorMessage: String? = null)

@Entity
@Table(name = "TRANSACTION_TYPE")
data class TransactionType(
        @Column(name = "transaction_type_id") val transactionTypeId: Long,
        @Column(name = "code") val code: TransactionTypeCode,
        @Column(name = "description") val description: String
)

@Entity
@Table(name = "CURRENCY")
data class Currency(
        @Column(name = "currency_id") val id: Long,
        @Column(name = "currency_code") val currencyCode: String,
        @Column(name = "numeric_id") val numericCode: Int
)

enum class AccountState {
    OPENED, BLOCKED, CLOSED
}

enum class TransactionTypeCode {
    CHECK, TRANSFER
}

enum class TransactionStatus {
    STARTED, COMPLETED, FAILED
}

val DEFAULT_CURRENCY: CurrencyUnit = Monetary.getCurrency("UAH")