package ua.piraeusbank.banking.card.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ua.piraeusbank.banking.card.domain.BankCard

@Repository
internal interface BankCardRepository : JpaRepository<BankCard, Long>