package ua.piraeusbank.banking.customer.repository

import org.springframework.data.jpa.repository.JpaRepository
import ua.piraeusbank.banking.domain.entity.CustomerEntity
import java.util.*

interface CustomerRepository: JpaRepository<CustomerEntity, Long> {

    fun findByPhoneNumber(phoneNumber: String): Optional<CustomerEntity>
}