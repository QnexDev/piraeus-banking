package ua.piraeusbank.banking.client.ui.model

import java.io.Serializable

enum class CardMenuKind : Serializable {
    BLOCK_CARD, UNBLOCK_CARD, ORDER_CARD, GET_SECURITY_CODE, CHANGE_PIN_CODE, CLOSE_CARD
}