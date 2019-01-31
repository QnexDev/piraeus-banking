package ua.piraeusbank.banking.customer.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ua.piraeusbank.banking.customer.repository.CustomerRepository

@Service
class CustomerService(@Autowired val customerRepository: CustomerRepository) {

//    @Transactional(readOnly = true)
//    fun getCustomer(): CustomerEntity {
//
//    }


}