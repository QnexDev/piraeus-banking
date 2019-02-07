package ua.piraeusbank.banking.domain.entity

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


data class BankCardNumber(val number: BigInteger, val binCode: Int)

@Entity
@Table(name = "CARD_PAYMENT_NETWORK")
data class BankCardNetwork(
        @Id @Column(name = "card_payment_network_id") val id: Long? = null,
        @Enumerated(EnumType.STRING)
        @Column(name = "code")
        val code: CardNetworkCode,
        @Column(name = "name") val name: String,
        @Column(name = "prefix_numbers") val prefixNumbers: Int,
        @Column(name = "description") val description: String)

@Entity
@Table(name = "CARD")
data class BankCard(
        @Id
        @GeneratedValue
        @Column(name = "bank_card_id") val id: Long? = null,
        @ManyToOne
        @JoinColumn(name = "account_id")
        val account: AccountEntity,
        @Enumerated(EnumType.STRING)
        @Column(name = "type")
        val type: BankCardType,
        @Enumerated(EnumType.STRING)
        @Column(name = "status")
        val status: BankCardStatus,
        @ManyToOne
        @JoinColumn(name = "customer_id")
        val cardholder: CustomerEntity,
//        @Column(name = "name") val name: String,
        @Column(name = "number") val number: BigInteger,
        @Column(name = "bin_code") val binCode: Int,
        @Column(name = "pin_code") val pinCode: Short,

        @ManyToOne
        @JoinColumn(name = "payment_card_network_id")
        val network: BankCardNetwork,

        @Column(name = "expiration_date") val expirationDate: LocalDate,
        @Column(name = "security_code") val securityCode: Short
)

data class BankCardParams(
        val id: Long? = null,
        val accountId: Long?,
        val type: BankCardType?,
        val status: BankCardStatus?,
        val customerId: Long?,
        val number: BigInteger?,
        val binCode: Int?,
        val pinCode: Short?,
        val network: BankCardNetwork?,
        val expirationDate: LocalDate?,
        val securityCode: Short?
)

enum class BankCardStatus {
    OPENED, CLOSED, BLOCKED
}

enum class BankCardType {
    DEBIT
}

enum class CardNetworkCode {
    VISA, MASTERCARD
}


