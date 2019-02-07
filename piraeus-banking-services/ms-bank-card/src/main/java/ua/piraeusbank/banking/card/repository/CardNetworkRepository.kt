package ua.piraeusbank.banking.card.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ua.piraeusbank.banking.domain.entity.BankCardNetwork
import ua.piraeusbank.banking.domain.entity.CardNetworkCode

internal interface CardNetworkRepository : JpaRepository<BankCardNetwork, Long> {

    fun findByCode(code: CardNetworkCode): BankCardNetwork

}