package ua.piraeusbank.banking.common.domain

import org.javamoney.moneta.Money
import java.time.Instant
import java.time.LocalDate

data class PaymentCard(
        val id: Long,
        val accountId: Long,
        val type: String,
        val name: String,
        val cardholderId: Long,
        val binCode: Int,
        val balance: Money,
        val networkCode: String,
        val expirationDate: LocalDate)

data class CardTransaction(
        val id: Long? = null,
        val timestamp: Instant,
        val amount: Money? = null,
        val sourceCard: PaymentCard? = null,
        val targetCard: PaymentCard? = null,
        val description: String? = null,
        val errorMessage: String? = null)


data class StatementRecord(
        val statementId: Long? = null,
        val customerId: Long,
        val date: LocalDate,
        val type: String,
        val description: String?,
        val paidIn: Money? = null,
        val paidOut: Money? = null
)

data class Customer(
        val customerId: Long? = null,
        val name: String,
        val lastName: String,
        val phoneNumber: String,
        val email: String,
        val dateOfBirthday: LocalDate
)
