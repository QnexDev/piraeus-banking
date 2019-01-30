package ua.piraeusbank.banking.card.domain

import java.math.BigInteger
import java.time.LocalDate
import javax.persistence.*

data class BankCardData(
        val id: Long? = null,
        val accountId: Long,
        val type: BankCardType,
        val cardholderId: Long,
        val binCode: Int,
        val networkCode: CardNetworkCode,
        val expirationDate: LocalDate)


internal data class BankCardNumber(val number: BigInteger, val binCode: Int)

@Entity
@Table(name = "CARD_NETWORK")
internal data class BankCardNetwork(
        @Column(name = "payment_card_network_id") val id: Long? = null,
        @Enumerated(EnumType.STRING)
        @Column(name = "code")
        val code: CardNetworkCode,
        @Column(name = "name") val name: String,
        @Column(name = "prefix_numbers") val prefixNumbers: Int,
        @Column(name = "description") val description: String)

@Entity
@Table(name = "CARD")
internal data class BankCard(
        @Id
        @GeneratedValue
        @Column(name = "bank_card_id") val id: Long? = null,
        @Column(name = "account_id") val accountId: Long,
        @Enumerated(EnumType.STRING)
        @Column(name = "type")
        val type: BankCardType,
        @Enumerated(EnumType.STRING)
        @Column(name = "status")
        val status: BankCardStatus,
        //TODO Customer entity
        @Column(name = "cardholder_id") val cardholderId: Long,
//        @Column(name = "name") val name: String,
        @Column(name = "number") val number: BigInteger,
        @Column(name = "binCode") val binCode: Int,
        @Column(name = "pinCode") val pinCode: Short,

        @ManyToOne
        @JoinColumn(name = "payment_card_network_id")
        @Column(name = "network")
        val network: BankCardNetwork,

        @Column(name = "expiration_date") val expirationDate: LocalDate,
        @Column(name = "security_code") val securityCode: Short
)

internal enum class BankCardStatus {
    OPENED, CLOSED, BLOCKED
}

enum class BankCardType {
    DEBIT, CREDIT, INTERNET
}

enum class CardNetworkCode {
    VISA, MASTERCARD
}


