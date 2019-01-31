package ua.piraeusbank.banking.customer.repository

import org.springframework.data.jpa.repository.JpaRepository
import ua.piraeusbank.banking.domain.entity.StatementEntity

interface StatementRepository : JpaRepository<StatementEntity, Long> {

    fun findAllByCustomerId(customerId: Long): List<StatementEntity>
}