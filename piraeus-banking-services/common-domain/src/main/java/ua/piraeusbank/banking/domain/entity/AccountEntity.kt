package ua.piraeusbank.banking.domain.entity

import org.javamoney.moneta.Money
import ua.piraeusbank.banking.domain.conversion.MoneyConverter
import java.time.Instant
import java.time.LocalDateTime
import javax.money.CurrencyUnit
import javax.money.Monetary
import javax.persistence.*

@Entity
@Table(name = "ACCOUNT")
data class AccountEntity(
        @GeneratedValue
        @Id @Column(name = "account_id") val accountId: Long? = null,
        @ManyToOne
        @JoinColumn(name = "account_type_id")
        val accountType: AccountTypeEntity,
        @ManyToOne
        @JoinColumn(name = "customer_id")
        val customer: CustomerEntity,
        @Column(name = "creation_date") val creationDate: LocalDateTime,
        @Column(name = "status") val status: AccountStatus,
        @Convert(converter = MoneyConverter::class)
        @Column(name = "balance") val balance: Money,
        @ManyToOne
        @JoinColumn(name = "currency_id")
        val currency: CurrencyEntity)


@Entity
@Table(name = "ACCOUNT_TYPE")
data class AccountTypeEntity(
        @GeneratedValue
        @Id @Column(name = "account_type_id") val accountTypeId: Long? = null,
        @Column(name = "name") val name: String,
        @Column(name = "description") val description: String
)

@Entity
@Table(name = "TRANSACTION")
data class TransactionEntity(
        @GeneratedValue
        @Id @Column(name = "transaction_id") val id: Long? = null,
        @Enumerated(EnumType.STRING) @Column(name = "status") val status: TransactionStatus,
        @ManyToOne
        @JoinColumn(name = "transaction_type_id")
        val type: TransactionTypeEntity,
        @Column(name = "timestamp") val timestamp: Instant,
        @Convert(converter = MoneyConverter::class)
        @Column(name = "amount") val amount: Money? = null,
        @ManyToOne
        @JoinColumn(name = "source_account_id")
        val sourceAccount: AccountEntity? = null,
        @ManyToOne
        @JoinColumn(name = "target_account_id")
        val targetAccount: AccountEntity? = null,
        @Column(name = "description") val description: String? = null,
        @Column(name = "errorMessage") val errorMessage: String? = null)

@Entity
@Table(name = "TRANSACTION_TYPE")
data class TransactionTypeEntity(
        @GeneratedValue
        @Id @Column(name = "transaction_type_id") val transactionTypeId: Long? = null,
        @Enumerated(EnumType.STRING) @Column(name = "code") val code: TransactionTypeCode,
        @Column(name = "description") val description: String
)

@Entity
@Table(name = "CURRENCY")
data class CurrencyEntity(
        @GeneratedValue
        @Id @Column(name = "currency_id") val id: Long? = null,
        @Column(name = "currency_code") val currencyCode: String,
        @Column(name = "numeric_code") val numericCode: Int
)

enum class AccountStatus {
    OPENED, BLOCKED, CLOSED
}

enum class TransactionTypeCode {
    CHECK, TRANSFER
}

enum class TransactionStatus {
    STARTED, COMPLETED, FAILED
}

val DEFAULT_CURRENCY: CurrencyUnit = Monetary.getCurrency("UAH")