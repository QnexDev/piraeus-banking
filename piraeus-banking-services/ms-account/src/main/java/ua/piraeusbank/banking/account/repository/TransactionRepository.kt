package ua.piraeusbank.banking.account.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import ua.piraeusbank.banking.domain.entity.TransactionEntity

interface TransactionRepository : JpaRepository<TransactionEntity, Long> {

    @Query("select t from TransactionEntity t inner join t.sourceAccount a " +
            "where a.accountId = :accountId")
    fun getOutgoingTransactionsByAccountId(accountId: Long): List<TransactionEntity>


    @Query("select t from TransactionEntity t inner join t.targetAccount a " +
            " where a.accountId = :accountId")
    fun getIncomingTransactionsByAccountId(accountId: Long): List<TransactionEntity>
}