package ua.piraeusbank.banking.account.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import ua.piraeusbank.banking.account.domain.TransactionEntity

interface TransactionRepository : JpaRepository<TransactionEntity, Long> {

    @Query("select t from transaction t where t.sourceAccountId")
    fun getOutgoingTransactionsByAccountId(accountId: Long)


    @Query("select t from transaction t where t.targetAccountId")
    fun getIncommingTransactionsByAccountId(accountId: Long)
}