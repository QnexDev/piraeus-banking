package ua.piraeusbank.banking.customer.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import ua.piraeusbank.banking.customer.service.CustomerService
import ua.piraeusbank.banking.customer.service.StatementService
import ua.piraeusbank.banking.domain.entity.CustomerEntity
import ua.piraeusbank.banking.domain.entity.StatementEntity

@RestController
class CustomerController(
        @Autowired private val statementService: StatementService,
        @Autowired private val customerService: CustomerService) {

    @GetMapping("/statements/{customerId}")
    fun getStatements(@PathVariable("customerId") customerId: Long): List<StatementEntity> =
            statementService.getStatements(customerId)

    @GetMapping("/{customerId}")
    fun getCustomer(@PathVariable("customerId") customerId: Long): CustomerEntity =
            customerService.getCustomer(customerId)

}