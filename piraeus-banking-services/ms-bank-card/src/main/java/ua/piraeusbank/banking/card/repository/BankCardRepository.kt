package ua.piraeusbank.banking.card.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ua.piraeusbank.banking.domain.entity.BankCard

@Repository
internal interface BankCardRepository : JpaRepository<BankCard, Long>