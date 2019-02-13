package ua.piraeusbank.banking.client.ui.model

import java.time.Instant
import java.time.LocalDate

data class StatementRecord(
    val statementId: Long? = null,
    val customerName: String,
    val customerLastname: String,
    val date: LocalDate,
    val type: String,
    val description: String?,
    val paidIn: Money? = null,
    val paidOut: Money? = null,
    val timestamp: Instant
)

enum class TransferType {
    INCOMING, OUTGOING
}