package ua.piraeusbank.banking.customer.repository

import org.springframework.data.jpa.repository.JpaRepository
import ua.piraeusbank.banking.domain.entity.CustomerEntity

interface CustomerRepository: JpaRepository<CustomerEntity, Long>