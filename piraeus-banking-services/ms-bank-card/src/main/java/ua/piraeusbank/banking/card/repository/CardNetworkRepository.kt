package ua.piraeusbank.banking.card.repository

import org.springframework.data.jpa.repository.JpaRepository
import ua.piraeusbank.banking.domain.entity.BankCardNetworkEntity
import ua.piraeusbank.banking.domain.entity.CardNetworkCode

internal interface CardNetworkRepository : JpaRepository<BankCardNetworkEntity, Long> {

    fun findByCode(code: CardNetworkCode): BankCardNetworkEntity

}