package ua.piraeusbank.banking.customer.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ua.piraeusbank.banking.customer.repository.CustomerRepository
import ua.piraeusbank.banking.domain.entity.CustomerEntity

@Service
class CustomerService(@Autowired val customerRepository: CustomerRepository) {

    @Transactional(readOnly = true)
    fun getCustomer(customerId: Long): CustomerEntity = customerRepository.getOne(customerId)


}