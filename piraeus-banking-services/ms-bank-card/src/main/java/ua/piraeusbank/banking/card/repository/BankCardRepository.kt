package ua.piraeusbank.banking.card.repository

import org.springframework.data.jpa.repository.JpaRepository
import ua.piraeusbank.banking.card.domain.BankCard

interface BankCardRepository : JpaRepository<BankCard, String>