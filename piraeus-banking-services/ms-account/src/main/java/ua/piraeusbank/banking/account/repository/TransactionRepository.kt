package ua.piraeusbank.banking.account.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import ua.piraeusbank.banking.domain.entity.TransactionEntity

interface TransactionRepository : JpaRepository<TransactionEntity, Long> {

    @Query("select t from TransactionEntity t where t.sourceAccountId = :accountId")
    fun getOutgoingTransactionsByAccountId(accountId: Long): List<TransactionEntity>


    @Query("select t from TransactionEntity t where t.targetAccountId = :accountId")
    fun getIncomingTransactionsByAccountId(accountId: Long): List<TransactionEntity>
}