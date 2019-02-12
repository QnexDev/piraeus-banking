package ua.piraeusbank.banking.customer.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import ua.piraeusbank.banking.domain.entity.StatementRecordEntity

interface StatementRepository : JpaRepository<StatementRecordEntity, Long> {

    @Query("select t from StatementRecordEntity t inner join t.customer c " +
                        "where c.customerId = :customerId")
    fun findAllByCustomerId(customerId: Long): List<StatementRecordEntity>

}