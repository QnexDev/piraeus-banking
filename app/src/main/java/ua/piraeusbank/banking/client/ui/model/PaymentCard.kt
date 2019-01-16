package ua.piraeusbank.banking.client.ui.model

data class PaymentCard(
        val cardName: String,
        val cardNumber: Int,
        val cardType: Type,
        val moneyAmount: String
    ) {
        enum class Type {
            VISA, MASTERCARD
        }
    }
