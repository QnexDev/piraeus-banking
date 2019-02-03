package ua.piraeusbank.banking.client.ui.model

data class StatementRecord(
    val senderCard: String,
    val recipientCard: String,
    val senderAccount: String,
    val recipientAccount: String,
    val transferType: TransferType,
    val description: String,
    val timestamp: String,
    val amount: String,
    val currency: String
)

enum class TransferType {
    INCOMING, OUTGOING
}