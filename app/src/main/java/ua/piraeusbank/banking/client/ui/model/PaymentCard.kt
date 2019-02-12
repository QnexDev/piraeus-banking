package ua.piraeusbank.banking.client.ui.model

import java.math.BigDecimal
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
    val expirationDate: LocalDate
)

data class Money(val amount: BigDecimal, val currency: String)