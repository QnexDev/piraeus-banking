package ua.piraeusbank.banking.customer.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import ua.piraeusbank.banking.domain.entity.StatementEntity
import ua.piraeusbank.banking.domain.entity.TransactionEntity

interface StatementRepository : JpaRepository<StatementEntity, Long> {

    //FIXME
    @Query("select t from StatementEntity t where t.customer = :customerId")
    fun findAllByCustomerId(customerId: Long): List<StatementEntity>

}