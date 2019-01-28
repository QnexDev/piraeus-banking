package ua.piraeusbank.banking.account.repository

import org.springframework.data.jpa.repository.JpaRepository
import ua.piraeusbank.banking.account.domain.AccountTransaction

interface AccountTransactionRepository: JpaRepository<AccountTransaction, Long>