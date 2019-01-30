package ua.piraeusbank.banking.card.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ua.piraeusbank.banking.card.domain.BankCard
import ua.piraeusbank.banking.card.domain.BankCardNetwork
import ua.piraeusbank.banking.card.domain.CardNetworkCode

@Repository
internal interface BankNetworkRepository : JpaRepository<BankCardNetwork, Long> {

    fun findByCode(code: CardNetworkCode): BankCardNetwork

}