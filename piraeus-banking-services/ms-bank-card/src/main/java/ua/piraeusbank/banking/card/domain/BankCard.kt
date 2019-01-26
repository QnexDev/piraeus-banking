package ua.piraeusbank.banking.card.domain

import java.time.LocalDate
import javax.persistence.*

@Entity
data class BankCardNetwork(@Column(name = "payment_card_network_id") val id: Long?,
                           @Column(name = "name") val name: String,
                           @Column(name = "description") val description: String)

enum class BankCardType {
        DEBIT, CREDIT
}

@Entity
data class BankCard(
        @Column(name = "payment_card_id") val id: String,
        @Enumerated(EnumType.STRING) @Column(name = "type") val type: BankCard,
        //TODO Customer entity
        @Column(name = "cardholder_id") val cardholderId: String,
        @Column(name = "name") val name: String,
        @Column(name = "number") val number: Int,
        @Column(name = "bin") val bin: Short,
        @Column(name = "pin") val pin: Short,

        @ManyToOne
        @JoinColumn(name = "payment_card_network_id")
        @Column(name = "network")
        val network: BankCardNetwork,

        @Column(name = "expiration_date") val expirationDate: LocalDate,
        @Column(name = "security_code") val securityCode: Short
)
