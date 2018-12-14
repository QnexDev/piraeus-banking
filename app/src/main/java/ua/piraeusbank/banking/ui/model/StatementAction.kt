package ua.piraeusbank.banking.ui.model

data class StatementAction(
    val transferType: TransferType,
    val description: String,
    val timestamp: String
) {
    enum class TransferType {
        INCOMING, OUTGOING
    }

}